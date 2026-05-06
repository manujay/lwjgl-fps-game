package com.game.engine.ui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class UIMesh {

	private int vao, vbo;

	public UIMesh() {
		float[] vertices = { 0f, 0f, 1f, 0f, 1f, 1f,

				0f, 0f, 1f, 1f, 0f, 1f };

		vao = GL30.glGenVertexArrays();
		vbo = GL15.glGenBuffers();

		GL30.glBindVertexArray(vao);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
		GL20.glEnableVertexAttribArray(0);

		GL30.glBindVertexArray(0);
	}

	public void render() {
		GL30.glBindVertexArray(vao);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
	}
}