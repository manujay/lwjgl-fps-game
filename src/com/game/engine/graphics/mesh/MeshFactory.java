package com.game.engine.graphics.mesh;

public class MeshFactory {

	public static Mesh createQuad() {

		float[] vertices = {
				// Triangle 1
				-0.5f, 0.5f, 0f, // top-left
				-0.5f, -0.5f, 0f, // bottom-left
				0.5f, -0.5f, 0f, // bottom-right

				// Triangle 2
				-0.5f, 0.5f, 0f, // top-left
				0.5f, -0.5f, 0f, // bottom-right
				0.5f, 0.5f, 0f // top-right
		};

		float[] texCoords = {
				// Triangle 1
				0f, 0f, // top-left
				0f, 1f, // bottom-left
				1f, 1f, // bottom-right

				// Triangle 2
				0f, 0f, // top-left
				1f, 1f, // bottom-right
				1f, 0f // top-right
		};

		float[] normals = { 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1 };

		return new Mesh(vertices, texCoords, normals);
	}
}
