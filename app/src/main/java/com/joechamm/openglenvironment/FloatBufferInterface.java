package com.joechamm.openglenvironment;

import java.nio.FloatBuffer;

interface FloatBufferInterface {

    void initFloatData ( float[] data );

    void uploadFloatData ( float[] data, int offset );  // upload an array of floats

    float[] getFloatData ();

    FloatBuffer getFloatBuffer ();

    int getFloatDataLength ();
}
