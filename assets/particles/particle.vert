#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;

out vec2 passUV;

uniform mat4 projection;
uniform mat4 view;

uniform vec3 particlePos;
uniform float size;

// atlas
uniform int rows;
uniform int cols;
uniform int frame;

void main() {

    vec3 right = normalize(vec3(view[0][0], view[1][0], view[2][0]));
    vec3 up    = normalize(vec3(view[0][1], view[1][1], view[2][1]));

    vec3 worldPos = particlePos
        + right * position.x * size
        + up    * position.y * size;

    // atlas UV calc
    float col = float(frame % cols);
    float row = float(frame / cols);

    vec2 atlasUV = vec2(
        (texCoord.x + col) / cols,
        (texCoord.y + row) / rows
    );

    passUV = atlasUV;

    gl_Position = projection * view * vec4(worldPos, 1.0);
}