package com.game.engine.graphics.mesh;

public class MeshFactory {

	public static Mesh createQuad() {

		float[] vertices = { -0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, -0.5f, 0f,

				0.5f, -0.5f, 0f, 0.5f, 0.5f, 0f, -0.5f, 0.5f, 0f };

		float[] texCoords = { 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0 };

		float[] normals = { 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1 };

		return new Mesh(vertices, texCoords, normals);
	}
}
