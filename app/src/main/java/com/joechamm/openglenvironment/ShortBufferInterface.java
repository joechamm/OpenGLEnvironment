package com.joechamm.openglenvironment;

import java.nio.ShortBuffer;

interface ShortBufferInterface {

    void initShortData ( short[] data );

    void uploadShortData ( short[] data, int offset );  // upload an array of shorts

    short[] getShortData ();

    ShortBuffer getShortBuffer ();

    int getShortDataLength ();
}
