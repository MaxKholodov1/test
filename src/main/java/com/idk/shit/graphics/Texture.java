package com.idk.shit.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.BufferUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
public class Texture {
    private final int textureID;

    public Texture(String path) {
        textureID = loadTexture(path);
    }

    public int loadTexture(String path) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        STBImage.stbi_set_flip_vertically_on_load(true);

        ByteBuffer image;
        URL imageUrl = Texture.class.getClassLoader().getResource(path);
//        if (imageUrl == null) {
//            System.err.println("Ресурс не найден: " + path);
//        } else {
//            System.out.println("Ресурс найден: " + imageUrl);
//        }
        if (imageUrl == null) {
            throw new RuntimeException("Не удалось найти текстуру: " + path);
        }

        try (InputStream inputStream = imageUrl.openStream()) {
            byte[] imageData = inputStream.readAllBytes();
//            System.out.println("Размер imageData: " + imageData.length);
            ByteBuffer imageBuffer = BufferUtils.createByteBuffer(imageData.length);
            imageBuffer.put(imageData);
            imageBuffer.flip();
//            System.out.println("Размер imageBuffer: " + imageBuffer.remaining());
            image = STBImage.stbi_load_from_memory(imageBuffer, width, height, channels, 4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (image == null) {
            throw new RuntimeException("Не удалось загрузить текстуру: " + path);
        }

        int id = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width.get(), height.get(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        STBImage.stbi_image_free(image);
        return id;
    }
    public void cleanup() {
        GL11.glDeleteTextures(textureID);
    }

    public void draw(float x, float y, float width, float height) {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);

        GL11.glColor4f(1, 1, 1, 1);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(x - width / 2, y - height / 2);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(x + width / 2, y - height / 2);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(x + width / 2, y + height / 2);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(x - width / 2, y + height / 2);
        }
        GL11.glEnd();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}
