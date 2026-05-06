package com.game.engine.shader;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class UIShader extends ShaderProgram {

	public UIShader(String vs, String fs) {
		super(vs, fs);
	}

	@Override
	protected void createUniforms() {
		createUniform("projection");
		createUniform("color");
	}

	public void setColor(Vector4f color) {
		setUniform("color", color);
	}

	public void setProjection(Matrix4f proj) {
		setUniform("projection", proj);
	}
}
