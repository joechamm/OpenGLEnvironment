package com.joechamm.openglenvironment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

public class JCGLSurfaceView extends GLSurfaceView {

    private final String TAG = "jcglenv:surfaceview";

    private final JCGLRenderer mRenderer;

    // reference to our context
    private Context mContext;

    // TODO: handle multiple context versions
    private final int EGL_CTX_VERSION_MAJ = 2;

    private static int glesMajorVersion = 1;
    private static int glesMinorVersion = 0;

    // touch event members
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float previousX;
    private float previousY;

    @Override
    public boolean onTouchEvent ( MotionEvent event ) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = event.getX ();
        float y = event.getY ();

        //noinspection SwitchStatementWithTooFewBranches
        switch ( event.getAction () ) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - previousX;
                float dy = y - previousY;

                // reverse direction of rotation about the mid-line
                if ( y > getHeight () / 2 ) {
                    dx = dx * - 1;
                }

                // reverse direction of rotation to left of the mid-line
                if ( x < getWidth () / 2 ) {
                    dy = dy * - 1;
                }

                mRenderer.setAngle (
                        mRenderer.getAngle () + ( ( dx + dy ) * TOUCH_SCALE_FACTOR ) );
                requestRender ();
        }

        previousX = x;
        previousY = y;
        return true;
    }

    public JCGLSurfaceView ( Context context ) {
        super ( context );

        mContext = context;

        // DEBUGGING
        Log.d ( TAG, "Detecting OpenGL ES Version present" );
        detectGLESVersion ();
        Log.d ( TAG, "Detected OpenGL ES Version: " + glesMajorVersion + "." + glesMinorVersion );

        if ( BuildConfig.IS_DEBUGGING ) {
            Log.d ( TAG, "Setting debug flags: DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS" );
            setDebugFlags ( DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS );
        }

        Log.d ( TAG, "setting EGLConfigChooser with RGBA8, 24-bit depth" );
        setEGLConfigChooser ( new JCEGLConfigChooser ( 8, 8, 8, 8, 24, 0, 0, 0 ) );

        // TODO: setGLWrapper

        //Log.d ( TAG, "calling setPreserverEGLContextOnPause" );
        //setPreserveEGLContextOnPause ( true );

        // DEBUGGING
        Log.d ( TAG, "JCGLSurfaceView ctor called" );
        setEGLContextFactory ( new ContextFactory () );

        // Create an OpenGL ES context
        //    setEGLContextClientVersion ( EGL_CTX_VERSION_MAJ );
        //    setEGLConfigChooser ( true );

        mRenderer = new JCGLRenderer ( context );

        // Set the renderer for drawing on the GLSurfaceView
        setRenderer ( mRenderer );

        // Render the view only when there is a change in the drawing data
        setRenderMode ( GLSurfaceView.RENDERMODE_CONTINUOUSLY );
    }

    private void detectGLESVersion () {
        Log.d ( TAG, "detecting OpenGL ES Version" );
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService ( Context.ACTIVITY_SERVICE );
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo ();
        if ( configurationInfo.reqGlEsVersion >= 0x00030002 ) {
            glesMajorVersion = 3;
            glesMinorVersion = 2;
        } else if ( configurationInfo.reqGlEsVersion >= 0x00030001 ) {
            glesMajorVersion = 3;
            glesMinorVersion = 1;
        } else if ( configurationInfo.reqGlEsVersion >= 0x00030000 ) {
            glesMajorVersion = 3;
            glesMinorVersion = 0;
        } else if ( configurationInfo.reqGlEsVersion >= 0x00020000 ) {
            glesMajorVersion = 2;
            glesMinorVersion = 0;
        } else if ( configurationInfo.reqGlEsVersion >= 0x00010001 ) {
            glesMajorVersion = 1;
            glesMinorVersion = 1;
        } else if ( configurationInfo.reqGlEsVersion >= 0x00010000 ) {
            glesMajorVersion = 1;
            glesMinorVersion = 0;
        } else {
            // it looks like OpenGL isn't supported on this device
            // indicate with -1
            glesMajorVersion = - 1;
            glesMinorVersion = - 1;
        }
        Log.d ( TAG, "found OpenGL ES Version: " + glesMajorVersion + "." + glesMinorVersion );
    }

    private static class ContextFactory implements GLSurfaceView.EGLContextFactory {
        private static int EGL_CONTEXT_CLIENT_VERSION = 0x3098;

        private static final String TAG = "jcglenv:view:ctxfactory";

        @Override
        public EGLContext createContext (
                EGL10 egl, EGLDisplay display, EGLConfig eglConfig
        ) {
            Log.d ( TAG, "creating OpenGL ES " + glesMajorVersion + "." + glesMinorVersion + " context" );

            JCGLContextHelper.logEGLErrors ( egl, "Before eglCreateContext" );

            int[] attribList = {
                    EGL_CONTEXT_CLIENT_VERSION, glesMajorVersion,
                    EGL10.EGL_NONE
            };

            EGLContext context = egl.eglCreateContext ( display, eglConfig, EGL10.EGL_NO_CONTEXT, attribList );

            JCGLContextHelper.logEGLErrors ( egl, "After eglCreateContext" );

            return context;
        }

        @Override
        public void destroyContext ( EGL10 egl, EGLDisplay display, EGLContext context ) {
            Log.d ( TAG, "destroying OpenGL ES context" );

            JCGLContextHelper.logEGLErrors ( egl, "Before eglDestroyContext" );

            egl.eglDestroyContext ( display, context );

            JCGLContextHelper.logEGLErrors ( egl, "After eglDestroyContext" );
        }
    }
}
