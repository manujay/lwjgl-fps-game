package com.game.game;

import org.joml.Vector3f;

import com.game.engine.graphics.texture.Entity;

public class Enemy {

	private Vector3f position;
	private Entity entity;
	private int health = 100;
	private float speed = 0.02f;

	public Enemy(Vector3f position, Entity entity) {
		this.position = position;
		this.entity = entity;
	}

	public void damage(int dmg) {
		health -= dmg;
	}

	public Entity getEntity() {
		return entity;
	}

	public Vector3f getPosition() {
		return position;
	}

	public boolean isDead() {
		return health <= 0;
	}

	private Vector3f playerPosition(Player player) {
		return player.getCamera().position;
	}

	public void setSpeed(float factor) {
		speed *= factor;
	}

	public void update(Player player) {

		Vector3f playerPos = playerPosition(player);

		// Move toward player
		Vector3f direction = new Vector3f(playerPos).sub(position).normalize();

		position.add(direction.mul(speed));

		// Sync render entity position
		entity.getPosition().set(position);

		// Attack if close
		if (position.distance(playerPos) < 1.5f) {
			player.damage(1);
		}
	}
}