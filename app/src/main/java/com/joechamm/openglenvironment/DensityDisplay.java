package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES32;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class DensityDisplay {

    public static final String TAG = "jcglen:dendisp";

    private static final int POS_ELEM_COUNT = 2;
    private static final int UV_ELEM_COUNT = 2;
    private static final int ELEMS_PER_VERT = POS_ELEM_COUNT + UV_ELEM_COUNT;
    private static final int BYTES_PER_VERT = ELEMS_PER_VERT * Constants.BYTES_PER_FLOAT;
    private static final int POS_BYTE_OFFSET = 0;
    private static final int UV_BYTE_OFFSET = POS_ELEM_COUNT * Constants.BYTES_PER_FLOAT;

    // Vertex Buffer using pos and uv := { x, y, u, v }
    private static final float[] squareVertices = {
            /* pos */   0.0f, 0.0f, /* uv */ 0.0f, 0.0f,
            /* pos */  1.0f, 0.0f, /* uv */ 1.0f, 0.0f,
            /* pos */  1.0f, 1.0f, /* uv */ 1.0f, 1.0f,
            /* pos */  0.0f, 1.0f, /* uv */ 0.0f, 1.0f
    };

    // Index Buffer
    private static final short[] squareIndices = {
            0, 1, 2,
            0, 2, 3
    };

    private static final float[] densityColor = {
            1.0f, 1.0f, 0.0f, 1.0f
    };

    private final int[] mVAO = new int[ 1 ];
    private final int[] mVBO = new int[ 1 ];
    private final int[] mIBO = new int[ 1 ];
    private final int[] mTEX = new int[ 1 ];

    private final int mDenFieldRows;
    private final int mDenFieldCols;
    private final int mTexByteSize;

    private final DensityShader mShader;

    private final int mIndexCount;

    private final int mPositionLoc;
    private final int mUVLoc;
    private final int mColorLoc;
    private final int mDensityLoc;

    // where to bind our texture
    private final int mBindIdxTEX;

    public DensityDisplay (
            Context ctx,
            /* number of rows in density field grid */
            final int numRows,
            /* number of columns in density field grid */
            final int numCols
    ) {

        mDenFieldRows = numRows;
        mDenFieldCols = numCols;

        mTexByteSize = numRows * numCols;

        mIndexCount = squareIndices.length;

        mShader = new DensityShader ( ctx );

        mPositionLoc = mShader.getPositionLoc ();
        mUVLoc = mShader.getUVLoc ();
        mColorLoc = mShader.getColorLoc ();
        mDensityLoc = mShader.getDensityLoc ();

        mBindIdxTEX = 0;

        initTEX ();

        // there's no real work setting up the vbo
        mVBO[ 0 ] = BufferHelper.makeStaticDrawVBO ( squareVertices );
        mIBO[ 0 ] = BufferHelper.makeShortIBO ( squareIndices );

        initVAO ();
    }

    public void updateTEX () {
        final float dx = 1.0f / ( mDenFieldCols - 1 );
        final float dy = 1.0f / ( mDenFieldRows - 1 );

        byte[] densityField = new byte[ mTexByteSize ];

        int idx = 0;
        for ( int row = 0; row < mDenFieldRows; row++ ) {
            for ( int col = 0; col < mDenFieldCols; col++ ) {
                float x = (float) col * dx;
                float y = (float) row * dy;

                float densitySample = LEFuncs.density_at ( x, y );
                densityField[ idx++ ] = (byte) ( densitySample * 255.0f );
            }
        }

        ByteBuffer densityFieldBuffer = ByteBuffer.allocateDirect ( mTexByteSize );
        densityFieldBuffer.order ( ByteOrder.nativeOrder () );
        densityFieldBuffer.put ( densityField );
        densityFieldBuffer.position ( 0 );

        GLES32.glBindTexture ( GLES32.GL_TEXTURE_2D, mTEX[ 0 ] );

        GLES32.glTexSubImage2D ( GLES32.GL_TEXTURE_2D, 0, 0, 0,
                                 mDenFieldCols, mDenFieldRows,
                                 GLES32.GL_LUMINANCE, GLES32.GL_UNSIGNED_BYTE,
                                 densityFieldBuffer );

        GLES32.glBindTexture ( GLES32.GL_TEXTURE_2D, 0 );
    }

    public void render () {

        // bind our density texture
        GLES32.glActiveTexture ( GLES32.GL_TEXTURE0 + mBindIdxTEX );
        GLES32.glBindTexture ( GLES32.GL_TEXTURE_2D, mTEX[ 0 ] );

        // bind our shader
        mShader.useProgram ();

        // bind our vao
        GLES32.glBindVertexArray ( mVAO[ 0 ] );

        // set the density color
        GLES32.glUniform4fv (
                mColorLoc, 1, densityColor, 0 );

        // set the density tex uniform
        GLES32.glUniform1i ( mDensityLoc, mBindIdxTEX );
        // draw
        GLES32.glDrawElements ( GLES32.GL_TRIANGLES, mIndexCount, GLES32.GL_UNSIGNED_SHORT, 0 );

        // reset our state
        GLES32.glBindVertexArray ( 0 );
        GLES32.glUseProgram ( 0 );
        GLES32.glBindTexture ( GLES32.GL_TEXTURE_2D, 0 );
    }

    private void initVAO () {
        // create our vao
        GLES32.glGenVertexArrays ( 1, mVAO, 0 );
        // bind for setup
        GLES32.glBindVertexArray ( mVAO[ 0 ] );

        // bind our index buffer
        GLES32.glBindBuffer ( GLES32.GL_ELEMENT_ARRAY_BUFFER, mIBO[ 0 ] );
        // grab our data from the vbo we created
        GLES32.glBindBuffer ( GLES32.GL_ARRAY_BUFFER, mVBO[ 0 ] );
        // enable the position attribute and tell OpenGL how to interpret the data
        GLES32.glEnableVertexAttribArray ( mPositionLoc );
        GLES32.glVertexAttribPointer ( mPositionLoc,
                                       POS_ELEM_COUNT,
                                       GLES32.GL_FLOAT,
                                       false,
                                       BYTES_PER_VERT,
                                       POS_BYTE_OFFSET );

        GLES32.glEnableVertexAttribArray ( mUVLoc );
        GLES32.glVertexAttribPointer ( mUVLoc,
                                       UV_ELEM_COUNT,
                                       GLES32.GL_FLOAT,
                                       false,
                                       BYTES_PER_VERT,
                                       UV_BYTE_OFFSET );

        // unbind vao to store state
        GLES32.glBindVertexArray ( 0 );
    }

    private void initTEX () {
        final float dx = 1.0f / ( mDenFieldCols - 1 );
        final float dy = 1.0f / ( mDenFieldRows - 1 );

        byte[] densityData = new byte[ mTexByteSize ];

        // initialize our data to 0
        Arrays.fill ( densityData, (byte) 0 );

        ByteBuffer densityBuffer = ByteBuffer.allocateDirect ( mTexByteSize );
        densityBuffer.order ( ByteOrder.nativeOrder () );
        densityBuffer.put ( densityData );
        densityBuffer.position ( 0 );

        GLES32.glGenTextures ( 1, mTEX, 0 );

        GLES32.glBindTexture ( GLES32.GL_TEXTURE_2D, mTEX[ 0 ] );

        GLES32.glTexParameterf ( GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_LINEAR );
        GLES32.glTexParameterf ( GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_LINEAR );
        GLES32.glTexParameterf ( GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_S, GLES32.GL_CLAMP_TO_EDGE );
        GLES32.glTexParameterf ( GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_T, GLES32.GL_CLAMP_TO_EDGE );

        GLES32.glTexImage2D ( GLES32.GL_TEXTURE_2D, 0, GLES32.GL_LUMINANCE,
                              mDenFieldRows, mDenFieldCols, 0, GLES32.GL_LUMINANCE,
                              GLES32.GL_UNSIGNED_BYTE, densityBuffer );

        GLES32.glBindTexture ( GLES32.GL_TEXTURE_2D, 0 );
    }
}
