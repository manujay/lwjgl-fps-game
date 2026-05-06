#version 330 core

in vec3 FragPos;
in vec3 Normal;

out vec4 FragColor;

struct DirLight {
    vec3 direction;
    vec3 color;
    float intensity;
};

struct Material {
    vec3 albedo;
    float ambient;
    float specular;
    float shininess;
};

uniform DirLight light;
uniform Material material;
uniform vec3 viewPos;

void main() {

    vec3 N = normalize(Normal);
    vec3 L = normalize(-light.direction);
    vec3 V = normalize(viewPos - FragPos);

    // Ambient
    vec3 ambient = material.ambient * light.color;

    // Diffuse
    float diff = max(dot(N, L), 0.0);
    vec3 diffuse = diff * light.color;

    // Specular (Blinn-Phong is more stable)
    vec3 H = normalize(L + V);
    float spec = pow(max(dot(N, H), 0.0), material.shininess);
    vec3 specular = material.specular * spec * light.color;

    vec3 color = (ambient + diffuse + specular) * material.albedo * light.intensity;

    FragColor = vec4(color, 1.0);
}