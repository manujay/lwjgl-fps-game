package com.game.engine.shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ParticleShader extends ShaderProgram {

	public ParticleShader(String vs, String fs) {
		super(vs, fs);
	}

	public void bindTexture(int unit) {
		setUniform("tex", unit);
	}

	@Override
	protected void createUniforms() {
		createUniform("projection");
		createUniform("view");
		createUniform("particlePos");
		createUniform("size");
		createUniform("color");
		createUniform("rows");
		createUniform("cols");
		createUniform("frame");
		createUniform("tex");
	}

	public void setAtlas(int rows, int cols) {
		setUniform("rows", rows);
		setUniform("cols", cols);
	}

	public void setCamera(Matrix4f proj, Matrix4f view) {
		setUniform("projection", proj);
		setUniform("view", view);
	}

	public void setParticle(Vector3f pos, float size, Vector4f color, int frame) {
		setUniform("particlePos", pos);
		setUniform("size", size);
		setUniform("color", color);
		setUniform("frame", frame);
	}
}