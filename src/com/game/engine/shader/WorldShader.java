package com.game.engine.shader;

import org.joml.Matrix4f;

public class WorldShader extends ShaderProgram {

	public WorldShader(String vs, String fs) {
		super(vs, fs);
	}

	@Override
	protected void createUniforms() {
		createUniform("projection");
		createUniform("view");
		createUniform("model");
	}

	public void setMatrices(Matrix4f proj, Matrix4f view, Matrix4f model) {
		setUniform("projection", proj);
		setUniform("view", view);
		setUniform("model", model);
	}
}
