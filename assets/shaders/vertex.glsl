#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;

out vec2 passTex;
out vec3 passNormal;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

void main() {
    passTex = texCoord;
    passNormal = normal;

    gl_Position = projection * view * model * vec4(position, 1.0);
}