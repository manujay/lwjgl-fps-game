package com.game.engine.shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.graphics.material.Material;

public class WorldShader extends ShaderProgram {

	public WorldShader(String vs, String fs) {
		super(vs, fs);
	}

	@Override
	protected void createUniforms() {
		createUniform("projection");
		createUniform("view");
		createUniform("model");

		createUniform("viewPos");

		createUniform("light.direction");
		createUniform("light.color");
		createUniform("light.intensity");

		createUniform("material.albedo");
		createUniform("material.ambient");
		createUniform("material.specular");
		createUniform("material.shininess");
	}

	public void setCamera(Vector3f camPos) {
		setUniform("viewPos", camPos);
	}

	public void setLight(DirectionalLight l) {
		setUniform("light.direction", l.direction);
		setUniform("light.color", l.color);
		setUniform("light.intensity", l.intensity);
	}

	public void setMaterial(Material m) {
		setUniform("material.albedo", m.albedo);
		setUniform("material.ambient", m.ambient);
		setUniform("material.specular", m.specular);
		setUniform("material.shininess", m.shininess);
	}

	public void setModel(Matrix4f model) {
		setUniform("model", model);
	}

	public void setProjection(Matrix4f proj) {
		setUniform("projection", proj);
	}

	public void setView(Matrix4f view) {
		setUniform("view", view);
	}
}
