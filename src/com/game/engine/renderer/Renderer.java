package com.game.engine.renderer;

import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.game.engine.graphics.Camera;
import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.graphics.texture.Entity;
import com.game.engine.shader.WorldShader;

public class Renderer extends AbstractRenderer {

	private WorldShader shader;
	private Camera camera;
	private DirectionalLight sceneLight;
	private List<Entity> entities;

	private Matrix4f projection;

	public Renderer(int width, int height) {
		projection = new Matrix4f().perspective((float) Math.toRadians(70.0f), (float) width / height, 0.01f, 1000.0f);
	}

	@Override
	protected void begin() {
		enableDepth();
		disableBlend();
		enableCulling();
	}

	// Clear screen (called externally once per frame)
	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	protected void end() {
		// Nothing fancy needed here, but keep symmetry
		enableDepth();
	}

	public Matrix4f getProjection() {
		return projection;
	}

	@Override
	protected void render() {

		if (entities == null || shader == null || camera == null) {
			return;
		}

		shader.bind();

		// FRAME
		shader.setProjection(projection);
		shader.setCamera(camera.position);
		shader.setLight(sceneLight);

		// OBJECTS
		for (Entity entity : entities) {
			shader.setModel(entity.getModelMatrix());
			shader.setMaterial(entity.getMaterial());
			entity.getMesh().render();
		}
	}

	public void setSceneLight(DirectionalLight sceneLight) {
		this.sceneLight = sceneLight;
	}

	// Submit data for this frame
	public void submit(List<Entity> entities, WorldShader shader, Camera camera) {
		this.entities = entities;
		this.shader = shader;
		this.camera = camera;
	}
}