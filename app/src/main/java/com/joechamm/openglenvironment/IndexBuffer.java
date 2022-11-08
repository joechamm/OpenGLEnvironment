package com.joechamm.openglenvironment;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class IndexBuffer {

    private static final String TAG = "jcglenv:ibo";

    private final int mIBO;

    IndexBuffer ( short[] data ) {
        final int[] ibo = new int[ 1 ];
        // Generate OpenGL buffer handle
        GLES20.glGenBuffers ( 1, ibo, 0 );
        if ( 0 == ibo[ 0 ] ) {
            Log.e ( TAG, "failed to generate index buffer" );
        }
        // Allocate a short buffer for our data and point to the front of it
        ShortBuffer dataBuffer = ByteBuffer.allocateDirect ( data.length * Constants.BYTES_PER_SHORT )
                                           .order ( ByteOrder.nativeOrder () ).asShortBuffer ().put ( data );
        dataBuffer.position ( 0 );
        // bind our OpenGL buffer to the element array buffer for use and allocate GPU memory for it
        // TODO: handle cases other than GL_STATIC_DRAW for usage
        GLES20.glBindBuffer ( GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo[ 0 ] );
        GLES20.glBufferData ( GLES20.GL_ELEMENT_ARRAY_BUFFER, data.length * Constants.BYTES_PER_SHORT,
                              dataBuffer, GLES20.GL_STATIC_DRAW );
        // unbind our buffer from the GL_ELEMENT_ARRAY_BUFFER target to avoid any state being set on our buffer we don't want
        GLES20.glBindBuffer ( GLES20.GL_ELEMENT_ARRAY_BUFFER, 0 );

        mIBO = ibo[ 0 ];
    }

    public int getHandle () {
        return mIBO;
    }

    public void bind () {
        GLES20.glBindBuffer ( GLES20.GL_ELEMENT_ARRAY_BUFFER, mIBO );
    }

    // TODO: uploadData can't be used when glBufferData usage is GL_STATIC_*
  /*  public void uploadData(short[] data, int offset) {
        // bind our buffer to target GL_ELEMENT_ARRAY_BUFFER first
        GLES20.glBindBuffer ( GLES20.GL_ARRAY_BUFFER, mIBO );
        if(JCGLDebugger.DEBUGGING) {
            // TODO
        }
        // Allocate float buffer to prepare for upload
        ShortBuffer dataBuffer = ByteBuffer.allocateDirect ( data.length * Constants.BYTES_PER_SHORT )
                                           .order ( ByteOrder.nativeOrder () ).asShortBuffer ().put ( data );
        dataBuffer.position ( 0 );
        // upload our data, then reset state
        GLES20.glBufferSubData ( GLES20.GL_ELEMENT_ARRAY_BUFFER, offset, data.length * Constants.BYTES_PER_SHORT, dataBuffer );
        GLES20.glBindBuffer ( GLES20.GL_ELEMENT_ARRAY_BUFFER, 0 );
    }*/
}
