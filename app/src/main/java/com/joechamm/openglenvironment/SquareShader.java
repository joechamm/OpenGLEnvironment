package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class SquareShader extends ShaderProgram {

    private static final String TAG = "jcglenv:sqshader";

    private final int aLoc_position;
    private final int aLoc_color;
    private final int uLoc_uMVP;

    SquareShader ( Context context ) {
        super ( context, R.raw.square_vert, R.raw.square_frag );
        aLoc_position = GLES20.glGetAttribLocation ( mProgram, "a_position" );
        aLoc_color = GLES20.glGetAttribLocation ( mProgram, "a_color" );
        uLoc_uMVP = GLES20.glGetUniformLocation ( mProgram, "uMVP" );

        if ( BuildConfig.IS_DEBUGGING ) {
            Log.d ( TAG,
                    "Square Shader:\n-- Attribute Locations --\n a_position: " + aLoc_position + "\n a_color: " + aLoc_color + "\n-- Uniform Locations --\n uMVP: " + uLoc_uMVP + "\n\n" );
        }
    }

    int getaLoc_position () {
        return aLoc_position;
    }

    int getaLoc_color () {
        return aLoc_color;
    }

    int getuLoc_uMVP () {
        return uLoc_uMVP;
    }
}
