package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class Square {
    private static final String TAG = "jcglenv:square";

    private final PosColVertexClientBuffer mVertexBuffer;
    //   private final ShortBuffer mIndexBuffer;

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

    private final int[] indices = {
            0, 1, 2, // top left, bottom left, bottom right
            0, 2, 3  // top left, bottom right, top right
    };

    // SHADER PROGRAM MEMBERS
    private final SquareShader mShader;

    public Square ( Context context ) {
        // DEBUGGING
        Log.d ( TAG, "ctor called" );

        JCGLDebugger.checkGLError ( "before square shader creation" );
        mShader = new SquareShader ( context );
        JCGLDebugger.checkGLError ( "after square shader creation, before vertex buffer creation" );
        mVertexBuffer = new PosColVertexClientBuffer ( squareVertices );
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

        final IntBuffer squareIndexBuffer = ByteBuffer.allocateDirect ( indices.length * Constants.BYTES_PER_INT )
                                                      .order ( ByteOrder.nativeOrder () )
                                                      .asIntBuffer ();

        squareIndexBuffer.put ( indices ).position ( 0 );

        GLES20.glGenBuffers ( 1, mIBO, 0 );
        if ( mIBO[ 0 ] > 0 ) {
            JCGLDebugger.checkGLError ( "after glGenBuffers(1,mIBO,0), before glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mIBO[0]" );
            GLES20.glBindBuffer ( GLES20.GL_ELEMENT_ARRAY_BUFFER, mIBO[ 0 ] );
            JCGLDebugger.checkGLError ( "after glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mIBO[0], before glBufferData" );
            GLES20.glBufferData ( GLES20.GL_ELEMENT_ARRAY_BUFFER, squareIndexBuffer.capacity () * Constants.BYTES_PER_INT,
                                  squareIndexBuffer, GLES20.GL_STATIC_DRAW );
            JCGLDebugger.checkGLError ( "after glBufferData, before glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)" );
            GLES20.glBindBuffer ( GLES20.GL_ELEMENT_ARRAY_BUFFER, 0 );
        } else {
            JCGLDebugger.checkGLError ( "after index buffer glGenBuffers" );
        }

        JCGLDebugger.checkGLError ( "after index buffer creation" );

    }

    public void draw ( float[] mvpMatrix ) {
        // Add program to OpenGL ES environment
        //    GLES20.glUseProgram ( mProgram );
        // bind shader program
        JCGLDebugger.checkGLError ( "Square:draw before mShader.useProgram" );

        mShader.useProgram ();

        JCGLDebugger.checkGLError ( "Square:draw after mShader.useProgram, before glUniformMatrix4fv" );
        int posLoc = mShader.getaLoc_position ();
        int colLoc = mShader.getaLoc_color ();
        int mvpLoc = mShader.getuLoc_uMVP ();

        final int vertexByteStride = ( Constants.POSITION_ELEMENT_COUNT + Constants.COLOR_ELEMENT_COUNT ) * Constants.BYTES_PER_FLOAT;
        final int posByteOffset = 0;
        final int colByteOffset = Constants.POSITION_ELEMENT_COUNT * Constants.BYTES_PER_FLOAT;

        final int COUNT = indices.length;
        // set uniforms

        GLES20.glUniformMatrix4fv ( mvpLoc, 1, false, mvpMatrix, 0 );

        JCGLDebugger.checkGLError ( "Square:draw afer glUniformMatrix4fv, before glEnableVertexAttribArray" );
        GLES20.glEnableVertexAttribArray ( posLoc );
        JCGLDebugger.checkGLError ( "Square:draw afer glEnableVertexAttribArray(posLoc), before glVertexAttribPointer" );
        GLES20.glVertexAttribPointer ( posLoc, Constants.POSITION_ELEMENT_COUNT, GLES20.GL_FLOAT, false, vertexByteStride,
                                       mVertexBuffer.getFloatBuffer ().position ( posByteOffset ) );

        JCGLDebugger.checkGLError ( "Square:draw afer glVertexAttribPointer, before glEnableVertexAttribArray(colLoc)" );
        GLES20.glEnableVertexAttribArray ( colLoc );
        JCGLDebugger.checkGLError ( "Square:draw afer glEnableVertexAttribArray(colLoc), before glVertexAttribPointer" );
        GLES20.glVertexAttribPointer ( colLoc, Constants.COLOR_ELEMENT_COUNT, GLES20.GL_FLOAT, false, vertexByteStride,
                                       mVertexBuffer.getFloatBuffer ().position ( colByteOffset ) );

        JCGLDebugger.checkGLError ( "Square:draw afer glVertexAttribPointer, before glBindBuffer" );
        // make call to draw
        //    GLES20.glDrawElements ( GLES20.GL_TRIANGLES, vertexCount, GLES20.GL_SHORT, mIndexBuffer );
        //    GLES20.glDrawElements ( GLES20.GL_TRIANGLES, vertexCount, GLES20.GL_SHORT, ( (Buffer) mIndexBuffer ) );

        GLES20.glBindBuffer ( GLES20.GL_ELEMENT_ARRAY_BUFFER, mIBO[ 0 ] );

        JCGLDebugger.checkGLError ( "Square:draw afer glBindBuffer, before glDrawElements" );

        GLES20.glDrawElements ( GLES20.GL_TRIANGLES, COUNT, GLES20.GL_INT, 0 );

        JCGLDebugger.checkGLError ( "Square:draw afer glDrawElements, before glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)" );

        GLES20.glBindBuffer ( GLES20.GL_ELEMENT_ARRAY_BUFFER, 0 );

        JCGLDebugger.checkGLError (
                "Square:draw afer glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0), before glDisableVertexAttribArray(posLoc)" );
        // reset state
        GLES20.glDisableVertexAttribArray ( posLoc );
        JCGLDebugger.checkGLError ( "Square:draw afer glDisableVertexAttribArray(posLoc), before glDisableVertexAttribArray(colLoc)" );
        GLES20.glDisableVertexAttribArray ( colLoc );
        JCGLDebugger.checkGLError ( "Square:draw afer glDisableVertexAttribArray(colLoc), before glUseProgram(0)" );
        GLES20.glUseProgram ( 0 );
        JCGLDebugger.checkGLError ( "Square:draw afer glUseProgram(0)" );
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

        int vsHandle = JCGLRenderer.loadShader ( GLES20.GL_VERTEX_SHADER, vsCode );
        int fsHandle = JCGLRenderer.loadShader ( GLES20.GL_FRAGMENT_SHADER, fsCode );

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram ();

        // add the vertex shader to program
        GLES20.glAttachShader ( mProgram, vsHandle );
        // add the fragment shader to program
        GLES20.glAttachShader ( mProgram, fsHandle );

        // creates OpenGL ES program executables
        GLES20.glLinkProgram ( mProgram );*//*


    }

    public void draw (float[] mvpMatrix) {
        // Add program to OpenGL ES environment
    //    GLES20.glUseProgram ( mProgram );
        // bind shader program
        mShader.useProgram ();
        // set uniforms
        GLES20.glUniform4fv ( mShader.getuLoc_vColor (), 1, squareColor, 0 );
        GLES20.glUniformMatrix4fv ( mShader.getuLoc_uMVP (), 1, false, mvpMatrix, 0 );

        int ibo = mIndexBuffer.getHandle ();
        int vbo = mVertexBuffer.getHandle ();

        GLES20.glBindBuffer ( GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo );
        GLES20.glBindBuffer ( GLES20.GL_ARRAY_BUFFER, vbo );
        GLES20.glEnableVertexAttribArray ( mShader.getaLoc_vPosition () );
        GLES20.glVertexAttribPointer ( mShader.getaLoc_vPosition (), COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, 0 );

        // bind index buffer
 //       mIndexBuffer.bind ();
        // bind vertex buffer (just positions here)
 //       mVertexBuffer.bind ( mShader.getaLoc_vPosition (), COORDS_PER_VERTEX, vertexStride, vertexCount );

        // make call to draw
        GLES20.glDrawElements ( GLES20.GL_TRIANGLES, vertexCount, GLES20.GL_SHORT, 0 );

        // reset state
        GLES20.glDisableVertexAttribArray ( mShader.getaLoc_vPosition () );
        GLES20.glBindBuffer ( GLES20.GL_ARRAY_BUFFER, 0 );
        GLES20.glBindBuffer ( GLES20.GL_ELEMENT_ARRAY_BUFFER, 0 );
        GLES20.glUseProgram ( 0 );
    }
}
*/
