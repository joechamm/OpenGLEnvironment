package com.joechamm.openglenvironment;

import java.nio.ByteBuffer;

interface ByteBufferInterface {

    void initByteData ( byte[] data );

    void uploadByteData ( byte[] data, int offset );  // upload an array of bytes

    byte[] getByteData ();

    ByteBuffer getByteBuffer ();

    int getByteDataLength ();
}
