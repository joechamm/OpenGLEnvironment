package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES32;
import android.util.Log;

abstract class ShaderProgram {

    private static final String TAG = "jcglenv:shdprog";

    protected final int mProgram;

    ShaderProgram (
            Context context,
            int vertexShaderResID,
            int fragmentShaderResID
    ) {
        if ( BuildConfig.IS_DEBUGGING ) {
            JCGLDebugger.checkGLError ( "Shader Program before compile" );
        }

        String vertexShaderSource = ShaderHelper.loadStringResource ( context, vertexShaderResID );
        String fragmentShaderSource = ShaderHelper.loadStringResource ( context, fragmentShaderResID );

        final int vertexShader = ShaderHelper.compileVertexShader ( vertexShaderSource );
        final int fragmentShader = ShaderHelper.compileFragmentShader ( fragmentShaderSource );

        if ( BuildConfig.IS_DEBUGGING ) {
            JCGLDebugger.checkGLError ( "Shader Program after compile, before link" );
        }

        mProgram = ShaderHelper.createAndLinkProgram ( vertexShader, fragmentShader, null );

        if ( BuildConfig.IS_DEBUGGING ) {
            JCGLDebugger.checkGLError ( "Shader Program after link, before validate" );
            boolean validated = ShaderHelper.validateShaderProgram ( mProgram );
            JCGLDebugger.checkGLError ( "Shader Program after validation" );
            if ( validated ) {
                Log.d ( TAG, "VALIDATED" );
            } else {
                Log.d ( TAG, "NOT VALIDATED" );
            }
        }
    }

    public void useProgram () {
        GLES32.glUseProgram ( mProgram );
    }
}
