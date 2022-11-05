package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES20;

class TriangleShader extends ShaderProgram {

    private int aLoc_vPosition;
    private int uLoc_vColor;
    private int uLoc_uMVP;

    TriangleShader ( Context context ) {
        super ( context, R.raw.triangle_vert, R.raw.triangle_frag );
        aLoc_vPosition = GLES20.glGetAttribLocation ( mProgram, "vPosition" );
        uLoc_vColor = GLES20.glGetUniformLocation ( mProgram, "vColor" );
        uLoc_uMVP = GLES20.glGetUniformLocation ( mProgram, "uMVP" );
    }

    int getaLoc_vPosition () {
        return aLoc_vPosition;
    }

    int getuLoc_vColor () {
        return uLoc_vColor;
    }

    int getuLoc_uMVP () {
        return uLoc_uMVP;
    }
}
