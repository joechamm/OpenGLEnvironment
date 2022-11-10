package com.joechamm.openglenvironment;

import android.opengl.GLES32;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class PerFrameUniforms {

    private static final String TAG = "jcglenv:perfruni";

    private static final int MAT_ELEM_COUNT = 16;
    private static final int MAT_COUNT = 3;
    private static final int DATA_ELEM_COUNT = MAT_COUNT * MAT_ELEM_COUNT;
    private static final int BUFFER_SIZE = DATA_ELEM_COUNT * Constants.BYTES_PER_FLOAT;

    //public float[] mProjectionMatrix = new float[ 16 ];
    //public float[] mViewMatrix = new float[ 16 ];
    // TODO: move model matrix to mesh target
    //public float[] mModelMatrix = new float[ 16 ];

    private float[] mData = new float[ DATA_ELEM_COUNT ];

    private static final int PROJ_ELEM_OFFSET = 0;
    private static final int VIEW_ELEM_OFFSET = MAT_ELEM_COUNT;
    private static final int MODEL_ELEM_OFFSET = VIEW_ELEM_OFFSET + MAT_ELEM_COUNT;
    private static final int PROJ_BYTE_OFFSET = PROJ_ELEM_OFFSET * Constants.BYTES_PER_FLOAT;
    private static final int VIEW_BYTE_OFFSET = VIEW_ELEM_OFFSET * Constants.BYTES_PER_FLOAT;
    private static final int MODEL_BYTE_OFFSET = MODEL_ELEM_OFFSET * Constants.BYTES_PER_FLOAT;

    private boolean mProjectionIsDirty = true;
    private boolean mViewIsDirty = true;
    private boolean mModelIsDirty = true;

    private int mUBO;

    PerFrameUniforms () {
//        Matrix.setIdentityM (mProjectionMatrix,0);
//        Matrix.setIdentityM ( mViewMatrix,0 );
//        Matrix.setIdentityM ( mModelMatrix, 0 );
        Matrix.setIdentityM ( mData, PROJ_ELEM_OFFSET );
        Matrix.setIdentityM ( mData, VIEW_ELEM_OFFSET );
        Matrix.setIdentityM ( mData, MODEL_ELEM_OFFSET );
        mUBO = BufferHelper.makeDynamicDrawFloatUBO ( mData );

        // bind our ubo now for use in shaders
        GLES32.glBindBufferBase ( GLES32.GL_UNIFORM_BUFFER, Constants.BIND_IDX_UNIFORMS, mUBO );
    }

    public void setProjFrustum (
            float left, float right,
            float bottom, float top,
            float near, float far
    ) {
        Matrix.frustumM ( mData, PROJ_ELEM_OFFSET, left, right, bottom, top, near, far );
        mProjectionIsDirty = true;
    }

    public void setProjOrtho (
            float left, float right,
            float bottom, float top,
            float near, float far
    ) {
        Matrix.orthoM ( mData, PROJ_ELEM_OFFSET, left, right, bottom, top, near, far );
        mProjectionIsDirty = true;
    }

    public void setProjPerspective ( float fovy, float aspectRatio, float zNear, float zFar ) {
        Matrix.perspectiveM ( mData, PROJ_ELEM_OFFSET, fovy, aspectRatio, zNear, zFar );
        mProjectionIsDirty = true;
    }

    public void setProjIdentity () {
        Matrix.setIdentityM ( mData, PROJ_ELEM_OFFSET );
        mProjectionIsDirty = true;
    }

    public void setProjMatrix ( float[] projMatrix ) {
        System.arraycopy ( projMatrix, 0, mData, PROJ_ELEM_OFFSET, MAT_ELEM_COUNT );
        mProjectionIsDirty = true;
    }

    public void setViewLookAt (
            float eyeX, float eyeY, float eyeZ,
            float targetX, float targetY, float targetZ,
            float upX, float upY, float upZ
    ) {
        Matrix.setLookAtM ( mData, VIEW_ELEM_OFFSET, eyeX, eyeY, eyeZ,
                            targetX, targetY, targetZ,
                            upX, upY, upZ );
        mViewIsDirty = true;
    }

    public void setViewIdentity () {
        Matrix.setIdentityM ( mData, VIEW_ELEM_OFFSET );
        mViewIsDirty = true;
    }

    public void setViewMatrix ( float[] viewMatrix ) {
        System.arraycopy ( viewMatrix, 0, mData, VIEW_ELEM_OFFSET, MAT_ELEM_COUNT );
        mViewIsDirty = true;
    }

    public void setModelIdentity () {
        Matrix.setIdentityM ( mData, VIEW_ELEM_OFFSET );
        mModelIsDirty = true;
    }

    public void setModelMatrix ( float[] modelMatrix ) {
        System.arraycopy ( modelMatrix, 0, mData, MODEL_ELEM_OFFSET, MAT_ELEM_COUNT );
        mModelIsDirty = true;
    }

    public void leftMultiplyModelMatrix ( float[] modelMatrix ) {
        float[] scratch = new float[ 16 ];
        Matrix.multiplyMM ( scratch, 0, modelMatrix, 0, mData, MODEL_ELEM_OFFSET );
        System.arraycopy ( scratch, 0, mData, MODEL_ELEM_OFFSET, MAT_ELEM_COUNT );
        mModelIsDirty = true;
    }

    public void rightMultiplyModelMatrix ( float[] modelMatrix ) {
        float[] scratch = new float[ 16 ];
        Matrix.multiplyMM ( scratch, 0, mData, MODEL_ELEM_OFFSET, modelMatrix, 0 );
        System.arraycopy ( scratch, 0, mData, MODEL_ELEM_OFFSET, MAT_ELEM_COUNT );
        mModelIsDirty = true;
    }

    public void setRotateEulerModelMatrix ( float eulerX, float eulerY, float eulerZ ) {
        Matrix.setRotateEulerM ( mData, MODEL_ELEM_OFFSET, eulerX, eulerY, eulerZ );
        mModelIsDirty = true;
    }

    public void setRotateAngleAxisModelMatrix (
            float angle, float axisX,
            float axisY, float axisZ
    ) {
        Matrix.setRotateM ( mData, MODEL_ELEM_OFFSET, angle, axisX, axisY, axisZ );
        mModelIsDirty = true;
    }

    public void translateModelMatrix ( float dx, float dy, float dz ) {
        Matrix.translateM ( mData, MODEL_ELEM_OFFSET, dx, dy, dz );
        mModelIsDirty = true;
    }

    public void scaleModelMatrix ( float sx, float sy, float sz ) {
        Matrix.scaleM ( mData, MODEL_ELEM_OFFSET, sx, sy, sz );
        mModelIsDirty = true;
    }

    public void rotateModelMatrix ( float angle, float axisX, float axisY, float axisZ ) {
        Matrix.rotateM ( mData, MODEL_ELEM_OFFSET, angle, axisX, axisY, axisZ );
        mModelIsDirty = true;
    }

    public void updateBuffer () {
        // Update our uniform buffer with the matrices that have changed
        // 48 total elements in mData with proj [0,15], view [16,31], and model [32,47]

        int begin = MODEL_ELEM_OFFSET + MAT_ELEM_COUNT - 1;
        int end = 0;

        if ( mProjectionIsDirty ) {
            begin = 0;
            end = VIEW_ELEM_OFFSET - 1;
        }

        if ( mViewIsDirty ) {
            begin = Math.min ( begin, VIEW_ELEM_OFFSET );
            end = Math.max ( end, MODEL_ELEM_OFFSET - 1 );
        }

        if ( mModelIsDirty ) {
            begin = Math.min ( begin, MODEL_ELEM_OFFSET );
            end = Math.max ( end, MODEL_ELEM_OFFSET + MAT_ELEM_COUNT - 1 );
        }

        int len = end - begin + 1;
        // make sure len > 0
        if ( len < 1 ) {
            return;
        }

        int byteCount = len * Constants.BYTES_PER_FLOAT;
        int byteOffset = begin * Constants.BYTES_PER_FLOAT;

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect ( byteCount );
        byteBuffer.order ( ByteOrder.nativeOrder () );
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer ();
        floatBuffer.put ( mData, begin, len );
        floatBuffer.position ( 0 );

        GLES32.glBindBuffer ( GLES32.GL_UNIFORM_BUFFER, mUBO );
        GLES32.glBufferSubData ( GLES32.GL_UNIFORM_BUFFER, byteOffset, byteCount, floatBuffer );
        GLES32.glBindBuffer ( GLES32.GL_UNIFORM_BUFFER, 0 );

        mProjectionIsDirty = false;
        mViewIsDirty = false;
        mModelIsDirty = false;

        // upload our new data to our ubo

//       int begin, end;
//        if(mProjectionIsDirty &&
//        mViewIsDirty &&
//       mModelIsDirty) {  // All 3 matrices need to be updated, so [0,47]
//           begin = 0;
//           end  = MODEL_ELEM_OFFSET + MAT_ELEM_COUNT - 1;
//       } else if(mProjectionIsDirty &&
//        mViewIsDirty) {  // just first two need update, so [0,31]
//            begin = 0;
//            end = MODEL_ELEM_OFFSET - 1;
//        } else if(mProjectionIsDirty &&
//        mModelIsDirty) { // first and last need update, so we just include the middle too so [0,47]
//            begin = 0;
//            end = MODEL_ELEM_OFFSET + MAT_ELEM_COUNT - 1;
//        } else if (mProjectionIsDirty) { // just the first matrix, so [0,15]
//            begin = 0;
//            end = VIEW_ELEM_OFFSET - 1;
//        } else if(mViewIsDirty &&
//        mModelIsDirty) { // last two, so [16,47]
//            begin = VIEW_ELEM_OFFSET;
//            end = MODEL_ELEM_OFFSET + MAT_ELEM_COUNT - 1;
//        } else if(mViewIsDirty) { // just the second one, so [16,31]
//            begin = VIEW_ELEM_OFFSET;
//            end = MODEL_ELEM_OFFSET - 1;
//        } else if(mModelIsDirty) { // just the last one, [32,47]
//            begin = MODEL_ELEM_OFFSET;
//            end = MODEL_ELEM_OFFSET + MAT_ELEM_COUNT - 1;
//        }
    }

    public boolean needsUpdate () {
        return ( mProjectionIsDirty || mViewIsDirty || mModelIsDirty );
    }


}
