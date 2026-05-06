package com.game.engine.graphics.mesh;

import org.joml.Vector3f;

public class Entity {

	private Mesh mesh;
	private Vector3f position = new Vector3f();

	public Entity(Mesh mesh) {
		this.mesh = mesh;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public Vector3f getPosition() {
		return position;
	}

}
