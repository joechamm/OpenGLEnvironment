#version 320 es

layout (location = 0) in vec3 a_position;

layout (std140, binding = 7) uniform PerFrameUniforms
{
   mat4 Proj;
   mat4 View;
   mat4 Model;
} perFrameUniforms;

void main() {
   mat4 projView = perFrameUniforms.Proj * perFrameUniforms.View;
   mat4 mvp = projView * perFrameUniforms.Model;
   gl_Position = mvp * vec4(a_position, 1.0);
}
