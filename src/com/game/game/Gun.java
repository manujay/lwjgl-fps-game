package com.game.game;

import java.util.List;

import org.joml.Vector3f;

import com.game.engine.graphics.Raycast;
import com.game.engine.particles.ParticleEmitter;

public class Gun {

	private int damage = 25;

	// Ammo system
	private int magazineSize = 10;
	private int ammo = magazineSize;
	private int reserveAmmo = 50;

	// Timing
	private long lastShotTime = 0;
	private long fireDelay = 200; // ms between shots

	private boolean reloading = false;
	private long reloadStartTime = 0;
	private long reloadTime = 1500; // ms
	private Runnable onShoot;
	private ParticleEmitter emitter;

	// -------------------------
	// Getters (for UI later)
	// -------------------------
	public int getAmmo() {
		return ammo;
	}

	public ParticleEmitter getEmitter() {
		return emitter;
	}

	public int getReserveAmmo() {
		return reserveAmmo;
	}

	public boolean isReloading() {
		return reloading;
	}

	// -------------------------
	// Reload
	// -------------------------
	public void reload() {
		if (reloading || (ammo == magazineSize) || (reserveAmmo <= 0)) {
			return;
		}

		reloading = true;
		reloadStartTime = System.currentTimeMillis();
	}

	public void setEmitter(ParticleEmitter emitter) {
		this.emitter = emitter;
	}

	public void setOnShoot(Runnable callback) {
		this.onShoot = callback;
	}

	// -------------------------
	// Shoot (hitscan)
	// -------------------------
	public void shoot(Vector3f origin, Vector3f direction, List<Enemy> enemies) {

		long now = System.currentTimeMillis();

		// Cannot shoot while reloading
		// Fire rate limit
		if (reloading || (now - lastShotTime < fireDelay)) {
			return;
		}

		// No ammo → reload
		if (ammo <= 0) {
			reload();
			return;
		}

		lastShotTime = now;
		ammo--;

		if (onShoot != null) {
			onShoot.run();
		}

		// emit gun shot
		emitter.burst(origin, 25);

		// Hit detection
		for (Enemy enemy : enemies) {
			if (Raycast.hit(origin, direction, enemy.getPosition(), 1.0f)) {
				enemy.damage(damage);
				if (getEmitter() != null) {
					// emit hit burst
					emitter.burst(enemy.getPosition(), 1000);
				}
			}
		}

		// (Optional later)
		// playSound("gunshot.wav");
	}

	// -------------------------
	// Update (handle reload timing)
	// -------------------------
	public void update() {
		if (reloading) {
			long now = System.currentTimeMillis();

			if (now - reloadStartTime >= reloadTime) {
				int needed = magazineSize - ammo;
				int toReload = Math.min(needed, reserveAmmo);

				ammo += toReload;
				reserveAmmo -= toReload;

				reloading = false;
			}
		}
	}
}