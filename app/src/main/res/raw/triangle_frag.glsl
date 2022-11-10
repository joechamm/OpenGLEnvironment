#version 320 es

precision mediump float;
uniform vec4 u_color;

layout (location = 0) out vec4 out_FragColor;

void main() {
    out_FragColor = u_color;
}
