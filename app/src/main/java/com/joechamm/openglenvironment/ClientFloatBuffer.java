package com.joechamm.openglenvironment;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class ClientFloatBuffer extends ClientBuffer
        implements FloatBufferInterface {

    private boolean mTightlyPacked;
    private FloatBuffer mFloatBuffer;

    ClientFloatBuffer ( float[] data, boolean tightlyPacked ) {
        mTightlyPacked = tightlyPacked;
        initFloatData ( data );
    }

    @Override
    public boolean isTightlyPacked () {
        return mTightlyPacked;
    }

    @Override
    public BufferDataType getBufferDataType () {
        return BufferDataType.BUFFER_DATA_TYPE_FLOAT;
    }

    @Override
    public void initFloatData ( float[] data ) {
        int byteCount = Constants.BYTES_PER_FLOAT * data.length;
        mDataBuffer = ByteBuffer.allocateDirect ( byteCount );
        mDataBuffer.order ( ByteOrder.nativeOrder () );
        mFloatBuffer = mDataBuffer.asFloatBuffer ();
        mFloatBuffer.put ( data );
        mFloatBuffer.position ( 0 );
    }

    @Override
    public void uploadFloatData ( float[] data, int offset ) {
        // copy 'data.length' floats from data to 'mFloatBuffer.position + offset'
        // throws 'BufferOverflowException' if 'data.length' > 'mFloatBuffer.remaining'
        int len = data.length;
        mFloatBuffer.put ( data, offset, len );
    }

    @Override
    public float[] getFloatData () {
        return mFloatBuffer.array ();
    }

    @Override
    public FloatBuffer getFloatBuffer () {
        return mFloatBuffer.duplicate ();
    }

    @Override
    public int getFloatDataLength () {
        int byteSize = mDataBuffer.capacity ();
        return ( byteSize / Constants.BYTES_PER_FLOAT );
    }
}
