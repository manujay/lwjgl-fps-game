package com.game.engine.ui;

import org.lwjgl.opengl.GL11;

public class UIRenderer {

	private float crosshairSpread = 10f;
	private float targetSpread = 10f;

	public void renderCrosshair(int width, int height) {

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);

		// ✅ RESET COLOR
		GL11.glColor3f(1f, 1f, 1f);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, -1, 1);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		float cx = width / 2f;
		float cy = height / 2f;

		float size = 8f;
		float spread = crosshairSpread;

		GL11.glBegin(GL11.GL_LINES);

		// Top
		GL11.glVertex2f(cx, cy - spread);
		GL11.glVertex2f(cx, cy - spread - size);

		// Bottom
		GL11.glVertex2f(cx, cy + spread);
		GL11.glVertex2f(cx, cy + spread + size);

		// Left
		GL11.glVertex2f(cx - spread, cy);
		GL11.glVertex2f(cx - spread - size, cy);

		// Right
		GL11.glVertex2f(cx + spread, cy);
		GL11.glVertex2f(cx + spread + size, cy);

		GL11.glEnd();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void renderHUD(int width, int height, int health, int ammo, int reserve) {

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, -1, 1);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		// Health bar
		float healthWidth = health * 2f;

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(20, height - 40);
		GL11.glVertex2f(20 + healthWidth, height - 40);
		GL11.glVertex2f(20 + healthWidth, height - 20);
		GL11.glVertex2f(20, height - 20);
		GL11.glEnd();

		// Ammo bar
		float ammoWidth = ammo * 5f;

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(width - 150, height - 40);
		GL11.glVertex2f(width - 150 + ammoWidth, height - 40);
		GL11.glVertex2f(width - 150 + ammoWidth, height - 20);
		GL11.glVertex2f(width - 150, height - 20);
		GL11.glEnd();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void triggerCrosshairExpand() {
		targetSpread = 25f + (float) (Math.random() * 5f); // expand on shooting
	}

	public void updateCrosshair(float deltaTime) {
		// Smooth interpolation back to normal
		float speed = 10f;
		crosshairSpread += (targetSpread - crosshairSpread) * speed * deltaTime;

		// Slowly return to default
		targetSpread = 10f;
	}
}