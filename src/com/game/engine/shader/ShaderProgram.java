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
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class ShaderProgram {

	public static String load(String path) {
		try {
			return new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load shader file " + path, e);
		}
	}

	protected final int programId;

	private final Map<String, Integer> uniforms = new HashMap<>();

	protected ShaderProgram(String vertexSrc, String fragmentSrc) {
		programId = glCreateProgram();
		if (programId == 0) {
			throw new RuntimeException("Could not create shader program");
		}

		int vs = compile(GL_VERTEX_SHADER, vertexSrc);
		int fs = compile(GL_FRAGMENT_SHADER, fragmentSrc);

		glAttachShader(programId, vs);
		glAttachShader(programId, fs);

		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
			throw new RuntimeException("Error linking shader: " + glGetProgramInfoLog(programId));
		}

		// shaders no longer needed after link
		glDetachShader(programId, vs);
		glDetachShader(programId, fs);
		glDeleteShader(vs);
		glDeleteShader(fs);

		// Let subclasses declare uniforms
		createUniforms();
	}

	// --- lifecycle ---
	public void bind() {
		glUseProgram(programId);
	}

	public void cleanup() {
		unbind();
		glDeleteProgram(programId);
	}

	private int compile(int type, String src) {
		int id = glCreateShader(type);
		glShaderSource(id, src);
		glCompileShader(id);

		if (glGetShaderi(id, GL_COMPILE_STATUS) == 0) {
			throw new RuntimeException("Shader compile error: " + glGetShaderInfoLog(id));
		}
		return id;
	}

	protected void createUniform(String name) {
		int loc = glGetUniformLocation(programId, name);

		// Fail-fast (dev). Change to warn if you prefer.
		if (loc < 0) {
			throw new RuntimeException("Uniform not found (or optimized out): " + name);
		}
		uniforms.put(name, loc);
	}

	// 🔥 Subclasses must register uniforms here
	protected abstract void createUniforms();

	private int getLoc(String name) {
		Integer loc = uniforms.get(name);
		if (loc == null) {
			throw new RuntimeException("Uniform not registered: " + name);
		}
		return loc;
	}

	protected void setUniform(String name, float v) {
		glUniform1f(getLoc(name), v);
	}

	// --- typed setters ---
	protected void setUniform(String name, int v) {
		glUniform1i(getLoc(name), v);
	}

	protected void setUniform(String name, org.joml.Matrix4f m) {
		try (var stack = org.lwjgl.system.MemoryStack.stackPush()) {
			glUniformMatrix4fv(getLoc(name), false, m.get(stack.mallocFloat(16)));
		}
	}

	protected void setUniform(String name, org.joml.Vector3f v) {
		glUniform3f(getLoc(name), v.x, v.y, v.z);
	}

	protected void setUniform(String name, org.joml.Vector4f v) {
		glUniform4f(getLoc(name), v.x, v.y, v.z, v.w);
	}

	public void unbind() {
		glUseProgram(0);
	}
}
