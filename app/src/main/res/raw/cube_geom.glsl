#version 320 es

precision mediump float;

layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

layout (location = 0) in vec4 v_color[];

layout (location = 0) out vec3 g_worldPosition;
layout (location = 1) out vec4 g_color;
layout (location = 2) out vec3 g_transformedNormal;

layout (std140, binding = 7) uniform PerFrameUniforms
{
    mat4 Proj;
    mat4 View;
    mat4 Model;
} perFrameUniforms;

void main() {

    mat4 mvMatrix = perFrameUniforms.View * perFrameUniforms.Model;
    mat3 normalMatrix = mat3(transpose(inverse(mvMatrix)));

    vec3 u = vec3(gl_in[2].gl_Position - gl_in[0].gl_Position);
    vec3 v = vec3(gl_in[1].gl_Position - gl_in[0].gl_Position);

    vec3 n = normalize(cross(u, v));

    for (int i = 0; i < 3; i++) {
        vec4 worldPosition = mvMatrix * gl_in[i].gl_Position;
        g_transformedNormal = normalMatrix * n;
        g_color = v_color[i];
        g_worldPosition = worldPosition.xyz;
        gl_Position = perFrameUniforms.Proj * worldPosition;
        EmitVertex();
    }

    EndPrimitive();
}
