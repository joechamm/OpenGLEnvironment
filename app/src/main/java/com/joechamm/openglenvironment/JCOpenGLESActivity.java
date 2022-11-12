package com.joechamm.openglenvironment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class JCOpenGLESActivity extends Activity {

    private final String TAG = "jcglenv:activity";

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );

        // DEBUGGING
        Log.d ( TAG, "JCOpenGLESActivity onCreate called" );

        // Create a GLSurfaceView instance and set it as the ContentView for this activity
        //    mGLSurfaceView = new JCGLSurfaceView ( this );
        mGLSurfaceView = new EigenFluidsView ( this );
        setContentView ( mGLSurfaceView );
    }

    @Override
    public boolean onCreateOptionsMenu ( Menu menu ) {
        //    getMenuInflater ().inflate ( R.menu.activity_openglenvironment, menu );
        return true;
    }

    @Override
    public void onWindowFocusChanged ( boolean hasFocus ) {
        super.onWindowFocusChanged ( hasFocus );
        if ( hasFocus ) {
            getWindow ().getDecorView ().setSystemUiVisibility (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY );
        }
    }

    private boolean detectOpenGLES30 () {
        ActivityManager activityManager = (ActivityManager) getSystemService ( Context.ACTIVITY_SERVICE );
        ConfigurationInfo info = activityManager.getDeviceConfigurationInfo ();
        return ( info.reqGlEsVersion >= 0x30000 );
    }
}
