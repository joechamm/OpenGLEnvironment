package com.joechamm.openglenvironment;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class JCOpenGLESActivity extends Activity {

    private final String TAG = "jcglenv:activity";

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );

        // DEBUGGING
        Log.d ( TAG, "JCOpenGLESActivity onCreate called" );

        // Create a GLSurfaceView instance and set it as the ContentView for this activity
        mGLSurfaceView = new JCGLSurfaceView ( this );
        setContentView ( mGLSurfaceView );
    }

    @Override
    public boolean onCreateOptionsMenu ( Menu menu ) {
        //    getMenuInflater ().inflate ( R.menu.activity_openglenvironment, menu );
        return true;
    }
}
