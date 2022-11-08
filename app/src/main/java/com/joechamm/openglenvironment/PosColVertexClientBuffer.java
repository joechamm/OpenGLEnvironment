package com.joechamm.openglenvironment;

class PosColVertexClientBuffer
        extends ClientFloatBuffer {
    private static final String TAG = "jcglenv:vtxclibuff";

    private final int mStrideBytes = ( Constants.POSITION_ELEMENT_COUNT + Constants.COLOR_ELEMENT_COUNT ) * Constants.BYTES_PER_FLOAT;
    private final int mPositionByteOffset = 0;
    private final int mColorByteOffset = Constants.POSITION_ELEMENT_COUNT * Constants.BYTES_PER_FLOAT;
    private final int mVertexCount;

    PosColVertexClientBuffer ( float[] data ) {
        super ( data, true );
        mVertexCount = data.length / ( Constants.POSITION_ELEMENT_COUNT + Constants.COLOR_ELEMENT_COUNT );
    }
}
//
//class VertexBuffer {
//
//    private static final String TAG = "jcglenv:vbo";
//
//    private final int mVBO;
//
//    VertexBuffer(float[] data) {
//        final int[] vbo = new int[1];
//        // Generate OpenGL buffer handle
//        GLES20.glGenBuffers ( 1, vbo, 0 );
//        if(0 == vbo[0]) {
//            Log.e ( TAG, "failed to generate vertex buffer" );
//        }
//        // Allocate a float buffer for our data and point to the front of it
//        FloatBuffer dataBuffer = ByteBuffer.allocateDirect ( data.length * Constants.BYTES_PER_FLOAT )
//                                           .order ( ByteOrder.nativeOrder () ).asFloatBuffer ().put ( data );
//        dataBuffer.position ( 0 );
//        // bind our OpenGL buffer to the array buffer for use and allocate GPU memory for it
//        // TODO: handle cases other than GL_STATIC_DRAW for usage
//        GLES20.glBindBuffer ( GLES20.GL_ARRAY_BUFFER, vbo[0] );
//        GLES20.glBufferData ( GLES20.GL_ARRAY_BUFFER, data.length * Constants.BYTES_PER_FLOAT,
//                              dataBuffer, GLES20.GL_STATIC_DRAW);
//        // unbind our buffer from the GL_ARRAY_BUFFER target to avoid any state being set on our buffer we don't want
//        GLES20.glBindBuffer ( GLES20.GL_ARRAY_BUFFER, 0 );
//
//        mVBO = vbo[0];
//    }
//
//    public int getHandle() {
//        return mVBO;
//    }
//
//    public void bind(int attribLoc, int components, int stride, int offset) {
//        GLES20.glBindBuffer ( GLES20.GL_ARRAY_BUFFER, mVBO );
//        GLES20.glEnableVertexAttribArray ( attribLoc );
//        GLES20.glVertexAttribPointer ( attribLoc, components, GLES20.GL_FLOAT, false, stride, offset );
//    }
//
//    // TODO: uploadData can't be used when glBufferData usage is GL_STATIC_*
//  /*  public void uploadData(float[] data, int offset) {
//        // bind our buffer to target GL_ARRAY_BUFFER first
//        GLES20.glBindBuffer ( GLES20.GL_ARRAY_BUFFER, mVBO );
//        if(JCGLDebugger.DEBUGGING) {
//            // TODO
//        }
//        // Allocate float buffer to prepare for upload
//        FloatBuffer dataBuffer = ByteBuffer.allocateDirect ( data.length * Constants.BYTES_PER_FLOAT )
//                                           .order ( ByteOrder.nativeOrder () ).asFloatBuffer ().put ( data );
//        dataBuffer.position ( 0 );
//        // upload our data, then reset state
//        GLES20.glBufferSubData ( GLES20.GL_ARRAY_BUFFER, offset, data.length * Constants.BYTES_PER_FLOAT, dataBuffer );
//        GLES20.glBindBuffer ( GLES20.GL_ARRAY_BUFFER, 0 );
//    }*/
//}
