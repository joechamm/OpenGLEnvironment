#version 320 es

precision mediump float;

layout (location = 0) out vec4 out_FragColor;

layout (location = 0) uniform vec4 u_color;

void main() {
    out_FragColor = u_color;
}
