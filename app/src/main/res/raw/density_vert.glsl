#version 320 es

precision mediump float;

layout (location = 0) in vec4 a_position;
layout (location = 1) in vec2 a_uv;

layout (std140, binding = 7) uniform PerFrameUniforms
{
    mat4 Proj;
    mat4 View;
    mat4 Model;
} perFrameUniforms;

layout (location = 0) out vec2 v_uv;

void main() {
    mat4 mvMatrix = perFrameUniforms.View * perFrameUniforms.Model;
    mat4 mvpMatrix = perFrameUniforms.Proj * mvMatrix;
    v_uv = a_uv;
    gl_Position = mvpMatrix * a_position;
}
