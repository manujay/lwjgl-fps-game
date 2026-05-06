package com.game.engine.particles;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import com.game.engine.graphics.mesh.Mesh;
import com.game.engine.graphics.texture.Texture;
import com.game.engine.shader.ShaderProgram;

public class ParticleRenderer {

	private Mesh quad;
	private ShaderProgram shader;
	private Texture texture;

	public ParticleRenderer(Mesh quad, ShaderProgram shader) {
		this.quad = quad;
		this.shader = shader;
	}

	public ParticleRenderer(Mesh quad, ShaderProgram shader, Texture texture) {
		this.quad = quad;
		this.shader = shader;
		this.texture = texture;
	}

	public void render(List<Particle> particles, Matrix4f projection, Matrix4f view) {

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
//
		GL11.glDepthMask(false);

		shader.bind();

		shader.setUniform("projection", projection);
		shader.setUniform("view", view);
		shader.setUniform("rows", 4);
		shader.setUniform("cols", 4);

		if (texture != null) {
			texture.bind();
		}

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
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void render(List<Particle> particles, Vector3f cameraPos) {

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE); // additive

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBegin(GL11.GL_QUADS);

		for (Particle p : particles) {

			if (!p.alive) {
				continue;
			}

			float s = p.size;

			GL11.glColor4f(p.r, p.g, p.b, p.a);

			// Simple quad (billboard approximation)
			GL11.glVertex3f(p.position.x - s, p.position.y + s, p.position.z);
			GL11.glVertex3f(p.position.x + s, p.position.y + s, p.position.z);
			GL11.glVertex3f(p.position.x + s, p.position.y - s, p.position.z);
			GL11.glVertex3f(p.position.x - s, p.position.y - s, p.position.z);
		}

		GL11.glEnd();

		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
