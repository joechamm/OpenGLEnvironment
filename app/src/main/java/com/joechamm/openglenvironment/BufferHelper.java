package com.joechamm.openglenvironment;

import android.opengl.GLES32;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class BufferHelper {

    private static final String TAG = "jcglenv:bufhelp";

    public static FloatBuffer makeFloatBuffer ( float[] data ) {
        ByteBuffer bb = ByteBuffer.allocateDirect ( data.length * Constants.BYTES_PER_FLOAT );
        bb.order ( ByteOrder.nativeOrder () );
        FloatBuffer fb = bb.asFloatBuffer ();
        fb.put ( data );
        fb.position ( 0 );
        return fb;
    }

    public static IntBuffer makeIntBuffer ( int[] data ) {
        ByteBuffer bb = ByteBuffer.allocateDirect ( data.length * Constants.BYTES_PER_INT );
        bb.order ( ByteOrder.nativeOrder () );
        IntBuffer ib = bb.asIntBuffer ();
        ib.put ( data );
        ib.position ( 0 );
        return ib;
    }

    public static ShortBuffer makeShortBuffer ( short[] data ) {
        ByteBuffer bb = ByteBuffer.allocateDirect ( data.length * Constants.BYTES_PER_SHORT );
        bb.order ( ByteOrder.nativeOrder () );
        ShortBuffer sb = bb.asShortBuffer ();
        sb.put ( data );
        sb.position ( 0 );
        return sb;
    }

    private static int makeFloatArrayBuffer ( float[] data, int usage ) {
        int size = data.length * Constants.BYTES_PER_FLOAT;
        int[] buf = new int[ 1 ];
        FloatBuffer floatBuffer = makeFloatBuffer ( data );
        GLES32.glGenBuffers ( 1, buf, 0 );
        GLES32.glBindBuffer ( GLES32.GL_ARRAY_BUFFER, buf[ 0 ] );
        GLES32.glBufferData ( GLES32.GL_ARRAY_BUFFER, size, floatBuffer, usage );
        GLES32.glBindBuffer ( GLES32.GL_ARRAY_BUFFER, 0 );
        if ( JCGLDebugger.DEBUGGING ) {
            // check for errors if we are debugging
            JCGLDebugger.checkGLError ( TAG + " makeFloatVBO" );
        }
        return buf[ 0 ];
    }

    private static int makeShortIndexBuffer ( short[] data, int usage ) {
        int size = data.length * Constants.BYTES_PER_SHORT;
        int[] buf = new int[ 1 ];
        ShortBuffer shortBuffer = makeShortBuffer ( data );
        GLES32.glGenBuffers ( 1, buf, 0 );
        GLES32.glBindBuffer ( GLES32.GL_ELEMENT_ARRAY_BUFFER, buf[ 0 ] );
        GLES32.glBufferData ( GLES32.GL_ELEMENT_ARRAY_BUFFER, size, shortBuffer, usage );
        GLES32.glBindBuffer ( GLES32.GL_ELEMENT_ARRAY_BUFFER, 0 );
        if ( JCGLDebugger.DEBUGGING ) {
            // check for errors if we are debugging
            JCGLDebugger.checkGLError ( TAG + " makeShortIndexBuffer" );
        }
        return buf[ 0 ];
    }

    private static int makeIntIndexBuffer ( int[] data, int usage ) {
        int size = data.length * Constants.BYTES_PER_INT;
        int[] buf = new int[ 1 ];
        IntBuffer intBuffer = makeIntBuffer ( data );
        GLES32.glGenBuffers ( 1, buf, 0 );
        GLES32.glBindBuffer ( GLES32.GL_ELEMENT_ARRAY_BUFFER, buf[ 0 ] );
        GLES32.glBufferData ( GLES32.GL_ELEMENT_ARRAY_BUFFER, size, intBuffer, usage );
        GLES32.glBindBuffer ( GLES32.GL_ELEMENT_ARRAY_BUFFER, 0 );
        if ( JCGLDebugger.DEBUGGING ) {
            // check for errors if we are debugging
            JCGLDebugger.checkGLError ( TAG + " makeIntIndexBuffer" );
        }
        return buf[ 0 ];
    }

    public static int makeStaticDrawVBO ( float[] data ) {
        return makeFloatArrayBuffer ( data, GLES32.GL_STATIC_DRAW );
    }

    public static int makeDynamicDrawVBO ( float[] data ) {
        return makeFloatArrayBuffer ( data, GLES32.GL_DYNAMIC_DRAW );
    }

    public static int makeStreamDrawVBO ( float[] data ) {
        return makeFloatArrayBuffer ( data, GLES32.GL_STREAM_DRAW );
    }

    public static int makeStaticCopyVBO ( float[] data ) {
        return makeFloatArrayBuffer ( data, GLES32.GL_STATIC_COPY );
    }

    public static int makeDynamicCopyVBO ( float[] data ) {
        return makeFloatArrayBuffer ( data, GLES32.GL_DYNAMIC_COPY );
    }

    public static int makeStreamCopyVBO ( float[] data ) {
        return makeFloatArrayBuffer ( data, GLES32.GL_STREAM_COPY );
    }

    public static int makeStaticReadVBO ( float[] data ) {
        return makeFloatArrayBuffer ( data, GLES32.GL_STATIC_READ );
    }

    public static int makeDynamicReadVBO ( float[] data ) {
        return makeFloatArrayBuffer ( data, GLES32.GL_DYNAMIC_READ );
    }

    public static int makeStreamReadVBO ( float[] data ) {
        return makeFloatArrayBuffer ( data, GLES32.GL_STREAM_READ );
    }

    public static int makeShortIBO ( short[] data ) {
        return makeShortIndexBuffer ( data, GLES32.GL_STATIC_DRAW );
    }

    public static int makeIntIBO ( int[] data ) {
        return makeIntIndexBuffer ( data, GLES32.GL_STATIC_DRAW );
    }


    private static int makeUniformBuffer ( ByteBuffer data, int usage ) {
        if ( JCGLDebugger.DEBUGGING ) {
            if ( ! data.isDirect () ) {
                Log.d ( TAG, "passed non-direct ByteBuffer to makeUniformBuffer" );
            }
        }

        int size = data.capacity ();
        int[] buf = new int[ 1 ];
        GLES32.glGenBuffers ( 1, buf, 0 );
        GLES32.glBindBuffer ( GLES32.GL_UNIFORM_BUFFER, buf[ 0 ] );
        GLES32.glBufferData ( GLES32.GL_UNIFORM_BUFFER, size, data, usage );
        GLES32.glBindBuffer ( GLES32.GL_UNIFORM_BUFFER, 0 );
        return buf[ 0 ];
    }

    public static int makeStaticDrawFloatUBO ( float[] data ) {
        int size = data.length * Constants.BYTES_PER_FLOAT;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect ( size );
        byteBuffer.order ( ByteOrder.nativeOrder () );
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer ();
        floatBuffer.put ( data );
        floatBuffer.position ( 0 );
        return makeUniformBuffer ( byteBuffer, GLES32.GL_STATIC_DRAW );
    }

    public static int makeDynamicDrawFloatUBO ( float[] data ) {
        int size = data.length * Constants.BYTES_PER_FLOAT;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect ( size );
        byteBuffer.order ( ByteOrder.nativeOrder () );
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer ();
        floatBuffer.put ( data );
        floatBuffer.position ( 0 );
        return makeUniformBuffer ( byteBuffer, GLES32.GL_DYNAMIC_DRAW );
    }

    public static int makeStreamDrawFloatUBO ( float[] data ) {
        int size = data.length * Constants.BYTES_PER_FLOAT;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect ( size );
        byteBuffer.order ( ByteOrder.nativeOrder () );
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer ();
        floatBuffer.put ( data );
        floatBuffer.position ( 0 );
        return makeUniformBuffer ( byteBuffer, GLES32.GL_STREAM_DRAW );
    }

    public static int makeStaticCopyFloatUBO ( float[] data ) {
        int size = data.length * Constants.BYTES_PER_FLOAT;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect ( size );
        byteBuffer.order ( ByteOrder.nativeOrder () );
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer ();
        floatBuffer.put ( data );
        floatBuffer.position ( 0 );
        return makeUniformBuffer ( byteBuffer, GLES32.GL_STATIC_COPY );
    }

    public static int makeDynamicCopyFloatUBO ( float[] data ) {
        int size = data.length * Constants.BYTES_PER_FLOAT;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect ( size );
        byteBuffer.order ( ByteOrder.nativeOrder () );
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer ();
        floatBuffer.put ( data );
        floatBuffer.position ( 0 );
        return makeUniformBuffer ( byteBuffer, GLES32.GL_DYNAMIC_COPY );
    }

    public static int makeStreamCopyFloatUBO ( float[] data ) {
        int size = data.length * Constants.BYTES_PER_FLOAT;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect ( size );
        byteBuffer.order ( ByteOrder.nativeOrder () );
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer ();
        floatBuffer.put ( data );
        floatBuffer.position ( 0 );
        return makeUniformBuffer ( byteBuffer, GLES32.GL_STREAM_COPY );
    }

    public static int makeStaticReadFloatUBO ( float[] data ) {
        int size = data.length * Constants.BYTES_PER_FLOAT;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect ( size );
        byteBuffer.order ( ByteOrder.nativeOrder () );
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer ();
        floatBuffer.put ( data );
        floatBuffer.position ( 0 );
        return makeUniformBuffer ( byteBuffer, GLES32.GL_STATIC_READ );
    }

    public static int makeDynamicReadFloatUBO ( float[] data ) {
        int size = data.length * Constants.BYTES_PER_FLOAT;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect ( size );
        byteBuffer.order ( ByteOrder.nativeOrder () );
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer ();
        floatBuffer.put ( data );
        floatBuffer.position ( 0 );
        return makeUniformBuffer ( byteBuffer, GLES32.GL_DYNAMIC_READ );
    }

    public static int makeStreamReadFloatUBO ( float[] data ) {
        int size = data.length * Constants.BYTES_PER_FLOAT;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect ( size );
        byteBuffer.order ( ByteOrder.nativeOrder () );
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer ();
        floatBuffer.put ( data );
        floatBuffer.position ( 0 );
        return makeUniformBuffer ( byteBuffer, GLES32.GL_STREAM_READ );
    }
}
