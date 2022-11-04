package com.joechamm.openglenvironment;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Square {

    private static final String TAG = "jcglenv:square";

    // GEOMETRY MEMBERS
    private FloatBuffer mVertexBuffer;
    private ShortBuffer mDrawListBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            - 0.5f, 0.5f, 0.0f,   // top left
            - 0.5f, - 0.5f, 0.0f,   // bottom left
            0.5f, - 0.5f, 0.0f,   // bottom right
            0.5f, 0.5f, 0.0f }; // top right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

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
    // TODO: use uniform naming scheme (e.g. uniform locations uLocName, attribute locations aLocName, etc...)
    private int mPositionHandle;
    private int mColorHandle;

    public Square () {
        // DEBUGGING
        Log.d ( TAG, "Square ctor called" );

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect (
                // (number of coordinate values * 4 bytes per float
                squareCoords.length * 4 );

        // use the device hardware's native byte order
        bb.order ( ByteOrder.nativeOrder () );

        // create a floating point buffer from the ByteBuffer
        mVertexBuffer = bb.asFloatBuffer ();
        // add the coordinates to the FloatBuffer
        mVertexBuffer.put ( squareCoords );
        // set the buffer to read the first coordinate
        mVertexBuffer.position ( 0 );

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect (
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2 );
        dlb.order ( ByteOrder.nativeOrder () );
        mDrawListBuffer = dlb.asShortBuffer ();
        mDrawListBuffer.put ( drawOrder );
        mDrawListBuffer.position ( 0 );

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

        // TODO
    }
}
