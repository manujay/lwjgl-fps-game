#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;

out vec3 FragPos;
out vec3 Normal;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

void main() {
    vec4 worldPos = model * vec4(position, 1.0);
    FragPos = worldPos.xyz;

    // correct normal transform (handles non-uniform scale)
    Normal = mat3(transpose(inverse(model))) * normal;

    gl_Position = projection * view * worldPos;
}