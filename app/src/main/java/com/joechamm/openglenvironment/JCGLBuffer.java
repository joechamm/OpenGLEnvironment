package com.joechamm.openglenvironment;

abstract class JCGLBuffer {

    enum BufferPacking {
        BUFFER_PACKING_TIGHT, // Buffer data is stored in an AoS (Array of Structures) type format (e.g. { x0,y0,z0,r0,g0,b0,u0,v0}, { x1,y1,z1,r1,g1,b1,u1,v1}, etc...)
        BUFFER_PACKING_SEPARATE, // Buffer data is stored in an SoA (Structure of Arrays) type format (e.g. {x0,y0,z0,x1,y1,z1,...},{r0,g0,b0,r1,g1,b1,...},{u0,v0,u1,v1,...})
        BUFFER_PACKING_COUNT
    }

    enum BufferBacking {
        BUFFER_BACKING_CLIENT, // Client side only data
        BUFFER_BACKING_SERVER, // Buffer data is stored on the GPU
        BUFFER_BACKING_COUNT
    }

    enum BufferDataType {
        BUFFER_DATA_TYPE_FLOAT,
        BUFFER_DATA_TYPE_INT,
        BUFFER_DATA_TYPE_UINT,
        BUFFER_DATA_TYPE_SHORT,
        BUFFER_DATA_TYPE_USHORT,
        BUFFER_DATA_TYPE_BYTE,
        BUFFER_DATA_TYPE_UBYTE,
        BUFFER_DATA_TYPE_COUNT
    }

    public abstract boolean isTightlyPacked ();

    public abstract boolean isClientSideOnly ();

    public abstract BufferDataType getBufferDataType ();
}
