package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES32;

class TriangleShader extends ShaderProgram {

    private final int aLoc_position;
    private final int uLoc_color;

    TriangleShader ( Context context ) {
        super ( context, R.raw.triangle_vert, R.raw.triangle_frag );
        aLoc_position = GLES32.glGetAttribLocation ( mProgram, "a_position" );
        uLoc_color = GLES32.glGetUniformLocation ( mProgram, "u_color" );
    }

    int getaLoc_vPosition () {
        return aLoc_position;
    }

    int getuLoc_vColor () {
        return uLoc_color;
    }
}
