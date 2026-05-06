package com.game.engine.particles;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL13;

import com.game.engine.graphics.mesh.Mesh;
import com.game.engine.graphics.mesh.MeshFactory;
import com.game.engine.graphics.texture.Texture;
import com.game.engine.shader.ShaderProgramOLD;

public class ParticleRendererOLD {

	private ShaderProgramOLD shader;
	private Texture texture;
	private Mesh quad;

	public void cleanup() {
		shader.cleanup();
	}

	public Mesh getQuad() {
		return quad;
	}

	public ShaderProgramOLD getShader() {
		return shader;
	}

	public Texture getTexture() {
		return texture;
	}

	public void init() {
		quad = MeshFactory.createQuad();

		texture = new Texture("assets/textures/particle.png");

		shader = new ShaderProgramOLD();
		shader.createVertexShader(ShaderProgramOLD.load("assets/particles/particle.vert"));
		shader.createFragmentShader(ShaderProgramOLD.load("assets/particles/particle.frag"));
		shader.link();

		// uniforms
		shader.createUniform("projection");
		shader.createUniform("view");
		shader.createUniform("particlePos");
		shader.createUniform("size");
		shader.createUniform("color");
		shader.createUniform("rows");
		shader.createUniform("cols");
		shader.createUniform("frame");
		shader.createUniform("tex");
	}

	public void render(List<Particle> particles, Matrix4f projection, Matrix4f view) {

		if (shader == null) {
			throw new IllegalStateException("Shader not set!");
		}

		// 🔥 Correct pipeline
//		GL11.glDisable(GL11.GL_CULL_FACE);
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE); // additive
//		GL11.glDepthMask(false);

		shader.bind();

		shader.setUniform("projection", projection);
		shader.setUniform("view", view);
		shader.setUniform("rows", 4);
		shader.setUniform("cols", 4);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		texture.bind();
		shader.setUniform("tex", 0);

		for (Particle p : particles) {

			if (!p.alive) {
				continue;
			}

			shader.setUniform("particlePos", p.position);
			shader.setUniform("size", p.size);
			shader.setUniform("color", new Vector4f(p.r, p.g, p.b, p.a));
			shader.setUniform("frame", p.frame);

			quad.render();
		}

		shader.unbind();

//		GL11.glDepthMask(true);
//		GL11.glDisable(GL11.GL_BLEND);
//		GL11.glEnable(GL11.GL_CULL_FACE);
	}

}