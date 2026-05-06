#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;

out vec2 passUV;

uniform mat4 projection;
uniform mat4 view;

uniform vec3 particlePos;
uniform float size;

uniform int rows;
uniform int cols;
uniform int frame;

void main() {

    vec3 right = normalize(vec3(view[0][0], view[1][0], view[2][0]));
    vec3 up    = normalize(vec3(view[0][1], view[1][1], view[2][1]));

    vec3 worldPos = particlePos
        + right * position.x * size
        + up    * position.y * size;

    // ✅ FIXED atlas math
    float f = float(frame);
    float col = mod(f, float(cols));
    float row = floor(f / float(cols));

    vec2 atlasUV = vec2(
        (texCoord.x + col) / float(cols),
        (texCoord.y + row) / float(rows)
    );

    passUV = atlasUV;

    gl_Position = projection * view * vec4(worldPos, 1.0);
}