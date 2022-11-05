package com.joechamm.openglenvironment;

import android.content.Context;
import android.graphics.Shader;
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
    static float[] triangleCoords = { // in counterclockwise order:
            0.0f, 0.622008459f, 0.0f, // top
            - 0.5f, - 0.311004243f, 0.0f, // bottom left
            0.5f, - 0.311004243f, 0.0f  // bottom right
    };

    // set color with red, green, blue and alpha (opacity) values
    float[] color = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    private final TriangleShader mShader;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public Triangle ( Context context ) {
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

        mShader = new TriangleShader ( context );

    }

    public void draw ( float[] mvpMatrix ) {
        // Add program to OpenGL ES environment
        //       GLES20.glUseProgram ( mProgram );
        mShader.useProgram ();

        // Enable a handle to the triangle vertices
        //     GLES20.glEnableVertexAttribArray ( aLoc_vPosition );

        GLES20.glEnableVertexAttribArray ( mShader.getaLoc_vPosition () );
        // Prepare the triangle coordinate data

        GLES20.glVertexAttribPointer ( mShader.getaLoc_vPosition (), COORDS_PER_VERTEX,
                                       GLES20.GL_FLOAT, false,
                                       vertexStride, mVertexBuffer );

        // set color for drawing the triangle
        //       GLES20.glUniform4fv ( uLoc_vColor, 1, color, 0 );
        GLES20.glUniform4fv ( mShader.getuLoc_vColor (), 1, color, 0 );

        // Pass the projection and view transformation to the shader
        //       GLES20.glUniformMatrix4fv ( uLoc_uMVP, 1, false, mvpMatrix, 0 );
        GLES20.glUniformMatrix4fv ( mShader.getuLoc_uMVP (), 1, false, mvpMatrix, 0 );

        // Draw the triangle
        GLES20.glDrawArrays ( GLES20.GL_TRIANGLES, 0, vertexCount );

        // Disable vertex array
        //       GLES20.glDisableVertexAttribArray ( aLoc_vPosition );
        GLES20.glDisableVertexAttribArray ( mShader.getaLoc_vPosition () );

    }
}
