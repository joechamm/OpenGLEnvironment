package com.joechamm.openglenvironment;

import java.nio.IntBuffer;

interface IntBufferInterface {

    void initIntData ( int[] data );

    void uploadIntData ( int[] data, int offset );  // upload an array of ints

    int[] getIntData ();

    IntBuffer getIntBuffer ();

    int getIntDataLength ();
}
