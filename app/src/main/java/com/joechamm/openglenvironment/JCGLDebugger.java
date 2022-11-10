package com.joechamm.openglenvironment;

import android.opengl.GLES32;
import android.opengl.GLU;
import android.util.Log;

public class JCGLDebugger
        implements GLES32.DebugProc {

    // TODO: handle debugging on/off later
    public static final boolean DEBUGGING = BuildConfig.IS_DEBUGGING;

    private static final String TAG = "jcglenv:debugger";

    public static void checkGLError ( String op ) {
        int error;
        while ( ( error = GLES32.glGetError () ) != GLES32.GL_NO_ERROR ) {
            String errStr = GLU.gluErrorString ( error );
            Log.e ( TAG, op + ": glError " + errStr );
        }
    }

    @Override
    public void onMessage ( int source, int type, int id, int severity, String message ) {

        if ( DEBUGGING ) {
            switch ( source ) {
                case GLES32.GL_DEBUG_SOURCE_API:
                    Log.d ( TAG, "GL_DEBUG_SOURCE_API" );
                    break;
                case GLES32.GL_DEBUG_SOURCE_APPLICATION:
                    Log.d ( TAG, "GL_DEBUG_SOURCE_APPLICATION" );
                    break;
                case GLES32.GL_DEBUG_SOURCE_SHADER_COMPILER:
                    Log.d ( TAG, "GL_DEBUG_SOURCE_SHADER_COMPILER" );
                    break;
                case GLES32.GL_DEBUG_SOURCE_WINDOW_SYSTEM:
                    Log.d ( TAG, "GL_DEBUG_SOURCE_WINDOW_SYSTEM" );
                    break;
                case GLES32.GL_DEBUG_SOURCE_THIRD_PARTY:
                    Log.d ( TAG, "GL_DEBUG_SOURCE_THIRD_PARTY" );
                    break;
                case GLES32.GL_DEBUG_SOURCE_OTHER:
                    Log.d ( TAG, "GL_DEBUG_SOURCE_OTHER" );
                    break;
                default:
                    Log.d ( TAG, "SOURCE_UNKNOWN" );
                    break;
            }

            switch ( type ) {
                case GLES32.GL_DEBUG_TYPE_ERROR:
                    Log.d ( TAG, "GL_DEBUG_TYPE_ERROR" );
                case GLES32.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
                    Log.d ( TAG, "GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR" );
                    break;
                case GLES32.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
                    Log.d ( TAG, "GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR" );
                    break;
                case GLES32.GL_DEBUG_TYPE_PORTABILITY:
                    Log.d ( TAG, "GL_DEBUG_TYPE_PORTABILITY" );
                    break;
                case GLES32.GL_DEBUG_TYPE_PERFORMANCE:
                    Log.d ( TAG, "GL_DEBUG_TYPE_PERFORMANCE" );
                    break;
                case GLES32.GL_DEBUG_TYPE_MARKER:
                    Log.d ( TAG, "GL_DEBUG_TYPE_MARKER" );
                    break;
                case GLES32.GL_DEBUG_TYPE_OTHER:
                    Log.d ( TAG, "GL_DEBUG_TYPE_OTHER" );
                    break;
                default:
                    Log.d ( TAG, "TYPE_UNKNOWN" );
                    break;
            }

            switch ( severity ) {
                case GLES32.GL_DEBUG_SEVERITY_NOTIFICATION:
                    Log.d ( TAG, "GL_DEBUG_SEVERITY_NOTIFICATION" );
                    break;
                case GLES32.GL_DEBUG_SEVERITY_LOW:
                    Log.d ( TAG, "GL_DEBUG_SEVERITY_LOW" );
                    break;
                case GLES32.GL_DEBUG_SEVERITY_MEDIUM:
                    Log.d ( TAG, "GL_DEBUG_SEVERITY_MEDIUM" );
                    break;
                case GLES32.GL_DEBUG_SEVERITY_HIGH:
                    Log.d ( TAG, "GL_DEBUG_SEVERITY_HIGH" );
                    break;
                default:
                    Log.d ( TAG, "SEVERITY_UNKNOWN" );
                    break;
            }

            Log.d ( TAG, "DEBUG ID: " + id + "\nMESSAGE:\n" + message );
        }
    }
}
