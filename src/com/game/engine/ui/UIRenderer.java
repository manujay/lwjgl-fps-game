package com.game.engine.ui;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.game.engine.renderer.AbstractRenderer;
import com.game.engine.shader.UIShader;

public class UIRenderer extends AbstractRenderer {

	private UIShader shader;
	private UIMesh quad;

	private Matrix4f projection = new Matrix4f();

	private int width, height;
	private int health, ammo;

	@Override
	protected void begin() {
		disableDepth();
		enableAlphaBlend();
	}

	private void drawRect(float x, float y, float w, float h, Vector4f color) {
//		shader.setUniform("color", color);
		shader.setColor(color);

		Matrix4f model = new Matrix4f().translate(x, y, 0).scale(w, h, 1);

//		shader.setUniform("projection", new Matrix4f(projection).mul(model));
		shader.setProjection(new Matrix4f(projection).mul(model));
		quad.render();
	}

	@Override
	protected void end() {
		enableDepth();
		disableBlend();
	}

	@Override
	protected void render() {

		shader.bind();
//		shader.setUniform("projection", projection);
		shader.setProjection(projection);

		// Health bar
		drawRect(20, height - 40, health * 2f, 20, new Vector4f(1, 0, 0, 1));

		// Ammo bar
		drawRect(width - 150, height - 40, ammo * 5f, 20, new Vector4f(1, 1, 0, 1));

		shader.unbind();
	}

	public void setQuad(UIMesh quad) {
		this.quad = quad;
	}

	public void setShader(UIShader shader) {
		this.shader = shader;
	}

	public void submit(int w, int h, int health, int ammo) {
		this.width = w;
		this.height = h;
		this.health = health;
		this.ammo = ammo;

		projection.setOrtho2D(0, w, h, 0);
	}
}