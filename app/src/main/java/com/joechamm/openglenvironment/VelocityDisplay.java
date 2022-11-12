package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES32;

import java.nio.FloatBuffer;

public class VelocityDisplay {

    private static final String TAG = "jcglenv:veldisp";

    private static final int POS_ELEM_COUNT = 2;
    private static final int ELEMS_PER_VERT = POS_ELEM_COUNT;
    private static final int VERTS_PER_LINE = 2;
    private static final int BYTES_PER_VERT = ELEMS_PER_VERT * Constants.BYTES_PER_FLOAT;

    private final int mVelFieldRows;
    private final int mVelFieldCols;

    private final VelocityShader mShader;

    private final int[] mVAO = new int[ 1 ];
    private final int[] mVBO = new int[ 1 ];

    private final int mVertexCount;
    private final int mVertexBufferSize;

    private final int mPositionLoc;
    private final int mColorLoc;

    public VelocityDisplay (
            Context ctx,
            /* number of rows in velocity field grid */
            final int numRows,
            /* number of columns in velocity field grid */
            final int numCols
    ) {

        mVelFieldRows = numRows;
        mVelFieldCols = numCols;

        final int NUM_GRID_POINTS = numRows * numCols;

        mVertexCount = NUM_GRID_POINTS * VERTS_PER_LINE;
        mVertexBufferSize = mVertexCount * BYTES_PER_VERT;

        mShader = new VelocityShader ( ctx );

        mPositionLoc = mShader.getPositionLoc ();
        mColorLoc = mShader.getColorLoc ();

        initVBO ();
        initVAO ();
    }

    public void updateVBO () {

        final float dispScale = Constants.VELOCITY_DISP_SCALE;

        final float dx = 1.0f / mVelFieldCols;
        final float dy = 1.0f / mVelFieldRows;

        final int FLOAT_COUNT = mVertexCount * ELEMS_PER_VERT;

        float[] velocityField = new float[ FLOAT_COUNT ];

        int idx = 0;

        for ( int row = 0; row < mVelFieldRows; row++ ) {
            for ( int col = 0; col < mVelFieldCols; col++ ) {
                // our grid point
                float x0 = (float) col * dx;
                float y0 = (float) row * dy;
                // grab the velocity vector here
                float vx = LEFuncs.VELOCITY_FIELD[ 0 ][ col + 1 ][ row + 1 ];
                float vy = LEFuncs.VELOCITY_FIELD[ 1 ][ col + 1 ][ row + 1 ];
                // our line's second point is offset by the scaled length of the velocity vector
                float x1 = x0 + vx * dx * dispScale;
                float y1 = y0 + vy * dy * dispScale;

                // store for update
                velocityField[ idx++ ] = x0;
                velocityField[ idx++ ] = y0;

                velocityField[ idx++ ] = x1;
                velocityField[ idx++ ] = y1;
            }
        }

        FloatBuffer velBuffer = BufferHelper.makeFloatBuffer ( velocityField );

        // update our vbo now
        GLES32.glBindBuffer ( GLES32.GL_ARRAY_BUFFER, mVBO[ 0 ] );
        GLES32.glBufferSubData ( GLES32.GL_ARRAY_BUFFER, 0, mVertexBufferSize, velBuffer );
        GLES32.glBindBuffer ( GLES32.GL_ARRAY_BUFFER, 0 );
    }

    public void render () {
        final float[] velocityColor = { 1.0f, 0.0f, 0.0f, 1.0f };

        // bind our shader
        mShader.useProgram ();

        // bind our vao
        GLES32.glBindVertexArray ( mVAO[ 0 ] );

        // set the velocity color
        GLES32.glUniform4fv ( mColorLoc,
                              1,
                              velocityColor,
                              0 );

        // draw
        GLES32.glDrawArrays ( GLES32.GL_LINES, 0, mVertexCount );

        // reset state
        GLES32.glBindVertexArray ( 0 );
        GLES32.glUseProgram ( 0 );
    }

    private void initVBO () {
        final int FLOAT_COUNT = mVertexCount * ELEMS_PER_VERT;
        final float dx = 1.0f / mVelFieldCols;
        final float dy = 1.0f / mVelFieldRows;

        float[] velocityField = new float[ FLOAT_COUNT ];

        int idx = 0;

        for ( int row = 0; row < mVelFieldRows; row++ ) {
            for ( int col = 0; col < mVelFieldCols; col++ ) {
                float x = (float) col * dx;
                float y = (float) row * dy;

                // set first point on the velocity vector
                velocityField[ idx++ ] = x;
                velocityField[ idx++ ] = y;

                // our initial velocity vector is (0,0) so place the second point on top of the first
                velocityField[ idx++ ] = x;
                velocityField[ idx++ ] = y;
            }
        }

        mVBO[ 0 ] = BufferHelper.makeDynamicDrawVBO ( velocityField );
    }

    private void initVAO () {
        // create our vao
        GLES32.glGenVertexArrays ( 1, mVAO, 0 );
        // bind for setup
        GLES32.glBindVertexArray ( mVAO[ 0 ] );
        // grab our data from the vbo we created
        GLES32.glBindBuffer ( GLES32.GL_ARRAY_BUFFER, mVBO[ 0 ] );
        // enable the position attribute and tell OpenGL how to interpret the data
        GLES32.glEnableVertexAttribArray ( mPositionLoc );
        GLES32.glVertexAttribPointer ( mPositionLoc,
                                       POS_ELEM_COUNT,
                                       GLES32.GL_FLOAT,
                                       false,
                                       BYTES_PER_VERT,
                                       0 );
        // unbind vao to store state
        GLES32.glBindVertexArray ( 0 );
    }
}
