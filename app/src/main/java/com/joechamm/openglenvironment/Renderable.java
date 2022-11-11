package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.Matrix;
import android.util.Log;

import java.util.Arrays;

public class Renderable {

    private static final String TAG = "jcglenv:renderable";

    private final int[] mVAO = new int[ 1 ];
    private final int[] mVBO = new int[ 1 ];
    private final int[] mIBO = new int[ 1 ];

    private final int mVertexBufferSize;
    private final int mNumIndices;
    private int mNumVertices;

    // SHADER PROGRAM MEMBERS
    private ShaderProgram mShaderProgram;

    private Context mContext;

    private float[] mModelMatrix = new float[ 16 ];

    public Renderable (
            Context context,
            float[] vertexData,
            short[] indexData
    ) {
        // DEBUGGING
        Log.d ( TAG, "ctor called" );

        Matrix.setIdentityM ( mModelMatrix, 0 );

        mContext = context;

        mVertexBufferSize = vertexData.length * Constants.BYTES_PER_FLOAT;
        mNumIndices = indexData.length;

        // create vao
        GLES32.glGenVertexArrays ( 1, mVAO, 0 );

        // setup buffers
        initBuffers ( vertexData, indexData );
    }

    private void initBuffers ( float[] vertexData, short[] indexData ) {

        mVBO[ 0 ] = BufferHelper.makeStaticDrawVBO ( vertexData );
        if ( mNumIndices > 0 ) {
            mIBO[ 0 ] = BufferHelper.makeShortIBO ( indexData );
        }
    }

    protected void setShaderProgram ( ShaderProgram shaderProgram ) {
        mShaderProgram = shaderProgram;
    }

    protected void initVAO (
            int posIdx,
            int colIdx,
            int norIdx,
            int uvIdx
    ) {
        final int posElemCount = ( posIdx > - 1 ) ? Constants.POSITION_ELEMENT_COUNT : 0;
        final int colElemCount = ( colIdx > - 1 ) ? Constants.COLOR_ELEMENT_COUNT : 0;
        final int norElemCount = ( norIdx > - 1 ) ? Constants.NORMAL_ELEMENT_COUNT : 0;
        final int uvElemCount = ( uvIdx > - 1 ) ? Constants.UV_ELEMENT_COUNT : 0;

        final int vertexElemCount = posElemCount + colElemCount + norElemCount + uvElemCount;
        final int vertexByteStride = vertexElemCount * Constants.BYTES_PER_FLOAT;

        final int posByteOffset = 0;
        final int colByteOffset = posElemCount * Constants.BYTES_PER_FLOAT;
        final int norByteOffset = ( posElemCount + colElemCount ) * Constants.BYTES_PER_FLOAT;
        final int uvByteOffset = ( posElemCount + colElemCount + norElemCount ) * Constants.BYTES_PER_FLOAT;

        mNumVertices = mVertexBufferSize / vertexByteStride;

        // bind our vao
        GLES32.glBindVertexArray ( mVAO[ 0 ] );

        // if we have index buffer, bind it now
        if ( mNumIndices > 0 ) {
            GLES32.glBindBuffer ( GLES32.GL_ELEMENT_ARRAY_BUFFER, mIBO[ 0 ] );
        }

        // bind our vertex buffer
        GLES32.glBindBuffer ( GLES32.GL_ARRAY_BUFFER, mVBO[ 0 ] );

        // setup our vertex attribs
        if ( posIdx > - 1 ) {
            GLES32.glEnableVertexAttribArray ( posIdx );
            GLES32.glVertexAttribPointer ( posIdx, Constants.POSITION_ELEMENT_COUNT, GLES32.GL_FLOAT, false, vertexByteStride,
                                           posByteOffset );
        }

        if ( colIdx > - 1 ) {
            GLES32.glEnableVertexAttribArray ( colIdx );
            GLES32.glVertexAttribPointer ( colIdx, Constants.COLOR_ELEMENT_COUNT, GLES32.GL_FLOAT, false, vertexByteStride,
                                           colByteOffset );
        }

        if ( norIdx > - 1 ) {
            GLES32.glEnableVertexAttribArray ( norIdx );
            GLES32.glVertexAttribPointer ( norIdx, Constants.NORMAL_ELEMENT_COUNT, GLES32.GL_FLOAT, false, vertexByteStride,
                                           norByteOffset );
        }

        if ( uvIdx > - 1 ) {
            GLES32.glEnableVertexAttribArray ( uvIdx );
            GLES32.glVertexAttribPointer ( uvIdx, Constants.UV_ELEMENT_COUNT, GLES32.GL_FLOAT, false, vertexByteStride,
                                           uvByteOffset );
        }

        // unbind vao
        GLES32.glBindVertexArray ( 0 );
    }

    public int getNumIndices () {
        return mNumIndices;
    }

    public int getVertexBufferSize () {
        return mVertexBufferSize;
    }

    public void draw ( PerFrameUniforms perFrameUniforms ) {
        // set model matrix
        perFrameUniforms.setModelMatrix ( mModelMatrix );
        // update uniform buffer
        perFrameUniforms.updateBuffer ();

        // bind our vertex array
        GLES32.glBindVertexArray ( mVAO[ 0 ] );

        // bind shader program
        mShaderProgram.useProgram ();

        // draw triangles
        if ( mNumIndices > 0 ) {
            GLES32.glDrawElements ( GLES32.GL_TRIANGLES,
                                    mNumIndices,
                                    GLES32.GL_UNSIGNED_SHORT,
                                    0 );
        } else {
            GLES32.glDrawArrays ( GLES32.GL_TRIANGLES,
                                  0,
                                  mNumVertices );
        }

        // unbind our shader and vertex array
        GLES32.glUseProgram ( 0 );
        GLES32.glBindVertexArray ( 0 );
    }

    public void rotate ( float angle, float axisX, float axisY, float axisZ ) {
        Matrix.rotateM ( mModelMatrix, 0, angle, axisX, axisY, axisZ );
    }

    public void setRotate ( float angle, float axisX, float axisY, float axisZ ) {
        Matrix.setIdentityM ( mModelMatrix, 0 );
        rotate ( angle, axisX, axisY, axisZ );
    }

    public void translate ( float dx, float dy, float dz ) {
        Matrix.translateM ( mModelMatrix, 0, dx, dy, dz );
    }

    public void setTranslate ( float dx, float dy, float dz ) {
        Matrix.setIdentityM ( mModelMatrix, 0 );
        translate ( dx, dy, dz );
    }

    public void scale ( float sx, float sy, float sz ) {
        Matrix.scaleM ( mModelMatrix, 0, sx, sy, sz );
    }

    public void setScale ( float sx, float sy, float sz ) {
        Matrix.setIdentityM ( mModelMatrix, 0 );
        scale ( sx, sy, sz );
    }

    public void setModelMatrix ( float[] modelMatrix ) {
        mModelMatrix = Arrays.copyOf ( modelMatrix, 16 );
    }

    public void setModelMatrix (
            float rotAngle, float rotAxisX, float rotAxisY, float rotAxisZ,
            float scaleX, float scaleY, float scaleZ,
            float posX, float posY, float posZ
    ) {
        Matrix.setIdentityM ( mModelMatrix, 0 );
        Matrix.scaleM ( mModelMatrix, 0, scaleX, scaleY, scaleZ );
        Matrix.rotateM ( mModelMatrix, 0, rotAngle, rotAxisX, rotAxisY, rotAxisZ );
        Matrix.translateM ( mModelMatrix, 0, posX, posY, posZ );
    }
}
