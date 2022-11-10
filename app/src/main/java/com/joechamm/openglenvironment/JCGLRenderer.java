package com.joechamm.openglenvironment;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class JCGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "jcglenv:renderer";

    // reference to current context (activity)
    private final Context mContext;

    private Triangle mTriangle;
    private Square mSquare;

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    //   private final float[] vPMatrix = new float[ 16 ];
    //   private final float[] projctionMatrix = new float[ 16 ];
    //   private final float[] viewMatrix = new float[ 16 ];
    //   private final float[] rotationMatrix = new float[ 16 ];

    private PerFrameUniforms mPerFrameUniforms;

    // rotation angle
    public volatile float mAngle;

    JCGLRenderer ( Context context ) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated ( GL10 gl, EGLConfig config ) {
        // DEBUGGING
        Log.d ( TAG, "onSurfaceCreate called" );

        JCGLContextHelper.logGLVersion ( gl );
        JCGLContextHelper.logGLVendor ( gl );
        JCGLContextHelper.logGLRenderer ( gl );
        JCGLContextHelper.logGLExtensions ( gl );

        // Set the background frame color
        GLES32.glClearColor ( 0.0f, 0.0f, 0.0f, 1.0f );

        // Enable depth testing
        GLES32.glEnable ( GLES32.GL_DEPTH_TEST );

        // initialize uniform buffer
        mPerFrameUniforms = new PerFrameUniforms ();

        // initialize triangle
        mTriangle = new Triangle ( mContext );

        // initialize square
        mSquare = new Square ( mContext );
    }

    @Override
    public void onSurfaceChanged ( GL10 gl, int width, int height ) {
        // DEBUGGING
        Log.d ( TAG, "onSurfaceChanged called" );
        GLES32.glViewport ( 0, 0, width, height );

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates in the onDrawFrame() method
        //     Matrix.frustumM ( projctionMatrix, 0, - ratio, ratio, - 1, 1, 3, 7 );
        //     Matrix.f
        mPerFrameUniforms.setProjFrustum ( - ratio, ratio, - 1, 1, 3, 7 );

        // DEBUGGING
        JCGLDebugger.checkGLError ( "glViewport" );
    }

    @Override
    public void onDrawFrame ( GL10 gl ) {
        // TODO: frame rate counter
        // Redraw background color
        JCGLDebugger.checkGLError ( "onDrawFrame: before glClear" );

        GLES32.glClear ( GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT );

        JCGLDebugger.checkGLError ( "onDrawFrame: after glClear, before mSquare.draw" );

        //    float[] scratch = new float[ 16 ];

        // Draw the square background first

        // Set the camera position (View matrix)
        //    Matrix.setLookAtM ( viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f );
        float eyeX = 0.0f;
        float eyeY = 0.0f;
        float eyeZ = 3.0f;
        float targetX = 0.0f;
        float targetY = 0.0f;
        float targetZ = 0.0f;
        float upX = 0.0f;
        float upY = 1.0f;
        float upZ = 0.0f;

        mPerFrameUniforms.setViewLookAt ( eyeX, eyeY, eyeZ, targetX, targetY, targetZ, upX, upY, upZ );

        mSquare.rotate ( mAngle, 0.0f, 0.0f, - 1.0f );

        mSquare.draw ( mPerFrameUniforms );


        // Calculate the projection and view transformations
        //     Matrix.multiplyMM ( vPMatrix, 0, projctionMatrix, 0, viewMatrix, 0 );

        // create a rotation transformation for the square
        //    Matrix.setRotateM ( rotationMatrix, 0, mAngle, 0, 0, - 1.0f );

        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order for the matrix multiplciation product to be correct.
        //    Matrix.multiplyMM ( scratch, 0, vPMatrix, 0, rotationMatrix, 0 );
        //    mSquare.draw ( scratch );

        JCGLDebugger.checkGLError ( "onDrawFrame: after mSquare.draw, before mTriangle.draw" );

        // Now draw the triangle on top
        // rotate the triangle at a constant rate
        long time = SystemClock.uptimeMillis () % 4000L;
        float angle = 0.090f * ( (int) time );
        // we need to reset the rotation matrix for the triangle
        //    Matrix.setRotateM ( rotationMatrix, 0, angle, 0, 0, - 1.0f );

        // the vPMMatrix hasn't changed, so now we just multiply vPMatrix * rotationMatrix and store the result in scratch
        //    Matrix.multiplyMM ( scratch, 0, vPMatrix, 0, rotationMatrix, 0 );

        mTriangle.rotate ( angle, 0.0f, 0.0f, - 1.0f );
        mTriangle.draw ( mPerFrameUniforms );

        // Draw the triangle now
        //    mTriangle.draw ( scratch );

        JCGLDebugger.checkGLError ( "onDrawFrame: after mTriangle.draw" );

        // DEBUGGING
        //   JCGLDebugger.checkGLError ( "onDrawFrame" );
    }

    public float getAngle () {
        return mAngle;
    }

    public void setAngle ( float angle ) {
        mAngle = angle;
    }

}
