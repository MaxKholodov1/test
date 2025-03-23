#version 330 core
in vec3 FragPos;       // Позиция фрагмента в мировых координатах
in vec3 Normal;        // Нормаль фрагмента
in vec2 TexCoord;      // Текстурные координаты

out vec4 FragColor;    // Цвет фрагмента

uniform sampler2D texture_diffuse1; // Текстура
uniform vec3 lightPos;              // Позиция источника света
uniform vec3 lightColor;            // Цвет света
uniform vec3 viewPos;               // Позиция камеры

void main() {
    // Освещение (Phong)
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(lightPos - FragPos);
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);

    float diff = max(dot(norm, lightDir), 0.0);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);

    vec3 diffuse = diff * lightColor;
    vec3 specular = spec * lightColor;

    vec3 result = (diffuse + specular) * texture(texture_diffuse1, TexCoord).rgb;
    FragColor = vec4(result, 1.0);
}