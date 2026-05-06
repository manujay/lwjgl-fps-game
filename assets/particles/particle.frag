#version 330 core

in vec2 passUV;
out vec4 FragColor;

uniform sampler2D tex;
uniform vec4 color;

void main() {

    vec4 texColor = texture(tex, passUV);

    if (texColor.a < 0.1)
        discard;

    FragColor = texColor * color;
}