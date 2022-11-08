package com.joechamm.openglenvironment;

import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.Set;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

public class JCEGLConfigChooser implements GLSurfaceView.EGLConfigChooser {

    private static final String TAG = "jcglenv:cfgchooser";

    // TODO: Weighted choice method
    enum ChooseMethod {
        FIRST_AVAILABLE,
        METHOD_COUNT
    }

    public static final ChooseMethod CHOOSE_METHOD = ChooseMethod.FIRST_AVAILABLE;

    private int mRedBits;
    private int mGreenBits;
    private int mBlueBits;
    private int mAlphaBits;
    private int mDepthBits;
    private int mStencilBits;
    private int mSampleBuffers;
    private int mSamples;

    private static final int EGL_OPENGL_ES2_BIT = 4;
    private static final int[] sConfigMinAttribs = {
            EGL10.EGL_RED_SIZE, 5,
            EGL10.EGL_GREEN_SIZE, 6,
            EGL10.EGL_BLUE_SIZE, 5,
            EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
            EGL10.EGL_NONE
    };

    public JCEGLConfigChooser (
            int red, int green, int blue, int alpha,
            int depth, int stencil,
            int sampleBuffers, int samples
    ) {
        mRedBits = red;
        mGreenBits = green;
        mBlueBits = blue;
        mAlphaBits = alpha;
        mDepthBits = depth;
        mStencilBits = stencil;
        mSampleBuffers = sampleBuffers;
        mSamples = samples;
    }

    @Override
    public EGLConfig chooseConfig (
            EGL10 egl, EGLDisplay display
    ) {
        int[] availableConfigs = new int[ 1 ];
        egl.eglChooseConfig ( display, sConfigMinAttribs, null, 0, availableConfigs );

        int numAvailableConfigs = availableConfigs[ 0 ];

        if ( numAvailableConfigs < 1 ) {
            throw new RuntimeException ( "no compatible EGLConfig available for this device" );
        }

        EGLConfig[] configs = new EGLConfig[ numAvailableConfigs ];

        egl.eglChooseConfig ( display, sConfigMinAttribs, configs, numAvailableConfigs, availableConfigs );

        if ( BuildConfig.IS_DEBUGGING ) {
            //    logAvailableConfigs(egl, display, configs);
        }

        EGLConfig cfg = null;

        if ( CHOOSE_METHOD == ChooseMethod.FIRST_AVAILABLE ) {
            cfg = chooseFirstAvailableConfig ( egl, display, configs );
        }

/*        if(CHOOSE_METHOD == ChooseMethod.FIRST_AVAILABLE) {
            return chooseFirstAvailableConfig ( egl, display, configs );
        } else {
            return null;
        }*/

        if ( BuildConfig.IS_DEBUGGING ) {
            Log.d ( TAG, "Logging EGLConfig chosen..." );
            logConfig ( egl, display, cfg );
        }

        return cfg;
    }

    public EGLConfig chooseFirstAvailableConfig ( EGL10 egl, EGLDisplay display, EGLConfig[] configs ) {
        for ( EGLConfig cfg : configs ) {
            int depthSize = getAttribValue ( egl, display, cfg, EGL10.EGL_DEPTH_SIZE, 0 );
            int stencilSize = getAttribValue ( egl, display, cfg, EGL10.EGL_STENCIL_SIZE, 0 );

            if ( depthSize < mDepthBits || stencilSize < mStencilBits ) {
                continue;
            }

            int redSize = getAttribValue ( egl, display, cfg, EGL10.EGL_RED_SIZE, 0 );
            int greenSize = getAttribValue ( egl, display, cfg, EGL10.EGL_GREEN_SIZE, 0 );
            int blueSize = getAttribValue ( egl, display, cfg, EGL10.EGL_BLUE_SIZE, 0 );
            int alphaSize = getAttribValue ( egl, display, cfg, EGL10.EGL_ALPHA_SIZE, 0 );
            int sampleBuffers = getAttribValue ( egl, display, cfg, EGL10.EGL_SAMPLE_BUFFERS, 0 );
            int samples = getAttribValue ( egl, display, cfg, EGL10.EGL_SAMPLES, 0 );

            if ( redSize >= mRedBits && greenSize >= mGreenBits && blueSize >= mBlueBits
                    && alphaSize >= mAlphaBits && sampleBuffers >= mSampleBuffers && samples >= mSamples ) {
                return cfg;
            }
        }

        return null;
    }

    private int getAttribValue ( EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue ) {
        int[] attribValue = new int[ 1 ];
        if ( egl.eglGetConfigAttrib ( display, config, attribute, attribValue ) ) {
            return attribValue[ 0 ];
        }
        return defaultValue;
    }

    private void logAvailableConfigs ( EGL10 egl, EGLDisplay display, EGLConfig[] configs ) {
        int numConfigs = configs.length;
        Log.d ( TAG, "Found " + numConfigs + " available configs" );
        for ( int i = 0; i < numConfigs; i++ ) {
            Log.d ( TAG, " Configuration " + i + ":\n" );
            logConfig ( egl, display, configs[ i ] );
        }
    }

    private void logConfig ( EGL10 egl, EGLDisplay display, EGLConfig config ) {
        Set<Integer> eglAttributes = JCGLContextHelper.getEGLAttributes ();

        int[] attribValue = new int[ 1 ];
        for ( Integer eglAttrib : eglAttributes ) {
            int attribKey = eglAttrib.intValue ();
            String attribName = JCGLContextHelper.getEGLAttributeString ( attribKey );
            if ( egl.eglGetConfigAttrib ( display, config, attribKey, attribValue ) ) {
                Log.d ( TAG, String.format ( " %s: %d\n", attribName, attribValue[ 0 ] ) );
            } else {
                int err = egl.eglGetError ();
                while ( EGL10.EGL_SUCCESS != err ) {
                    String errString = JCGLContextHelper.getEGLErrorString ( err );
                    Log.e ( TAG, "EGL ERR: " + errString );
                    err = egl.eglGetError ();
                }
            }
        }
    }
}
