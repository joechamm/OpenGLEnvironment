#version 320 es

precision mediump float;

layout (location = 0) in vec3 a_position;
layout (location = 1) in vec4 a_color;

layout (std140, binding = 7) uniform PerFrameUniforms
{
    mat4 Proj;
    mat4 View;
    mat4 Model;
} perFrameUniforms;

// just pass through
layout (location = 0) out vec4 v_color;

void main() {
    v_color = a_color;
    gl_Position = vec4(a_position, 1.0);
}
