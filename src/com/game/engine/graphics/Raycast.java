package com.game.engine.graphics;

import org.joml.Vector3f;

public class Raycast {

	public static boolean hit(Vector3f origin, Vector3f dir, Vector3f target, float radius) {

		Vector3f toTarget = new Vector3f(target).sub(origin);
		float proj = toTarget.dot(dir);

		if (proj < 0) {
			return false;
		}

		Vector3f closest = new Vector3f(dir).mul(proj).add(origin);
		return closest.distance(target) < radius;
	}
}