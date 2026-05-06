package com.game.game;

import java.util.ArrayList;
import java.util.List;

import com.game.engine.graphics.Camera;
import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.graphics.material.Material;
import com.game.engine.graphics.mesh.Mesh;
import com.game.engine.graphics.mesh.MeshFactory;
import com.game.engine.graphics.texture.Entity;
import com.game.engine.graphics.texture.Texture;
import com.game.engine.particles.ParticleEmitter;
import com.game.engine.particles.ParticleRenderer;
import com.game.engine.renderer.Renderer;
import com.game.engine.shader.ParticleShader;
import com.game.engine.shader.ShaderProgram;
import com.game.engine.shader.UIShader;
import com.game.engine.shader.WorldShader;
import com.game.engine.ui.UIMesh;
import com.game.engine.ui.UIRenderer;
import com.game.engine.utils.OBJLoader;
import com.game.engine.window.GameWindow;
import com.game.game.utils.EnemySpawner;

public class Game implements IGameLogic {

	private Mesh enemyMesh;
	private Mesh playerMesh;

	private Player player;
	private List<Enemy> enemies = new ArrayList<>();
	private List<Entity> renderList = new ArrayList<>();

	private Renderer renderer;
	private WorldShader worldShader;
	private UIShader uiShader;
	private ParticleShader particleShader;
	private Camera camera;
	private Entity cube;
	private UIRenderer ui;
	private long lastTime = System.nanoTime();
	private EnemySpawner spawner;
	private ParticleEmitter particleEmitter;
	private ParticleRenderer particleRenderer;
	private Mesh particleMesh;
	private Texture particleTexture;
	private DirectionalLight light;
	private Material material;

	@Override
	public void cleanup() {
		worldShader.cleanup();
		uiShader.cleanup();
		particleShader.cleanup();
	}

	@Override
	public void init(GameWindow window) {

		// SET UP START ----------------------------------------------

		ui = new UIRenderer();
		camera = new Camera();
		spawner = new EnemySpawner();
		spawner.setEnemies(enemies);

		worldShader = new WorldShader(ShaderProgram.load("assets/shaders/vertex.glsl"),
				ShaderProgram.load("assets/shaders/fragment.glsl"));

		uiShader = new UIShader(ShaderProgram.load("assets/ui/ui.vert"), ShaderProgram.load("assets/ui/ui.frag"));

		particleShader = new ParticleShader(ShaderProgram.load("assets/particles/particle.vert"),
				ShaderProgram.load("assets/particles/particle.frag"));

		particleEmitter = new ParticleEmitter();
		particleRenderer = new ParticleRenderer();

		renderer = new Renderer(window.getWidth(), window.getHeight());

		enemyMesh = OBJLoader.loadMesh("assets/models/sphere.obj");
		playerMesh = OBJLoader.loadMesh("assets/models/plane.obj");

		cube = new Entity(OBJLoader.loadMesh("assets/models/cube.obj"));

		player = new Player(camera, new Entity(playerMesh));

		player.getGun().setEmitter(particleEmitter);
		spawner.setEmitter(particleEmitter);

		// INIT SHADER, RENDERER, PARTICLE ---------------------------

		particleTexture = new Texture("assets/textures/particle.png");
		particleMesh = MeshFactory.createQuad();
		particleRenderer.setShader(particleShader);
		particleRenderer.setQuad(particleMesh);
		particleRenderer.setTexture(particleTexture);

		ui.setShader(uiShader);
		ui.setQuad(new UIMesh());

		// PBR

		light = new DirectionalLight();
		light.direction.set(-1f, -1f, -1f).normalize();
		light.color.set(1f, 1f, 1f);
		light.intensity = 1.0f;

		material = new Material();
		material.albedo.set(1, 0.7f, 0.6f);
		material.ambient = 0.1f;
		material.specular = 0.5f;
		material.shininess = 32f;

		cube.setMaterial(material);

		renderer.setSceneLight(light);

		cube.getModelMatrix().identity().translate(0, 0, -5);

		// GUN CALLBACK ------------------------------------------------

//		player.getGun().setOnShoot(() -> {
//			ui.triggerCrosshairExpand();
//		});

		// MOUSE LOOK --------------------------------------------------

		window.setCursorCallback(window.getHandle(), camera);
	}

	@Override
	public void input(GameWindow window) {
		player.update(window.getHandle(), enemies);
	}

	@Override
	public void render(GameWindow window) {

		renderer.clear();
		renderList.clear();

		renderList.add(cube);

		for (Enemy e : enemies) {
			Entity entity = e.getEntity();
			entity.setMaterial(material);
			renderList.add(entity);
		}

		// --- 3D PASS ---
		renderer.submit(renderList, worldShader, camera);
		renderer.renderPass();

		// --- PARTICLES ---
		particleRenderer.submit(particleEmitter.getParticles(), renderer.getProjection(), camera.getViewMatrix());
		particleRenderer.renderPass();

		// --- UI ---
		ui.submit(window.getWidth(), window.getHeight(), player.getHealth(), player.getGun().getAmmo());
		ui.renderPass();
	}

	@Override
	public void update() {

		long now = System.nanoTime();
		float deltaTime = (now - lastTime) / 1_000_000_000f;
		lastTime = now;

		spawner.update(deltaTime, camera.position, new Entity(enemyMesh));

//		if (player.moving()) {
//			ui.update(deltaTime);
//		}

		for (Enemy e : spawner.getEnemies()) {
			e.update(player);
		}

		particleEmitter.update(deltaTime);

		enemies.removeIf(Enemy::isDead);

		System.out.println("Particles: " + particleEmitter.getParticles().size());
	}

}
