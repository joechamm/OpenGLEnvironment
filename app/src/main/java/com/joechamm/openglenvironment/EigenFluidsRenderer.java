package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class EigenFluidsRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "jcglenv:efluidrend";

    // reference to current context (activity)
    private final Context mContext;

    private PerFrameUniforms mPerFrameUniforms;

    private static final int VEL_RESOLUTION = 36;
    private static final int DEN_RESOLUTION = 36;
    private static final int DIMENSION = 36;

    public static final boolean RENDER_VELOCITY = true;
    public static final boolean RENDER_DENSITY = true;

    public static int FORCE_MODE = 2;
    public static int DENSITY_MODE = 1;
    public static int VELOCITY_MODE = 1;

    public static boolean IS_PRESSED = false;
    public static float PRESS_X = 0.5f;
    public static float PRESS_Y = 0.5f;

    private static int WIN_WIDTH;
    private static int WIN_HEIGHT;

    public static float[] POS_1 = new float[ 2 ];
    public static float[] POS_2 = new float[ 2 ];

    public static ArrayList<Float> DRAG_PATH_X;
    public static ArrayList<Float> DRAG_PATH_Y;

    public VelocityDisplay mVelocityDisplay = null;
    public DensityDisplay mDensityDisplay = null;

    EigenFluidsRenderer ( Context context ) {
        mContext = context;

        // initialize our laplacian eigenfunctions
        // DEBUGGING
        Log.d ( TAG, "Initializing LEFuncs" );
        LEFuncs.init ( VEL_RESOLUTION, DEN_RESOLUTION, DIMENSION );
        Log.d ( TAG, "Expanding Basis" );
        LEFuncs.expand_basis ();
    }

    @Override
    public void onSurfaceCreated ( GL10 gl, EGLConfig config ) {
        // DEBUGGING
        Log.d ( TAG, "onSurfaceCreated called" );

        JCGLContextHelper.logGLVersion ( gl );
        JCGLContextHelper.logGLVendor ( gl );
        JCGLContextHelper.logGLRenderer ( gl );
        JCGLContextHelper.logGLExtensions ( gl );
        JCGLContextHelper.logGLSLVersion ( gl );

        Log.d ( TAG, "creating velocity display" );
        mVelocityDisplay = new VelocityDisplay ( mContext, LEFuncs.VEL_ROWS, LEFuncs.VEL_COLS );
        Log.d ( TAG, "creating density display" );
        mDensityDisplay = new DensityDisplay ( mContext, LEFuncs.DEN_ROWS, LEFuncs.DEN_COLS );

        // set background color
        GLES32.glClearColor ( 0.0f, 0.0f, 0.0f, 0.0f );

        // set the line width
        GLES32.glLineWidth ( 1.0f );

        // set front face
        GLES32.glFrontFace ( GLES32.GL_CCW );

        // initialize uniform buffer
        mPerFrameUniforms = new PerFrameUniforms ();
    }

    @Override
    public void onSurfaceChanged ( GL10 unused, int width, int height ) {
        WIN_WIDTH = width;
        WIN_HEIGHT = height;

        mPerFrameUniforms.setModelIdentity ();
        mPerFrameUniforms.setProjOrtho ( 0.0f, 1.0f, 0.0f, 1.0f, - 1.0f, 1.0f );
//        mPerFrameUniforms.setProjOrtho ( -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f );

        GLES32.glViewport ( 0, 0, WIN_WIDTH, WIN_HEIGHT );
    }

    @Override
    public void onDrawFrame ( GL10 unused ) {
        LEFuncs.step ();

        GLES32.glClear ( GLES32.GL_COLOR_BUFFER_BIT );

        if ( mPerFrameUniforms.needsUpdate () ) {
            mPerFrameUniforms.updateBuffer ();
        }

        // if RENDER_DENSITY is on, update the density texture and render
        if ( RENDER_DENSITY ) {
            mDensityDisplay.updateTEX ();
            mDensityDisplay.render ();
        }

        // if RENDER_VELOCITY is on, update the velocity buffer and render
        if ( RENDER_VELOCITY ) {
            mVelocityDisplay.updateVBO ();
            mVelocityDisplay.render ();
        }
    }

    public void handleTouchEvent ( MotionEvent event ) {
        // handle touch event cases
        switch ( event.getAction () ) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown ( event );
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove ( event );
                break;
            case MotionEvent.ACTION_CANCEL:
                handleActionCancel ( event );
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp ( event );
                break;
        }
    }

    public void handleActionDown ( MotionEvent event ) {
        // set the IS_PRESSED flag on and grab the position
        IS_PRESSED = true;
        PRESS_X = event.getX ();
        PRESS_Y = event.getY ();

        // mode 1 stores touched positions
        if ( FORCE_MODE == 1 ) {
            DRAG_PATH_X = new ArrayList<> ();
            DRAG_PATH_Y = new ArrayList<> ();
            DRAG_PATH_X.add ( Float.valueOf ( PRESS_X ) );
            DRAG_PATH_Y.add ( Float.valueOf ( PRESS_Y ) );
        } else if ( FORCE_MODE == 2 ) {
            // just store this position and calculate difference for the force projection
            POS_1[ 0 ] = PRESS_X / (float) WIN_WIDTH;
            POS_1[ 1 ] = PRESS_Y / (float) WIN_HEIGHT;
        }
    }

    public void handleActionMove ( MotionEvent event ) {
        // check if screen is being touched now
        if ( ! IS_PRESSED ) {
            return;
        }

        PRESS_X = event.getX ();
        PRESS_Y = event.getY ();

        if ( DENSITY_MODE == 1 ) {
            int i = (int) ( ( PRESS_X * DEN_RESOLUTION ) / WIN_WIDTH );
            int j = (int) ( ( PRESS_Y * DEN_RESOLUTION ) / WIN_HEIGHT );

            if ( i >= 2 && i < DEN_RESOLUTION - 2 &&
                    j >= 2 && j < DEN_RESOLUTION - 2 ) {
                for ( int i0 = i - 1; i0 < i + 2; i0++ ) {
                    for ( int j0 = j - 2; j0 < j + 2; j0++ ) {
                        LEFuncs.DENSITY_FIELD[ i0 ][ j0 ] = 1.0f;
                    }
                }
            }

            float[][] forcePath = new float[ 2 ][ 4 ];
            forcePath[ 0 ][ 0 ] = 0.25f;
            forcePath[ 0 ][ 1 ] = 0.35f;
            forcePath[ 0 ][ 2 ] = - 0.015f * LEFuncs.FORCE_MAG;
            forcePath[ 0 ][ 3 ] = 0.02f * LEFuncs.FORCE_MAG;

            LEFuncs.stir ( forcePath );
        } else if ( FORCE_MODE == 1 ) {
            DRAG_PATH_X.add ( Float.valueOf ( PRESS_X ) );
            DRAG_PATH_Y.add ( Float.valueOf ( PRESS_Y ) );
        } else if ( FORCE_MODE == 2 ) {
            POS_2[ 0 ] = PRESS_X / WIN_WIDTH;
            POS_2[ 1 ] = PRESS_Y / WIN_HEIGHT;

            float[][] forcePath = new float[ 2 ][ 4 ];
            forcePath[ 0 ][ 0 ] = POS_1[ 0 ];
            forcePath[ 0 ][ 1 ] = POS_1[ 1 ];
            forcePath[ 0 ][ 2 ] = ( POS_2[ 0 ] - POS_1[ 0 ] ) * LEFuncs.FORCE_MAG;
            forcePath[ 0 ][ 3 ] = ( POS_2[ 1 ] - POS_1[ 1 ] ) * LEFuncs.FORCE_MAG;

            LEFuncs.stir ( forcePath );

            POS_1[ 0 ] = POS_2[ 0 ];
            POS_1[ 1 ] = POS_2[ 1 ];
        }
    }

    public void handleActionCancel ( MotionEvent event ) {

        IS_PRESSED = false;
    }

    public void handleActionUp ( MotionEvent event ) {
        if ( FORCE_MODE == 1 ) {
            float[][] forcePath = new float[ DRAG_PATH_X.size () ][ 4 ];

            Iterator<Float> iterX = DRAG_PATH_X.iterator ();
            Iterator<Float> iterY = DRAG_PATH_Y.iterator ();

            int idx = 0;
            while ( iterX.hasNext () ) {
                Float x = iterX.next ();
                Float y = iterY.next ();

                forcePath[ idx ][ 0 ] = x.floatValue () / (float) WIN_WIDTH;
                forcePath[ idx ][ 1 ] = y.floatValue () / (float) WIN_HEIGHT;
                idx++;
            }

            for ( int i = 0; i < forcePath.length - 1; i++ ) {
                forcePath[ i ][ 2 ] = ( forcePath[ i + 1 ][ 0 ] - forcePath[ i ][ 0 ] ) * LEFuncs.FORCE_MAG;
                forcePath[ i ][ 3 ] = ( forcePath[ i + 1 ][ 1 ] - forcePath[ i ][ 1 ] ) * LEFuncs.FORCE_MAG;
            }

            LEFuncs.stir ( forcePath );
        }

        IS_PRESSED = false;
        PRESS_X = event.getX ();
        PRESS_Y = event.getY ();
    }

}
