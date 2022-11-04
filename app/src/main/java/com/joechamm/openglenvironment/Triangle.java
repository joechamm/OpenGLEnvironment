package com.joechamm.openglenvironment;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {

    private static final String TAG = "jcglenv:triangle";

    // GEOMETRY MEMBERS
    private FloatBuffer mVertexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = { // in counterclockwise order:
            0.0f, 0.622008459f, 0.0f, // top
            - 0.5f, - 0.311004243f, 0.0f, // bottom left
            0.5f, - 0.311004243f, 0.0f  // bottom right
    };

    // set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    // SHADER PROGRAM MEMBERS
    private final String vsCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "void main() {" +
                    "   gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fsCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "   gl_FragColor = vColor;" +
                    "}";

    private final int mProgram;

    private int positionHandle;
    private int colorHandle;
    private int vPMatrixHandle; // used to access and set the view transformation

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public Triangle () {
        // DEBUGGING
        Log.d ( TAG, "Triangle ctor called" );

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect (
                // (number of coordinate values * 4 bytes per float
                triangleCoords.length * 4 );

        // use the device hardware's native byte order
        bb.order ( ByteOrder.nativeOrder () );

        // create a floating point buffer from the ByteBuffer
        mVertexBuffer = bb.asFloatBuffer ();
        // add the coordinates to the FloatBuffer
        mVertexBuffer.put ( triangleCoords );
        // set the buffer to read the first coordinate
        mVertexBuffer.position ( 0 );

        int vsHandle = JCGLRenderer.loadShader ( GLES20.GL_VERTEX_SHADER, vsCode );
        int fsHandle = JCGLRenderer.loadShader ( GLES20.GL_FRAGMENT_SHADER, fsCode );

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram ();

        // add the vertex shader to program
        GLES20.glAttachShader ( mProgram, vsHandle );
        // add the fragment shader to program
        GLES20.glAttachShader ( mProgram, fsHandle );

        // creates OpenGL ES program executables
        GLES20.glLinkProgram ( mProgram );

    }

    public void draw ( float[] mvpMatrix ) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram ( mProgram );

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation ( mProgram, "vPosition" );

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray ( positionHandle );

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer ( positionHandle, COORDS_PER_VERTEX,
                                       GLES20.GL_FLOAT, false,
                                       vertexStride, mVertexBuffer );

        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetUniformLocation ( mProgram, "vColor" );

        // set color for drawing the triangle
        GLES20.glUniform4fv ( colorHandle, 1, color, 0 );

        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation ( mProgram, "uMVPMatrix" );

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv ( vPMatrixHandle, 1, false, mvpMatrix, 0 );

        // Draw the triangle
        GLES20.glDrawArrays ( GLES20.GL_TRIANGLES, 0, vertexCount );

        // Disable vertex array
        GLES20.glDisableVertexAttribArray ( positionHandle );
    }
}
