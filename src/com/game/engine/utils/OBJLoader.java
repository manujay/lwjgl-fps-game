package com.game.engine.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.game.engine.graphics.mesh.Mesh;

public class OBJLoader {

	public static Mesh loadMesh(String filePath) {

		List<Vector3f> positions = new ArrayList<>();
		List<Vector2f> texCoords = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();

		List<Integer> indices = new ArrayList<>();

		List<Vector3f> finalVertices = new ArrayList<>();
		List<Vector2f> finalTexCoords = new ArrayList<>();
		List<Vector3f> finalNormals = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

			String line;

			while ((line = reader.readLine()) != null) {

				String[] tokens = line.split("\\s+");

				switch (tokens[0]) {

				case "v":
					positions.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]),
							Float.parseFloat(tokens[3])));
					break;

				case "vt":
					texCoords.add(new Vector2f(Float.parseFloat(tokens[1]), 1 - Float.parseFloat(tokens[2]) // flip Y
					));
					break;

				case "vn":
					normals.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]),
							Float.parseFloat(tokens[3])));
					break;

				case "f":
					for (int i = 1; i <= 3; i++) {
						String[] parts = tokens[i].split("/");

						int posIndex = Integer.parseInt(parts[0]) - 1;
						int texIndex = parts.length > 1 && !parts[1].isEmpty() ? Integer.parseInt(parts[1]) - 1 : -1;
						int normIndex = parts.length > 2 ? Integer.parseInt(parts[2]) - 1 : -1;

						finalVertices.add(positions.get(posIndex));

						if (texIndex >= 0) {
							finalTexCoords.add(texCoords.get(texIndex));
						} else {
							finalTexCoords.add(new Vector2f());
						}

						if (normIndex >= 0) {
							finalNormals.add(normals.get(normIndex));
						} else {
							finalNormals.add(new Vector3f());
						}
					}
					break;
				}
			}

		} catch (IOException e) {
			throw new RuntimeException("Failed to load OBJ: " + filePath, e);
		}

		// Convert to arrays
		float[] vertices = new float[finalVertices.size() * 3];
		float[] texArray = new float[finalTexCoords.size() * 2];
		float[] normalArray = new float[finalNormals.size() * 3];

		for (int i = 0; i < finalVertices.size(); i++) {
			Vector3f v = finalVertices.get(i);
			vertices[i * 3] = v.x;
			vertices[i * 3 + 1] = v.y;
			vertices[i * 3 + 2] = v.z;
		}

		for (int i = 0; i < finalTexCoords.size(); i++) {
			Vector2f t = finalTexCoords.get(i);
			texArray[i * 2] = t.x;
			texArray[i * 2 + 1] = t.y;
		}

		for (int i = 0; i < finalNormals.size(); i++) {
			Vector3f n = finalNormals.get(i);
			normalArray[i * 3] = n.x;
			normalArray[i * 3 + 1] = n.y;
			normalArray[i * 3 + 2] = n.z;
		}

		return new Mesh(vertices, texArray, normalArray);
	}
}