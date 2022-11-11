#version 320 es

precision mediump float;

layout (location = 0) in vec4 a_position;

layout (std140, binding = 7) uniform PerFrameUniforms
{
    mat4 Proj;
    mat4 View;
    mat4 Model;
} perFrameUniforms;

void main() {
    mat4 mvMatrix = perFrameUniforms.View * perFrameUniforms.Model;
    mat4 mvpMatrix = perFrameUniforms.Proj * mvMatrix;
    gl_Position = mvpMatrix * a_position;
}
