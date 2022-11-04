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
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "   gl_Position = vPosition;" +
                    "}";

    private final String fsCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "   gl_FragColor = vColor;" +
                    "}";

    private final int mProgram;

    private int mPositionHandle;
    private int mColorHandle;

    private final int mVertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int mVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

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

    public void draw () {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram ( mProgram );

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation ( mProgram, "vPosition" );

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray ( mPositionHandle );

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer ( mPositionHandle, COORDS_PER_VERTEX,
                                       GLES20.GL_FLOAT, false,
                                       mVertexStride, mVertexBuffer );

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation ( mProgram, "vColor" );

        // set color for drawing the triangle
        GLES20.glUniform4fv ( mColorHandle, 1, color, 0 );

        // Draw the triangle
        GLES20.glDrawArrays ( GLES20.GL_TRIANGLES, 0, mVertexCount );

        // Disable vertex array
        GLES20.glDisableVertexAttribArray ( mPositionHandle );
    }
}
