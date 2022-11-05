package com.joechamm.openglenvironment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class JCOpenGLESActivity extends Activity {

    private final String TAG = "jcglenv:activity";

    private GLSurfaceView mGLSurfaceView;

    private boolean rendererSet = false;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );

        // DEBUGGING
        Log.d ( TAG, "JCOpenGLESActivity onCreate called" );

        // Make sure we support OpenGL ES 2.0
        final ActivityManager activityManager = (ActivityManager) getSystemService ( Context.ACTIVITY_SERVICE );
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo ();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
                || ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && ( Build.FINGERPRINT.startsWith ( "generic" )
                || Build.FINGERPRINT.startsWith ( "unknown" )
                || Build.MODEL.contains ( "google_sdk" )
                || Build.MODEL.contains ( "Emulator" )
                || Build.MODEL.contains ( "Android SDK built for x86" ) ) );

        if ( ! supportsEs2 ) {
            Toast.makeText ( this, "OpenGL ES 2.0 NOT SUPPORTED ON THIS DEVICE!", Toast.LENGTH_LONG ).show ();
            return;
        }

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
