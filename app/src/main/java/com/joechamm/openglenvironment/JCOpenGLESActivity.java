package com.joechamm.openglenvironment;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class JCOpenGLESActivity extends Activity {

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );

        // Create a GLSurfaceView instance and set it as the ContentView for this activity
        mGLSurfaceView = new JCGLSurfaceView ( this );
        setContentView ( mGLSurfaceView );

    }
}
