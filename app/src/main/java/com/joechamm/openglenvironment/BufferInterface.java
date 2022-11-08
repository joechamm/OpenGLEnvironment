package com.joechamm.openglenvironment;

interface BufferInterface {

    enum BufferType {
        BUFFER_TYPE_FLOAT,
        BUFFER_TYPE_INT,
        BUFFER_TYPE_SHORT,
        BUFFER_TYPE_BYTE,
        BUFFER_TYPE_COUNT
    }

    enum BufferMode {
        BUFFER_MODE_CLIENT_SEPARATE,
        BUFFER_MODE_CLIENT_PACKED,
        BUFFER_MODE_GPU_SEPARATE,
        BUFFER_MODE_GPU_PACKED,
        BUFFER_MODE_COUNT
    }

    public int getHandle ();

    public void bind ();

    BufferType getType ();

    BufferMode getMode ();

}
