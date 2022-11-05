package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES20;

abstract class ShaderProgram {

    protected final int mProgram;

    ShaderProgram (
            Context context,
            int vertexShaderResID,
            int fragmentShaderResID
    ) {

        String vertexShaderSource = ShaderHelper.loadStringResource ( context, vertexShaderResID );
        String fragmentShaderSource = ShaderHelper.loadStringResource ( context, fragmentShaderResID );

        final int vertexShader = ShaderHelper.compileVertexShader ( vertexShaderSource );
        final int fragmentShader = ShaderHelper.compileFragmentShader ( fragmentShaderSource );

        mProgram = ShaderHelper.createAndLinkProgram ( vertexShader, fragmentShader, null );
    }

    public void useProgram () {
        GLES20.glUseProgram ( mProgram );
    }
}
