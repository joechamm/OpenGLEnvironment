package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES32;
import android.util.Log;

// TODO: move to factory pattern
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

        String vertexShaderSource = ResourceLoader.loadStringResource ( context, vertexShaderResID );
        String fragmentShaderSource = ResourceLoader.loadStringResource ( context, fragmentShaderResID );

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

    ShaderProgram (
            Context context,
            int vertexShaderResID,
            int fragmentShaderResID,
            int geometryShaderResID
    ) {
        if ( BuildConfig.IS_DEBUGGING ) {
            JCGLDebugger.checkGLError ( "Shader Program before compile" );
        }

        String vertexShaderSource = ResourceLoader.loadStringResource ( context, vertexShaderResID );
        String fragmentShaderSource = ResourceLoader.loadStringResource ( context, fragmentShaderResID );
        String geometryShaderSource = ResourceLoader.loadStringResource ( context, geometryShaderResID );

        final int vertexShader = ShaderHelper.compileVertexShader ( vertexShaderSource );
        final int fragmentShader = ShaderHelper.compileFragmentShader ( fragmentShaderSource );
        final int geometryShader = ShaderHelper.compileGeometryShader ( geometryShaderSource );

        if ( BuildConfig.IS_DEBUGGING ) {
            JCGLDebugger.checkGLError ( "Shader Program after compile, before link" );
        }

        mProgram = ShaderHelper.createAndLinkRenderProgram ( vertexShader, fragmentShader, geometryShader, null );

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

    ShaderProgram (
            Context context,
            int vertexShaderResID,
            int fragmentShaderResID,
            int tessControlShaderResID,
            int tessEvalShaderResID
    ) {
        if ( BuildConfig.IS_DEBUGGING ) {
            JCGLDebugger.checkGLError ( "Shader Program before compile" );
        }

        String vertexShaderSource = ResourceLoader.loadStringResource ( context, vertexShaderResID );
        String fragmentShaderSource = ResourceLoader.loadStringResource ( context, fragmentShaderResID );
        String tessControlShaderSource = ResourceLoader.loadStringResource ( context, tessControlShaderResID );
        String tessEvalShaderSource = ResourceLoader.loadStringResource ( context, tessEvalShaderResID );

        final int vertexShader = ShaderHelper.compileVertexShader ( vertexShaderSource );
        final int fragmentShader = ShaderHelper.compileFragmentShader ( fragmentShaderSource );
        final int tessControlShader = ShaderHelper.compileTessControlShader ( tessControlShaderSource );
        final int tessEvalShader = ShaderHelper.compileTessEvaluationShader ( tessEvalShaderSource );

        if ( BuildConfig.IS_DEBUGGING ) {
            JCGLDebugger.checkGLError ( "Shader Program after compile, before link" );
        }

        mProgram = ShaderHelper.createAndLinkRenderProgram ( vertexShader, fragmentShader, tessControlShader, tessEvalShader, null );

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

    ShaderProgram (
            Context context,
            int vertexShaderResID,
            int fragmentShaderResID,
            int geometryShaderResID,
            int tessControlShaderResID,
            int tessEvalShaderResID
    ) {
        if ( BuildConfig.IS_DEBUGGING ) {
            JCGLDebugger.checkGLError ( "Shader Program before compile" );
        }

        String vertexShaderSource = ResourceLoader.loadStringResource ( context, vertexShaderResID );
        String fragmentShaderSource = ResourceLoader.loadStringResource ( context, fragmentShaderResID );
        String geometryShaderSource = ResourceLoader.loadStringResource ( context, geometryShaderResID );
        String tessControlShaderSource = ResourceLoader.loadStringResource ( context, tessControlShaderResID );
        String tessEvalShaderSource = ResourceLoader.loadStringResource ( context, tessEvalShaderResID );

        final int vertexShader = ShaderHelper.compileVertexShader ( vertexShaderSource );
        final int fragmentShader = ShaderHelper.compileFragmentShader ( fragmentShaderSource );
        final int geometryShader = ShaderHelper.compileGeometryShader ( geometryShaderSource );
        final int tessControlShader = ShaderHelper.compileTessControlShader ( tessControlShaderSource );
        final int tessEvalShader = ShaderHelper.compileTessEvaluationShader ( tessEvalShaderSource );

        if ( BuildConfig.IS_DEBUGGING ) {
            JCGLDebugger.checkGLError ( "Shader Program after compile, before link" );
        }

        mProgram = ShaderHelper.createAndLinkRenderProgram ( vertexShader, fragmentShader, geometryShader, tessControlShader,
                                                             tessEvalShader, null );

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

    ShaderProgram (
            Context context,
            int computeShaderResID
    ) {
        if ( BuildConfig.IS_DEBUGGING ) {
            JCGLDebugger.checkGLError ( "Shader Program before compile" );
        }

        String computeShaderSource = ResourceLoader.loadStringResource ( context, computeShaderResID );

        final int computeShader = ShaderHelper.compileComputeShader ( computeShaderSource );

        if ( BuildConfig.IS_DEBUGGING ) {
            JCGLDebugger.checkGLError ( "Shader Program after compile, before link" );
        }

        mProgram = ShaderHelper.createAndLinkComputeProgram ( computeShader );

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
