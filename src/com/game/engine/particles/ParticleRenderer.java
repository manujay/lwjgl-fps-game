package com.game.engine.particles;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL13;

import com.game.engine.graphics.mesh.Mesh;
import com.game.engine.graphics.texture.Texture;
import com.game.engine.renderer.AbstractRenderer;
import com.game.engine.shader.ParticleShader;

public class ParticleRenderer extends AbstractRenderer {

	private ParticleShader shader;
	private Texture texture;
	private Mesh quad;

	private List<Particle> particles;
	private Matrix4f projection, view;

	@Override
	protected void begin() {
		disableCulling();
		enableAdditiveBlend();
		disableDepth();
	}

	@Override
	protected void end() {
		enableDepth();
		disableBlend();
		enableCulling();
	}

	@Override
	protected void render() {

		shader.bind();

//		shader.setUniform("projection", projection);
//		shader.setUniform("view", view);
//		shader.setUniform("rows", 4);
//		shader.setUniform("cols", 4);

		shader.setCamera(projection, view);
		shader.setAtlas(4, 4);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		texture.bind();
//		shader.setUniform("tex", 0);
		shader.bindTexture(0);

		for (Particle p : particles) {
			if (!p.alive) {
				continue;
			}

//			shader.setUniform("particlePos", p.position);
//			shader.setUniform("size", p.size);
//			shader.setUniform("color", new Vector4f(p.r, p.g, p.b, p.a));
//			shader.setUniform("frame", p.frame);
			shader.setParticle(p.position, p.size, new Vector4f(p.r, p.g, p.b, p.a), p.frame);

			quad.render();
		}

		shader.unbind();
	}

	public void setQuad(Mesh quad) {
		this.quad = quad;
	}

	public void setShader(ParticleShader shader) {
		this.shader = shader;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public void submit(List<Particle> particles, Matrix4f proj, Matrix4f view) {
		this.particles = particles;
		this.projection = proj;
		this.view = view;
	}
}
