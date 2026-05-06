package com.game.engine.graphics.texture;

import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

public class Texture {

	private int id;

	public Texture(String path) {

		try (var stack = stackPush()) {

			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);

			ByteBuffer image = STBImage.stbi_load(path, w, h, channels, 4);
			if (image == null) {
				throw new RuntimeException("Failed to load texture: " + path);
			}

			id = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w.get(), h.get(), 0, GL11.GL_RGBA,
					GL11.GL_UNSIGNED_BYTE, image);

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

			STBImage.stbi_image_free(image);
		}
	}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
}