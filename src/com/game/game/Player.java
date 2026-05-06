package com.game.game;

import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.game.engine.graphics.Camera;
import com.game.engine.graphics.texture.Entity;

public class Player {

	private Camera camera;
	private Entity entity;
	private float speed = 0.08f;
	private int health = 100;
	private Gun gun = new Gun();
	private boolean moving = false;

	public Player(Camera camera, Entity entity) {
		this.camera = camera;
		this.entity = entity;
	}

	public void damage(int amount) {
		health -= amount;
	}

	public Camera getCamera() {
		return camera;
	}

	public Gun getGun() {
		return gun;
	}

	public int getHealth() {
		return health;
	}

	public boolean moving() {
		return moving;
	}

	public void update(long window, List<Enemy> enemies) {
		entity.getPosition().set(camera.position);

		// Movement (unchanged)
		Vector3f forward = camera.getForward();
		Vector3f right = camera.getRight();

		moving = true;

		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
			camera.position.add(new Vector3f(forward).mul(speed));
		}

		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
			camera.position.sub(new Vector3f(forward).mul(speed));
		}

		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
			camera.position.sub(new Vector3f(right).mul(speed));
		}

		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
			camera.position.add(new Vector3f(right).mul(speed));
		}

		// 🔫 Shooting
		if (GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
			gun.shoot(camera.position, camera.getForward(), enemies);
		}

		// 🔄 Reload (press R)
		if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_R) == GLFW.GLFW_PRESS) {
			gun.reload();
		}

		// Update gun timers
		gun.update();
	}
}