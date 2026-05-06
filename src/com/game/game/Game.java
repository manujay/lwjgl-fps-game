package com.game.game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import com.game.engine.graphics.Camera;
import com.game.engine.graphics.mesh.Entity;
import com.game.engine.graphics.mesh.Mesh;
import com.game.engine.graphics.mesh.MeshFactory;
import com.game.engine.graphics.texture.Texture;
import com.game.engine.particles.ParticleEmitter;
import com.game.engine.particles.ParticleRenderer;
import com.game.engine.renderer.Renderer;
import com.game.engine.shader.ShaderProgram;
import com.game.engine.ui.UIRenderer;
import com.game.engine.utils.OBJLoader;
import com.game.engine.window.GameWindow;
import com.game.game.utils.EnemySpawner;

public class Game implements IGameLogic {

	private Mesh enemyMesh;
	private Mesh playerMesh;

	private Player player;
	private List<Enemy> enemies = new ArrayList<>();

	private Renderer renderer;
	private ShaderProgram shader;
	private ShaderProgram particleShader;
	private Camera camera;
	private Entity cube;
	private UIRenderer ui;
	private long lastTime = System.nanoTime();
	private EnemySpawner spawner;
	private ParticleEmitter particleEmitter;
	private ParticleRenderer particleRenderer;

	@Override
	public void cleanup() {
		shader.cleanup();
	}

	@Override
	public void init(GameWindow window) {
		ui = new UIRenderer();
		camera = new Camera();
		spawner = new EnemySpawner();

		Mesh particleMesh = MeshFactory.createQuad();
		particleEmitter = new ParticleEmitter();

		enemyMesh = OBJLoader.loadMesh("assets/models/sphere.obj");
		playerMesh = OBJLoader.loadMesh("assets/models/plane.obj");

		player = new Player(camera, new Entity(playerMesh));

		player.getGun().setEmitter(particleEmitter);
		spawner.setEmitter(particleEmitter);

		player.getGun().setOnShoot(() -> {
			ui.triggerCrosshairExpand();
		});

		enemies.add(new Enemy(new Vector3f(0, 1, -5), new Entity(enemyMesh)));

		enemies.add(new Enemy(new Vector3f(2, 1, -8), new Entity(enemyMesh)));

		renderer = new Renderer(800, 600);

		shader = new ShaderProgram();
		shader.createVertexShader(ShaderProgram.load("assets/shaders/vertex.glsl"));
		shader.createFragmentShader(ShaderProgram.load("assets/shaders/fragment.glsl"));
		shader.link();

		shader.createUniform("projection");
		shader.createUniform("view");
		shader.createUniform("model");

		cube = new Entity(OBJLoader.loadMesh("assets/models/cube.obj"));

		window.setCursorCallback(window.getHandle(), camera);

		Texture texture = new Texture("assets/textures/fireball.png");
		particleShader = new ShaderProgram();
		particleShader.createVertexShader(ShaderProgram.load("assets/particles/particle.vert"));
		particleShader.createFragmentShader(ShaderProgram.load("assets/particles/particle.frag"));
		particleShader.link();
		particleShader.createUniform("projection");
		particleShader.createUniform("view");
		particleShader.createUniform("particlePos");
		particleShader.createUniform("size");
		particleShader.createUniform("color");
		particleShader.createUniform("rows");
		particleShader.createUniform("cols");
		particleShader.createUniform("frame");
//		particleRenderer = new ParticleRenderer(particleMesh, particleShader);
		particleRenderer = new ParticleRenderer(particleMesh, particleShader, texture);

		particleEmitter.burst(new Vector3f(0, 1, -3), 50);
	}

	@Override
	public void input(GameWindow window) {
		player.update(window.getHandle(), enemies);
	}

	@Override
	public void render(GameWindow window) {

		renderer.clear();

		// 3D rendering
		renderer.render(cube, shader, camera);

		// render enemies
		for (Enemy enemy : enemies) {
			renderer.render(enemy.getEntity(), shader, camera);
		}

//		particleRenderer.render(particleEmitter.getParticles(), camera.position);
		particleRenderer.render(particleEmitter.getParticles(), renderer.getProjection(), camera.getViewMatrix());

		// 🎯 UI Rendering (AFTER 3D)
		ui.renderCrosshair(800, 600);

		ui.renderHUD(800, 600, player.getHealth(), player.getGun().getAmmo(), player.getGun().getReserveAmmo());

	}

	@Override
	public void update() {

		long now = System.nanoTime();
		float deltaTime = (now - lastTime) / 1_000_000_000f;
		lastTime = now;

		spawner.update(deltaTime, enemies, camera.position, new Entity(enemyMesh));

		if (player.moving()) {
			ui.updateCrosshair(deltaTime);
		}

		for (Enemy e : enemies) {
			e.update(player);
		}

		particleEmitter.update(deltaTime);

		enemies.removeIf(Enemy::isDead);

		System.out.println("Particles: " + particleEmitter.getParticles().size());
	}

}
