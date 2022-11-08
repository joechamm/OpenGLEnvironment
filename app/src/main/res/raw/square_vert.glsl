#version 100

uniform mat4 uMVP;

attribute vec3 a_position;
attribute vec4 a_color;

varying vec4 v_color;

void main() {
    v_color = a_color;
    gl_Position = uMVP * vec4(a_position, 1.0);
}
