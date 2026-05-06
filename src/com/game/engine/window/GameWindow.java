package com.game.engine.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.game.engine.graphics.Camera;

public class GameWindow {

	private long handle;
	private String title;
	private int height;
	private int width;
	private double lastX = 400;
	private double lastY = 300;
	private boolean firstMouse = true;
	private boolean invertX = true, invertY = false;
	private long lastTime = System.nanoTime();

	public GameWindow(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
	}

	public long getHandle() {
		return handle;
	}

	public void init() {
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("GLFW init failed");
		}

		GLFW.glfwInit();
		handle = GLFW.glfwCreateWindow(width, height, title, 0, 0);

		GLFW.glfwMakeContextCurrent(handle);
		GL.createCapabilities();

		GL11.glEnable(GL11.GL_DEPTH_TEST);

		/**
		 * Mouse look
		 */
		GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

		// If supported (Windows/Linux with proper drivers):
		if (GLFW.glfwRawMouseMotionSupported()) {
			GLFW.glfwSetInputMode(handle, GLFW.GLFW_RAW_MOUSE_MOTION, GLFW.GLFW_TRUE);
		}

		GLFW.glfwSwapInterval(1); // VSync ON (stable, slightly more latency)
	}

	public void setCursorCallback(long handle, Camera camera) {
		GLFW.glfwSetCursorPosCallback(handle, (win, xpos, ypos) -> {

			if (firstMouse) {
				lastX = xpos;
				lastY = ypos;
				firstMouse = false;
			}

			double dx = xpos - lastX;
			double dy = ypos - lastY;

			lastX = xpos;
			lastY = ypos;

			float sensitivity = 0.0015f;
			float smoothing = 0.5f;

			dx *= smoothing;
			dy *= smoothing;

			double yawInput = invertX ? -dx : dx;
			double pitchInput = invertY ? dy : -dy;

			camera.yaw += yawInput * sensitivity;
			camera.pitch -= pitchInput * sensitivity;

			// Clamp pitch (avoid flipping)
			camera.pitch = Math.max(-1.5f, Math.min(1.5f, camera.pitch));
		});
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(handle);
	}

	public void update() {
		if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_I) == GLFW.GLFW_PRESS) {
			invertY = !invertY;
		}
		GLFW.glfwSwapBuffers(handle);
		GLFW.glfwPollEvents();
	}

}