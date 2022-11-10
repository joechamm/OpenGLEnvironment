package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Square {
    private static final String TAG = "jcglenv:square";

    //   private final ShortBuffer mIndexBuffer;

    private final int[] mVBO = new int[ 1 ];
    private final int[] mIBO = new int[ 1 ];

    static final float[] squareVertices = {  // vertices tightly packed as {x,y,z,r,g,b,a}
            // top left
            - 0.5f, 0.5f, 0.0f, 0.87f, 0.02f, 0.08f, 0.85f,
            // bottom left
            - 0.5f, - 0.5f, 0.0f, 0.87f, 0.02f, 0.08f, 0.85f,
            // bottom right
            0.5f, - 0.5f, 0.0f, 0.87f, 0.02f, 0.08f, 0.85f,
            // top right
            0.5f, 0.5f, 0.0f, 0.87f, 0.02f, 0.08f, 0.85f
    };

    static private final short[] indices = {
            0, 1, 2, // top left, bottom left, bottom right
            0, 2, 3  // top left, bottom right, top right
    };

    // SHADER PROGRAM MEMBERS
    private final SquareShader mShader;

    private float[] mModelMatrix = new float[ 16 ];

    public Square ( Context context ) {
        // DEBUGGING
        Log.d ( TAG, "ctor called" );

        Matrix.setIdentityM ( mModelMatrix, 0 );

        JCGLDebugger.checkGLError ( "before square shader creation" );
        mShader = new SquareShader ( context );
        JCGLDebugger.checkGLError ( "after square shader creation, before vertex buffer creation" );

        // upload our vertices to the vbo
        final FloatBuffer squareVertexBuffer = ByteBuffer.allocateDirect ( squareVertices.length * Constants.BYTES_PER_FLOAT )
                                                         .order ( ByteOrder.nativeOrder () )
                                                         .asFloatBuffer ();

        squareVertexBuffer.put ( squareVertices ).position ( 0 );

        GLES32.glGenBuffers ( 1, mVBO, 0 );
        if ( mVBO[ 0 ] > 0 ) {
            JCGLDebugger.checkGLError ( "glGenBuffers" );
            GLES32.glBindBuffer ( GLES32.GL_ARRAY_BUFFER, mVBO[ 0 ] );
            JCGLDebugger.checkGLError ( "glBindBuffer" );
            GLES32.glBufferData ( GLES32.GL_ARRAY_BUFFER, squareVertexBuffer.capacity () * Constants.BYTES_PER_FLOAT,
                                  squareVertexBuffer, GLES32.GL_STATIC_DRAW );
            JCGLDebugger.checkGLError ( "glBufferData" );
            GLES32.glBindBuffer ( GLES32.GL_ARRAY_BUFFER, 0 );

        } else {
            JCGLDebugger.checkGLError ( "after vertex buffer glGenBuffers" );
        }

        JCGLDebugger.checkGLError ( "after vertex buffer creation, before index buffer creation" );

        // initialize byte buffer for the draw list
//        ByteBuffer ib = ByteBuffer.allocateDirect (
//                // (# of coordinate values * 2 bytes per short)
//                indices.length * Constants.BYTES_PER_SHORT );
//        ib.order ( ByteOrder.nativeOrder () );
//
//        mIndexBuffer = ib.asShortBuffer ();
//        mIndexBuffer.put ( indices );
//        mIndexBuffer.position ( 0 );

        final ShortBuffer squareIndexBuffer = ByteBuffer.allocateDirect ( indices.length * Constants.BYTES_PER_SHORT )
                                                        .order ( ByteOrder.nativeOrder () )
                                                        .asShortBuffer ();

        squareIndexBuffer.put ( indices ).position ( 0 );

        GLES32.glGenBuffers ( 1, mIBO, 0 );
        if ( mIBO[ 0 ] > 0 ) {
            JCGLDebugger.checkGLError ( "after glGenBuffers(1,mIBO,0), before glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, mIBO[0]" );
            GLES32.glBindBuffer ( GLES32.GL_ELEMENT_ARRAY_BUFFER, mIBO[ 0 ] );
            JCGLDebugger.checkGLError ( "after glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, mIBO[0], before glBufferData" );
            GLES32.glBufferData ( GLES32.GL_ELEMENT_ARRAY_BUFFER, squareIndexBuffer.capacity () * Constants.BYTES_PER_SHORT,
                                  squareIndexBuffer, GLES32.GL_STATIC_DRAW );
            JCGLDebugger.checkGLError ( "after glBufferData, before glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)" );
            GLES32.glBindBuffer ( GLES32.GL_ELEMENT_ARRAY_BUFFER, 0 );
        } else {
            JCGLDebugger.checkGLError ( "after index buffer glGenBuffers" );
        }

        JCGLDebugger.checkGLError ( "after index buffer creation" );

    }

    public void draw ( PerFrameUniforms perFrameUniforms ) {
        // set model matrix
        perFrameUniforms.setModelMatrix ( mModelMatrix );
        // update uniform buffer
        perFrameUniforms.updateBuffer ();

        // Add program to OpenGL ES environment
        //    GLES32.glUseProgram ( mProgram );
        // bind shader program
        JCGLDebugger.checkGLError ( "Square:draw before mShader.useProgram" );

        mShader.useProgram ();

        JCGLDebugger.checkGLError ( "Square:draw after mShader.useProgram, before glUniformMatrix4fv" );
        int posLoc = mShader.getaLoc_position ();
        int colLoc = mShader.getaLoc_color ();
        //    int mvpLoc = mShader.getuLoc_uMVP ();

        final int vertexByteStride = ( Constants.POSITION_ELEMENT_COUNT + Constants.COLOR_ELEMENT_COUNT ) * Constants.BYTES_PER_FLOAT;
        final int posByteOffset = 0;
        final int colByteOffset = Constants.POSITION_ELEMENT_COUNT * Constants.BYTES_PER_FLOAT;

        final int COUNT = indices.length;
        // set uniforms

        GLES32.glBindBuffer ( GLES32.GL_ARRAY_BUFFER, mVBO[ 0 ] );

        //    GLES32.glUniformMatrix4fv ( mvpLoc, 1, false, mvpMatrix, 0 );

        JCGLDebugger.checkGLError ( "Square:draw afer glUniformMatrix4fv, before glEnableVertexAttribArray" );
        GLES32.glEnableVertexAttribArray ( posLoc );
        JCGLDebugger.checkGLError ( "Square:draw afer glEnableVertexAttribArray(posLoc), before glVertexAttribPointer" );
        GLES32.glVertexAttribPointer ( posLoc, Constants.POSITION_ELEMENT_COUNT, GLES32.GL_FLOAT, false, vertexByteStride, posByteOffset );

        JCGLDebugger.checkGLError ( "Square:draw afer glVertexAttribPointer, before glEnableVertexAttribArray(colLoc)" );
        GLES32.glEnableVertexAttribArray ( colLoc );
        JCGLDebugger.checkGLError ( "Square:draw afer glEnableVertexAttribArray(colLoc), before glVertexAttribPointer" );
        GLES32.glVertexAttribPointer ( colLoc, Constants.COLOR_ELEMENT_COUNT, GLES32.GL_FLOAT, false, vertexByteStride, colByteOffset );

        JCGLDebugger.checkGLError ( "Square:draw afer glVertexAttribPointer, before glBindBuffer" );
        // make call to draw
        //    GLES32.glDrawElements ( GLES32.GL_TRIANGLES, vertexCount, GLES32.GL_SHORT, mIndexBuffer );
        //    GLES32.glDrawElements ( GLES32.GL_TRIANGLES, vertexCount, GLES32.GL_SHORT, ( (Buffer) mIndexBuffer ) );

        GLES32.glBindBuffer ( GLES32.GL_ELEMENT_ARRAY_BUFFER, mIBO[ 0 ] );

        JCGLDebugger.checkGLError ( "Square:draw afer glBindBuffer, before glDrawElements" );

        // *** glDrawElements TYPE MUST BE 'GL_UNSIGNED_BYTE' 'GL_UNSIGNED_SHORT' or 'GL_UNSIGNED_INT' -- NO SIGNED INTEGER TYPES!!! --- ***
        GLES32.glDrawElements ( GLES32.GL_TRIANGLES, COUNT, GLES32.GL_UNSIGNED_SHORT, 0 );

        JCGLDebugger.checkGLError ( "Square:draw afer glDrawElements, before glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)" );

        GLES32.glBindBuffer ( GLES32.GL_ELEMENT_ARRAY_BUFFER, 0 );
        GLES32.glBindBuffer ( GLES32.GL_ARRAY_BUFFER, 0 );

        JCGLDebugger.checkGLError (
                "Square:draw afer glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0), before glDisableVertexAttribArray(posLoc)" );
        // reset state
        GLES32.glDisableVertexAttribArray ( posLoc );
        JCGLDebugger.checkGLError ( "Square:draw afer glDisableVertexAttribArray(posLoc), before glDisableVertexAttribArray(colLoc)" );
        GLES32.glDisableVertexAttribArray ( colLoc );
        JCGLDebugger.checkGLError ( "Square:draw afer glDisableVertexAttribArray(colLoc), before glUseProgram(0)" );
        GLES32.glUseProgram ( 0 );
        JCGLDebugger.checkGLError ( "Square:draw afer glUseProgram(0)" );
    }

    public void rotate ( float angle, float axisX, float axisY, float axisZ ) {
        Matrix.rotateM ( mModelMatrix, 0, angle, axisX, axisY, axisZ );
    }
}
/*

public class Square {

    private static final String TAG = "jcglenv:square";

    // GEOMETRY MEMBERS
    private final VertexBuffer mVertexBuffer;
    private final IndexBuffer mIndexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 4;
    static final float[] squareCoords = {
            - 0.5f,   0.5f, 0.0f, 1.0f,   // top left
            - 0.5f, - 0.5f, 0.0f, 1.0f,   // bottom left
              0.5f, - 0.5f, 0.0f, 1.0f,  // bottom right
              0.5f,   0.5f, 0.0f, 1.0f }; // top right

    // square color
    static final float[] squareColor = {
            0.87f, 0.08f, 0.02f, 1.0f
    };

    private short[] drawOrder = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * Constants.BYTES_PER_FLOAT; // 4 bytes per vertex

    // SHADER PROGRAM MEMBERS
    private final SquareShader mShader;

    public Square ( Context context) {
        // DEBUGGING
        Log.d ( TAG, "Square ctor called" );

        mShader = new SquareShader ( context );
        mVertexBuffer = new VertexBuffer ( squareCoords );
        mIndexBuffer = new IndexBuffer ( drawOrder );
*/
/*
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

        int vsHandle = JCGLRenderer.loadShader ( GLES32.GL_VERTEX_SHADER, vsCode );
        int fsHandle = JCGLRenderer.loadShader ( GLES32.GL_FRAGMENT_SHADER, fsCode );

        // create empty OpenGL ES Program
        mProgram = GLES32.glCreateProgram ();

        // add the vertex shader to program
        GLES32.glAttachShader ( mProgram, vsHandle );
        // add the fragment shader to program
        GLES32.glAttachShader ( mProgram, fsHandle );

        // creates OpenGL ES program executables
        GLES32.glLinkProgram ( mProgram );*//*


    }

    public void draw (float[] mvpMatrix) {
        // Add program to OpenGL ES environment
    //    GLES32.glUseProgram ( mProgram );
        // bind shader program
        mShader.useProgram ();
        // set uniforms
        GLES32.glUniform4fv ( mShader.getuLoc_vColor (), 1, squareColor, 0 );
        GLES32.glUniformMatrix4fv ( mShader.getuLoc_uMVP (), 1, false, mvpMatrix, 0 );

        int ibo = mIndexBuffer.getHandle ();
        int vbo = mVertexBuffer.getHandle ();

        GLES32.glBindBuffer ( GLES32.GL_ELEMENT_ARRAY_BUFFER, ibo );
        GLES32.glBindBuffer ( GLES32.GL_ARRAY_BUFFER, vbo );
        GLES32.glEnableVertexAttribArray ( mShader.getaLoc_vPosition () );
        GLES32.glVertexAttribPointer ( mShader.getaLoc_vPosition (), COORDS_PER_VERTEX, GLES32.GL_FLOAT, false, vertexStride, 0 );

        // bind index buffer
 //       mIndexBuffer.bind ();
        // bind vertex buffer (just positions here)
 //       mVertexBuffer.bind ( mShader.getaLoc_vPosition (), COORDS_PER_VERTEX, vertexStride, vertexCount );

        // make call to draw
        GLES32.glDrawElements ( GLES32.GL_TRIANGLES, vertexCount, GLES32.GL_SHORT, 0 );

        // reset state
        GLES32.glDisableVertexAttribArray ( mShader.getaLoc_vPosition () );
        GLES32.glBindBuffer ( GLES32.GL_ARRAY_BUFFER, 0 );
        GLES32.glBindBuffer ( GLES32.GL_ELEMENT_ARRAY_BUFFER, 0 );
        GLES32.glUseProgram ( 0 );
    }
}
*/
