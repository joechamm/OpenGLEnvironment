package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES32;
import android.util.Log;

public class CubeShader extends ShaderProgram {

    private static final String TAG = "jcglenv:sqshader";

    private final int aLoc_position;
    private final int aLoc_color;

    CubeShader ( Context context ) {
        super ( context, R.raw.cube_vert, R.raw.cube_frag, R.raw.cube_geom );
        aLoc_position = GLES32.glGetAttribLocation ( mProgram, "a_position" );
        aLoc_color = GLES32.glGetAttribLocation ( mProgram, "a_color" );

        if ( BuildConfig.IS_DEBUGGING ) {
            Log.d ( TAG,
                    "Cube Shader:\n-- Attribute Locations --\n a_position: " + aLoc_position + "\n a_color: " + aLoc_color + "\n\n" );
        }
    }

    int getaLoc_position () {

        return aLoc_position;
    }

    int getaLoc_color () {
        return aLoc_color;
    }
}
