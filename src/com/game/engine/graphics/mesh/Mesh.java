package com.game.engine.graphics.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Mesh {

	private int vaoId;
	private int vertexCount;

	public Mesh(float[] positions, float[] texCoords, float[] normals) {

		vertexCount = positions.length / 3;

		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);

		// Position
		int posVbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, posVbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positions, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

		// Texture
		int texVbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texVbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texCoords, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);

		// Normals
		int normVbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normVbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normals, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		GL30.glBindVertexArray(0);
	}

	public void render() {
		GL30.glBindVertexArray(vaoId);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
	}
}
