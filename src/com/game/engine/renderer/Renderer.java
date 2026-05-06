package com.game.engine.renderer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.game.engine.graphics.Camera;
import com.game.engine.graphics.mesh.Entity;
import com.game.engine.shader.ShaderProgram;

public class Renderer {

	private Matrix4f projection;

	public Renderer(int width, int height) {
		float aspect = (float) width / height;

		projection = new Matrix4f().perspective((float) Math.toRadians(70), aspect, 0.1f, 1000f);
	}

	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public Matrix4f getProjection() {
		return projection;
	}

	public void render(Entity entity, ShaderProgram shader, Camera camera) {
		shader.bind();

		// Projection
		shader.setUniform("projection", projection);

		// View (camera)
		shader.setUniform("view", camera.getViewMatrix());

		// Model
		Matrix4f model = new Matrix4f().translate(entity.getPosition()).scale(0.2f); // adjust as needed

		shader.setUniform("model", model);

		entity.getMesh().render();

		shader.unbind();
	}

}
