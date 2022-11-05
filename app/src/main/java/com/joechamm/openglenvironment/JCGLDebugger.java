package com.joechamm.openglenvironment;

import android.opengl.GLES20;
import android.util.Log;

import android.opengl.GLU;

public class JCGLDebugger {

    // TODO: handle debugging on/off later
    public static final boolean DEBUGGING = true;

    private static final String TAG = "jcglenv:debugger";

    public static void checkGLError ( String op ) {
        int error;
        while ( ( error = GLES20.glGetError () ) != GLES20.GL_NO_ERROR ) {
            String errStr = GLU.gluErrorString ( error );
            Log.e ( TAG, op + ": glError " + errStr );
        }
    }
}
