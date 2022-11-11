#version 320 es

precision mediump float;

layout (location = 0) out vec4 out_FragColor;

layout (location = 0) in vec2 v_uv;

layout (binding = 0) uniform sampler2D u_density;
layout (location = 0) uniform vec4 u_color;

void main() {
    float d = texture(u_density, v_uv).r;
    vec3 color = u_color.rgb * d;
    out_FragColor = vec4(color, 1.0);
}
