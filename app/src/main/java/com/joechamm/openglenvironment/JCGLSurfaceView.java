package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class JCGLSurfaceView extends GLSurfaceView {

    private final JCGLRenderer mRenderer;

    private final String TAG = "jcglenv:surfaceview";

    // TODO: handle multiple context versions
    private final int EGL_CTX_VERSION_MAJ = 2;

    public JCGLSurfaceView ( Context context ) {
        super ( context );

        // Create an OpenGL ES context
        setEGLContextClientVersion ( EGL_CTX_VERSION_MAJ );

        mRenderer = new JCGLRenderer ();

        // Set the renderer for drawing on the GLSurfaceView
        setRenderer ( mRenderer );
    }
}
