package com.game.engine.renderer;

import org.lwjgl.opengl.GL11;

public abstract class AbstractRenderer {

	protected void begin() {
	}

	protected void disableBlend() {
		GL11.glDisable(GL11.GL_BLEND);
	}

	protected void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	protected void disableDepth() {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
	}

	// 🔧 Common helpers

	protected void enableAdditiveBlend() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
	}

	protected void enableAlphaBlend() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	protected void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	protected void enableDepth() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
	}

	protected void end() {
	}

	protected abstract void render();

	public final void renderPass() {
		begin();
		render();
		end();
	}
}
