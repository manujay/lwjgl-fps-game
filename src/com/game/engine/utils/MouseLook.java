package com.game.engine.utils;

import org.lwjgl.glfw.GLFW;

import com.game.engine.graphics.Camera;

public class MouseLook {

	private double lastX, lastY;
	private boolean first = true;
	private float sensitivity = 0.0022f; // tweak to taste
	private float smoothFactor = 0.2f; // 0–1 (lower = tighter)
	// Camera fields
	private float recoilPitch = 0f;
	private float recoilYaw = 0f;

	private float smoothedDX = 0, smoothedDY = 0;

	// On shoot:
	public void addRecoil() {
		recoilPitch -= 0.04f; // up kick
		recoilYaw += (Math.random() - 0.5f) * 0.02f; // slight horizontal
	}

	private void applyLook(Camera cam, float dx, float dy, float dt) {

		smoothedDX += (dx - smoothedDX) * smoothFactor;
		smoothedDY += (dy - smoothedDY) * smoothFactor;

		// Scale by dt so feel stays consistent if FPS changes
		float scale = sensitivity * (dt * 1000f); // ms-like scaling

		cam.yaw += dx * scale;
		cam.pitch -= dy * scale;

		// Clamp pitch (no flips)
		cam.pitch = Math.max(-1.5f, Math.min(1.5f, cam.pitch));
	}

	public void updateMouse(long window, Camera camera, float deltaTime) {
		double[] x = new double[1];
		double[] y = new double[1];
		GLFW.glfwGetCursorPos(window, x, y);

		if (first) {
			lastX = x[0];
			lastY = y[0];
			first = false;
		}

		double dx = x[0] - lastX;
		double dy = y[0] - lastY;

		lastX = x[0];
		lastY = y[0];

		applyLook(camera, (float) dx, (float) dy, deltaTime);
	}

	// In update:
	public void updateRecoil(Camera cam, float dt) {
		float recovery = 10f;

		recoilPitch += (0 - recoilPitch) * recovery * dt;
		recoilYaw += (0 - recoilYaw) * recovery * dt;

		cam.pitch += recoilPitch;
		cam.yaw += recoilYaw;
	}

}
