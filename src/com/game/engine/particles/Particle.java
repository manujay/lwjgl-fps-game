package com.game.engine.particles;

import org.joml.Vector3f;

public class Particle {

	public Vector3f position = new Vector3f();
	public Vector3f velocity = new Vector3f();

	public float life; // remaining time
	public float maxLife; // initial time

	public float size, rotation;
	public float r, g, b, a;

	public int frame = 0;
	public int totalFrames = 16;

	public float frameTime = 0.05f; // seconds per frame
	private float frameTimer = 0f;

	public boolean alive = true;

	public void update(float dt) {
		if (!alive) {
			return;
		}

		life -= dt;
		if (life <= 0f) {
			alive = false;
			return;
		}

		position.add(new Vector3f(velocity).mul(dt));

		// Fade out
		a = life / maxLife;

		// animation
		frameTimer += dt;
		if (frameTimer >= frameTime) {
			frameTimer = 0f;
			frame++;
			if (frame >= totalFrames) {
				frame = totalFrames - 1; // clamp (or loop)
			}
		}
	}
}