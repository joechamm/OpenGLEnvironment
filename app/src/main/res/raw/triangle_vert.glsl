#version 100

// This matrix member variable provides a hook to manipulate
// the coordinates of the objects that use this vertex shader
uniform mat4 uMVP;
attribute vec4 vPosition;

// the matrix must be included as a modifier of gl_Position
// Note that the uMVPMatrix factor *must be first* in order
// for the matrix multiplication product to be correct.
void main() {
   gl_Position = uMVP * vPosition;
}
