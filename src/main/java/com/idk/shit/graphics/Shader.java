package com.idk.shit.graphics;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL11;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class Shader {
    private int programID;

    public Shader(String vertexPath, String fragmentPath) {
        int vertexID = loadShader(vertexPath, GL20.GL_VERTEX_SHADER);
        int fragmentID = loadShader(fragmentPath, GL20.GL_FRAGMENT_SHADER);

        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexID);
        GL20.glAttachShader(programID, fragmentID);
        GL20.glLinkProgram(programID);

        // Проверка ошибок линковки
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            throw new RuntimeException("Ошибка линковки шейдеров: " + GL20.glGetProgramInfoLog(programID));
        }

        // Удаляем шейдеры после линковки
        GL20.glDeleteShader(vertexID);
        GL20.glDeleteShader(fragmentID);
    }

    private int loadShader(String path, int type) {
        String source = loadShaderSource(path);
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, source);
        GL20.glCompileShader(shaderID);

        // Проверка ошибок компиляции
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new RuntimeException("Ошибка компиляции шейдера: " + GL20.glGetShaderInfoLog(shaderID));
        }

        return shaderID;
    }

    private String loadShaderSource(String path) {
        try (InputStream in = getClass().getResourceAsStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить шейдер: " + path, e);
        }
    }

    public void use() {
        GL20.glUseProgram(programID);
    }

    public void setUniform(String name, float value) {
        int location = GL20.glGetUniformLocation(programID, name);
        GL20.glUniform1f(location, value);
    }

    public void setUniform(String name, int value) {
        int location = GL20.glGetUniformLocation(programID, name);
        GL20.glUniform1i(location, value);
    }

    public void setUniform(String name, float x, float y, float z) {
        int location = GL20.glGetUniformLocation(programID, name);
        GL20.glUniform3f(location, x, y, z);
    }

    public void setUniform(String name, float x, float y, float z, float w) {
        int location = GL20.glGetUniformLocation(programID, name);
        GL20.glUniform4f(location, x, y, z, w);
    }

    public void cleanup() {
        GL20.glDeleteProgram(programID);
    }
}