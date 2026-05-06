package com.game.engine.shader;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public class ShaderProgram {

	public static String load(String path) {
		try {
			return new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load shader file " + path, e);
		}
	}

	private final int programId;
	private int vertexId;

	private int fragmentId;

	private Map<String, Integer> uniforms = new HashMap<>();

	public ShaderProgram() {
		programId = glCreateProgram();
		if (programId == 0) {
			throw new RuntimeException("could not create shader program");
		}
	}

	public void bind() {
		glUseProgram(programId);
	}

	// -------------------------
	// Cleanup
	// -------------------------
	public void cleanup() {
		unbind();
		if (programId != 0) {
			glDeleteProgram(programId);
		}
	}

	public void createFragmentShader(String code) {
		fragmentId = createShader(code, GL_FRAGMENT_SHADER);
	}

	private int createShader(String code, int type) {
		int shaderId = glCreateShader(type);
		if (shaderId == 0) {
			throw new RuntimeException("Error creating shader. Type " + type);
		}

		glShaderSource(shaderId, code);
		glCompileShader(shaderId);

		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
			throw new RuntimeException("Error compiling shader code " + glGetShaderInfoLog(shaderId, 1024));
		}

		glAttachShader(programId, shaderId);

		return shaderId;
	}

	/**
	 * Uniforms
	 */

	public void createUniform(String name) {
		int location = glGetUniformLocation(programId, name);
		if (location < 0) {
			throw new RuntimeException("Uniform not found : " + name);
		}

		uniforms.put(name, location);
	}

	public void createVertexShader(String code) {
		vertexId = createShader(code, GL_VERTEX_SHADER);
	}

	/**
	 * Link
	 */
	public void link() {
		glLinkProgram(programId);

		if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
			throw new RuntimeException("Error linking shader: " + glGetProgramInfoLog(programId, 1024));
		}

		if (vertexId != 0) {
			glDetachShader(programId, vertexId);
		}
		if (fragmentId != 0) {
			glDetachShader(programId, fragmentId);
		}

		glValidateProgram(programId);
	}

	public void setUniform(String name, float value) {
		glUniform1f(uniforms.get(name), value);
	}

	public void setUniform(String name, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			glUniformMatrix4fv(uniforms.get(name), false, fb);
		}
	}

	public void setUniform(String name, Vector3f value) {
		glUniform3f(uniforms.get(name), value.x, value.y, value.z);
	}

	public void setUniform(String name, Vector4f value) {
		glUniform4f(uniforms.get(name), value.w, value.x, value.y, value.z);
	}

	public void unbind() {
		glUseProgram(0);
	}

}
