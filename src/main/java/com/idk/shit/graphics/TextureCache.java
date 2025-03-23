package com.idk.shit.graphics;

import java.util.HashMap;
import java.util.Map;

public class TextureCache {
    private static final Map<String, Texture> textureMap = new HashMap<>();

    public static Texture getTexture(String path) {
        // Если текстура уже загружена, возвращаем её из кэша
        if (textureMap.containsKey(path)) {
            return textureMap.get(path);
        }

        // Если текстура не загружена, загружаем её и добавляем в кэш
        Texture texture = new Texture(path);
        textureMap.put(path, texture);
        return texture;
    }

    public static void cleanup() {
        // Освобождаем все текстуры
        for (Texture texture : textureMap.values()) {
            texture.cleanup();
        }
        textureMap.clear();
    }
}