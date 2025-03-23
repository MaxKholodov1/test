package com.idk.shit.utils;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

public class InputManager {
    private Set<Integer> pressedKeys = new HashSet<>();

    public void registerCallbacks(long window) {
        glfwSetKeyCallback(window, (w, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                pressedKeys.add(key);
            } else if (action == GLFW_RELEASE) {
                pressedKeys.remove(key);
            }
        });
    }

    public boolean isKeyPressed(int key) {
        return pressedKeys.contains(key);
    }

    public void cleanup() {
        pressedKeys.clear();
    }
}

