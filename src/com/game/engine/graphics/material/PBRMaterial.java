package com.game.engine.graphics.material;

import org.joml.Vector3f;

public class PBRMaterial {
	public Vector3f albedo = new Vector3f(1);
	public float metallic = 0.0f;
	public float roughness = 0.5f;
	public float ao = 1.0f;
}
