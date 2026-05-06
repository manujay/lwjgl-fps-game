package com.game.engine.graphics.material;

import org.joml.Vector3f;

public class Material {
	public Vector3f albedo = new Vector3f(1, 1, 1); // base color
	public float ambient = 0.1f;
	public float specular = 0.5f;
	public float shininess = 32.0f;
}