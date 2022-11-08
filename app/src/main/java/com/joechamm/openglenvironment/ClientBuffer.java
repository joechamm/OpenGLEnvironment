package com.joechamm.openglenvironment;

import java.nio.ByteBuffer;

abstract class ClientBuffer extends JCGLBuffer {

    protected ByteBuffer mDataBuffer;

    @Override
    public boolean isClientSideOnly () {
        return true;
    }

    public int getCapacity () {
        return mDataBuffer.capacity ();
    }

    public int getPosition () {
        return mDataBuffer.position ();
    }

    public int getRemaining () {
        return mDataBuffer.remaining ();
    }
}
