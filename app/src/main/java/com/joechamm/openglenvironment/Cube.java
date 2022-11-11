package com.joechamm.openglenvironment;

import android.content.Context;

public class Cube extends Renderable {

    private static final String TAG = "jcglenv:cube";

    private static final float[] cubeVertices = {
            // front
            /* pos */ - 1.0f, - 1.0f, 1.0f, /* col */ 0.43f, 0.81f, 0.91f, 1.0f,
            /* pos */ 1.0f, - 1.0f, 1.0f, /* col */ 0.43f, 0.81f, 0.91f, 1.0f,
            /* pos */ 1.0f, 1.0f, 1.0f, /* col */ 0.43f, 0.81f, 0.91f, 1.0f,
            /* pos */ - 1.0f, 1.0f, 1.0f, /* col */ 0.43f, 0.81f, 0.91f, 1.0f,
            // back
            /* pos */ - 1.0f, - 1.0f, - 1.0f, /* col */ 0.43f, 0.81f, 0.91f, 1.0f,
            /* pos */ 1.0f, - 1.0f, - 1.0f, /* col */ 0.43f, 0.81f, 0.91f, 1.0f,
            /* pos */ 1.0f, 1.0f, - 1.0f, /* col */ 0.43f, 0.81f, 0.91f, 1.0f,
            /* pos */ - 1.0f, 1.0f, - 1.0f, /* col */ 0.43f, 0.81f, 0.91f, 1.0f
    };

    private static final short[] cubeIndices = {
            // front
            0, 1, 2,
            2, 3, 0,
            // right
            1, 5, 6,
            6, 2, 1,
            // back
            7, 6, 5,
            5, 4, 7,
            // left
            4, 0, 3,
            3, 7, 4,
            // bottom
            4, 5, 1,
            1, 0, 4,
            // top
            3, 2, 6,
            6, 7, 3
    };

    public Cube ( Context context ) {
        super ( context, cubeVertices, cubeIndices );

        CubeShader cubeShader = new CubeShader ( context );

        setShaderProgram ( cubeShader );

        int posIdx = cubeShader.getaLoc_position ();
        int colIdx = cubeShader.getaLoc_color ();
        int norIdx = - 1;
        int uvIdx = - 1;

        initVAO ( posIdx, colIdx, norIdx, uvIdx );
    }
}
