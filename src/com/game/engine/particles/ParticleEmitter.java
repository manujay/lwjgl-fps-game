package com.game.engine.particles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector3f;

public class ParticleEmitter {

	private List<Particle> particles = new ArrayList<>();
	private Random rand = new Random();

	// 💥 Generic burst
	public void burst(Vector3f origin, int count) {
		for (int i = 0; i < count; i++) {
			Particle p = new Particle();

			p.position.set(origin);

			p.velocity.set((rand.nextFloat() - 0.5f) * 4f, rand.nextFloat() * 4f, (rand.nextFloat() - 0.5f) * 4f);

			p.maxLife = 0.5f + rand.nextFloat() * 0.5f;
			p.life = p.maxLife;

			p.size = 0.05f + rand.nextFloat() * 0.05f;

			p.r = 1.0f;
			p.g = 0.6f + rand.nextFloat() * 0.4f;
			p.b = 0.1f;
			p.a = 1.0f;

			particles.add(p);
		}
	}

	public List<Particle> getParticles() {
		return particles;
	}

	public void update(float dt) {
		for (Particle p : particles) {
			p.update(dt);
		}
		particles.removeIf(p -> !p.alive);
	}
}
