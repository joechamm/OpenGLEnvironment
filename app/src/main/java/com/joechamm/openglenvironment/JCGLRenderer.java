package com.joechamm.openglenvironment;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class JCGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "jcglenv:renderer";

    private Triangle mTriangle;
    private Square mSquare;

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] vPMatrix = new float[ 16 ];
    private final float[] projctionMatrix = new float[ 16 ];
    private final float[] viewMatrix = new float[ 16 ];
    private final float[] rotationMatrix = new float[ 16 ];

    // rotation angle
    public volatile float mAngle;

    @Override
    public void onSurfaceCreated ( GL10 gl, EGLConfig config ) {
        // DEBUGGING
        Log.d ( TAG, "onSurfaceCreate called" );

        // Set the background frame color
        GLES20.glClearColor ( 0.0f, 0.0f, 0.0f, 1.0f );

        // initialize triangle
        mTriangle = new Triangle ();

        // initialize square
        mSquare = new Square ();
    }

    @Override
    public void onSurfaceChanged ( GL10 gl, int width, int height ) {
        // DEBUGGING
        Log.d ( TAG, "onSurfaceChanged called" );
        GLES20.glViewport ( 0, 0, width, height );

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates in the onDrawFrame() method
        Matrix.frustumM ( projctionMatrix, 0, - ratio, ratio, - 1, 1, 3, 7 );
    }

    @Override
    public void onDrawFrame ( GL10 gl ) {
        // TODO: frame rate counter
        // Redraw background color
        GLES20.glClear ( GLES20.GL_COLOR_BUFFER_BIT );

        float[] scratch = new float[ 16 ];

        // Set the camera position (View matrix)
        Matrix.setLookAtM ( viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f );

        // Calculate the projection and view transformation
        Matrix.multiplyMM ( vPMatrix, 0, projctionMatrix, 0, viewMatrix, 0 );

        // create a rotation transformation for the triangle
        //long time = SystemClock.uptimeMillis () % 4000L;
        //float angle = 0.090f * ( (int) time );
        Matrix.setRotateM ( rotationMatrix, 0, mAngle, 0, 0, - 1.0f );

        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order for the matrix multiplciation product to be correct.
        Matrix.multiplyMM ( scratch, 0, vPMatrix, 0, rotationMatrix, 0 );

        // Draw the triangle
        mTriangle.draw ( scratch );

        // mSquare.draw();
    }

    public float getAngle () {
        return mAngle;
    }

    public void setAngle ( float angle ) {
        mAngle = angle;
    }

    public static int loadShader ( int type, String shaderCode ) {
        // DEBUGGING
        Log.d ( TAG, "loadShader called" );

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader ( type );

        // add the source code to the shader and compile it
        GLES20.glShaderSource ( shader, shaderCode );
        GLES20.glCompileShader ( shader );

        return shader;
    }
}
