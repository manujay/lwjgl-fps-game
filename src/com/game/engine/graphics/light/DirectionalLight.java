package com.game.engine.graphics.light;

import org.joml.Vector3f;

public class DirectionalLight {
	public Vector3f direction = new Vector3f(-0.2f, -1.0f, -0.3f).normalize();
	public Vector3f color = new Vector3f(1.0f, 1.0f, 1.0f);
	public float intensity = 1.0f;
}