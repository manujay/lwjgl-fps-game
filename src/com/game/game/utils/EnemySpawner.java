package com.game.game.utils;

import java.util.List;
import java.util.Random;

import org.joml.Vector3f;

import com.game.engine.graphics.mesh.Entity;
import com.game.engine.particles.ParticleEmitter;
import com.game.game.Enemy;

public class EnemySpawner {

	private float spawnInterval = 2.5f; // seconds
	private float timer = 0f;

	private int maxEnemies = 10;

	private Random random = new Random();

	private float spawnRadius = 20f;

	private ParticleEmitter emitter;

	public ParticleEmitter getEmitter() {
		return emitter;
	}

	private Vector3f getSpawnPosition(Vector3f playerPos) {

		float angle = random.nextFloat() * (float) Math.PI * 2f;
		float distance = 10f + random.nextFloat() * spawnRadius;

		float x = playerPos.x + (float) Math.cos(angle) * distance;
		float z = playerPos.z + (float) Math.sin(angle) * distance;

		return new Vector3f(x, 1, z);
	}

	public void setEmitter(ParticleEmitter emitter) {
		this.emitter = emitter;
	}

	public void update(float deltaTime, List<Enemy> enemies, Vector3f playerPos, Entity enemyTemplate) {

		timer += deltaTime;

		if (timer >= spawnInterval && enemies.size() < maxEnemies) {
			timer = 0f;

			Vector3f spawnPos = getSpawnPosition(playerPos);

			Enemy enemy = new Enemy(spawnPos, new Entity(enemyTemplate.getMesh()));

			if (getEmitter() != null) {
				emitter.burst(spawnPos, 25);
			}
			enemies.add(enemy);
		}
	}
}
