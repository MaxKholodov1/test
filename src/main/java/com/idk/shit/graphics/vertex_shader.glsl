#version 330 core
layout(location = 0) in vec3 aPos;       // Позиция вершины
layout(location = 1) in vec3 aNormal;    // Нормаль вершины
layout(location = 2) in vec2 aTexCoord;  // Текстурные координаты

out vec3 FragPos;       // Позиция фрагмента в мировых координатах
out vec3 Normal;        // Нормаль фрагмента
out vec2 TexCoord;      // Текстурные координаты

uniform mat4 model;      // Матрица модели
uniform mat4 view;       // Матрица вида
uniform mat4 projection; // Матрица проекции

void main() {
    FragPos = vec3(model * vec4(aPos, 1.0));
    Normal = mat3(transpose(inverse(model))) * aNormal;
    TexCoord = aTexCoord;
    
    gl_Position = projection * view * model * vec4(aPos, 1.0);
}
