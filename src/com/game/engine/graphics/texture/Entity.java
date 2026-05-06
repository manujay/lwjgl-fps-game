package com.game.engine.graphics.texture;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.game.engine.graphics.material.Material;
import com.game.engine.graphics.mesh.Mesh;

public class Entity {

	private Mesh mesh;
	public Vector3f position = new Vector3f();
	public Vector3f rotation = new Vector3f(); // in degrees
	public float scale = 1.0f;

	private Matrix4f modelMatrix = new Matrix4f();
	private Material material = new Material();

	public Entity(Mesh mesh) {
		this.mesh = mesh;
	}

	public Material getMaterial() {
		return material;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public Matrix4f getModelMatrix() {

		modelMatrix.identity().translate(position).rotateX((float) Math.toRadians(rotation.x))
				.rotateY((float) Math.toRadians(rotation.y)).rotateZ((float) Math.toRadians(rotation.z)).scale(scale);

		return modelMatrix;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
}
