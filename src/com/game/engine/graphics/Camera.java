package com.game.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	public Vector3f position = new Vector3f(0, 0, 3);
	public float pitch, yaw;

	public Vector3f getForward() {
		return new Vector3f((float) Math.sin(yaw), 0, (float) -Math.cos(yaw)).normalize();
	}

	public Vector3f getRight() {
		return new Vector3f((float) Math.cos(yaw), 0, (float) Math.sin(yaw)).normalize();
	}

	public Matrix4f getViewMatrix() {
		return new Matrix4f().rotateX(pitch).rotateY(yaw).translate(-position.x, -position.y, -position.z);
	}
}
