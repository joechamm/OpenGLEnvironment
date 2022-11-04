package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class JCGLSurfaceView extends GLSurfaceView {

    private final JCGLRenderer mRenderer;

    private final String TAG = "jcglenv:surfaceview";

    // TODO: handle multiple context versions
    private final int EGL_CTX_VERSION_MAJ = 2;

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

        // DEBUGGING
        Log.d ( TAG, "JCGLSurfaceView ctor called" );

        // Create an OpenGL ES context
        setEGLContextClientVersion ( EGL_CTX_VERSION_MAJ );

        mRenderer = new JCGLRenderer ();

        // Set the renderer for drawing on the GLSurfaceView
        setRenderer ( mRenderer );

        // Render the view only when there is a change in the drawing data
        setRenderMode ( GLSurfaceView.RENDERMODE_WHEN_DIRTY );
    }
}
