#version 320 es

precision mediump float;

layout (location = 0) out vec4 out_FragColor;

layout (location = 0) in vec3 g_worldPosition;
layout (location = 1) in vec4 g_color;
layout (location = 2) in vec3 g_transformedNormal;

const vec3 kLightDir = vec3(0.0, -0.9889, 0.1483);
const vec3 kLightPos = vec3(1.5, 9.889, 1.483);

const vec3 kMaterialAmbient = vec3(0.43f, 0.81f, 0.91f);
const vec3 kMaterialSpecular = vec3(1.0, 1.0, 1.0);
const float kMaterialShininess = 64.0f;
const vec3 kLightAmbient = vec3(0.1, 0.1, 0.1);
const vec3 kLightL = vec3(0.0, 0.8f, 0.8f);

vec3 blinnPhong(vec3 position, vec3 n) {
    vec3 ambient = kLightAmbient * kMaterialAmbient;
    vec3 s = normalize(kLightPos - position);
    float sDotN = max(dot(s, n), 0.0);
    vec3 diffuse = g_color.rgb * sDotN;
    vec3 spec = vec3(0.0);
    if (sDotN > 0.0) {
        vec3 v = normalize(-position.xyz);
        vec3 h = normalize(v + s);
        spec = kMaterialSpecular * pow(max(dot(h, n), 0.0), kMaterialShininess);
    }

    return ambient + kLightL * (diffuse + spec);
}

void main() {

    vec3 n = normalize(g_transformedNormal);
    vec3 position = g_worldPosition;
    vec3 color = blinnPhong(position, n);
    out_FragColor = vec4(color, 1.0);
}
