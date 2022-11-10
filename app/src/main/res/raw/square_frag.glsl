#version 320 es

precision mediump float;

layout (location = 0) in vec4 v_color;
layout (location = 0) out vec4 out_FragColor;

void main() {
    out_FragColor = v_color;
}
