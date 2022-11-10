package com.joechamm.openglenvironment;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES32;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("SpellCheckingInspection")
public class ShaderHelper {

    private static final String TAG = "jcglenv:shaderhelper";

    public static String loadStringResource ( Context context, int resourceID ) {
        // recipe from 'OpenGL ES 2 for Android: A Quick-Start Guide' by Kevin Brothaler
        StringBuilder stringBuilder = new StringBuilder ();

        try {
            InputStream inputStream = context.getResources ().openRawResource ( resourceID );
            InputStreamReader inputStreamReader = new InputStreamReader ( inputStream );
            BufferedReader bufferedReader = new BufferedReader ( inputStreamReader );

            String nextLine;

            while ( ( nextLine = bufferedReader.readLine () ) != null ) {
                stringBuilder.append ( nextLine );
                stringBuilder.append ( '\n' );
            }
        } catch ( IOException e ) {
            Log.e ( TAG, "Could not open resource: " + resourceID, e );
            throw new RuntimeException ( "Could not open resource: " + resourceID, e );
        } catch ( Resources.NotFoundException notFoundException ) {
            Log.e ( TAG, "Resource not found: " + resourceID, notFoundException );
            throw new RuntimeException ( "Resource not found: " + resourceID, notFoundException );
        }

        return stringBuilder.toString ();
    }

    public static int compileVertexShader ( String shaderCode ) {
        Log.d ( TAG, "compileVertexShader called" );
        Log.v ( TAG, "Shader Code: \n" + shaderCode );
        return compileShader ( GLES32.GL_VERTEX_SHADER, shaderCode );
    }

    public static int compileFragmentShader ( String shaderCode ) {
        Log.d ( TAG, "compileFragmentShader called" );
        Log.v ( TAG, "Shader Code: \n" + shaderCode );
        return compileShader ( GLES32.GL_FRAGMENT_SHADER, shaderCode );
    }

    private static int compileShader ( final int shaderType, String shaderCode ) {

        final int handle = GLES32.glCreateShader ( shaderType );
        try {

            if ( 0 == handle ) {
                throw new RuntimeException ( "Invalid Shader Type" );
            }

            GLES32.glShaderSource ( handle, shaderCode );
            GLES32.glCompileShader ( handle );

            final int[] params = new int[ 1 ];

            GLES32.glGetShaderiv ( handle, GLES32.GL_COMPILE_STATUS, params, 0 );
            if ( GLES32.GL_FALSE == params[ 0 ] ) {
                String infoLog = getShaderInfoLog ( handle );
                throw new RuntimeException ( infoLog );
            }
        } catch ( RuntimeException e ) {
            Log.e ( TAG, "error compiling shader: " + e.getMessage (), e );
            GLES32.glDeleteShader ( handle );
            return 0;
        }

        return handle;
    }

    public static String getShaderInfoLog ( final int shaderHandle ) {
        return GLES32.glGetShaderInfoLog ( shaderHandle );
    }

    public static int createAndLinkProgram (
            final int vertexShaderHandle,
            final int fragmentShaderHandle,
            final String[] attributes
    ) {
        final int program = GLES32.glCreateProgram ();

        try {

            if ( 0 == vertexShaderHandle || 0 == fragmentShaderHandle ) {
                throw new RuntimeException ( "Invalid shader handle passed" );
            }

            if ( 0 == program ) {
                throw new RuntimeException ( "Error creating program" );
            }

            GLES32.glAttachShader ( program, vertexShaderHandle );
            GLES32.glAttachShader ( program, fragmentShaderHandle );

            // Bind attributes
            if ( null != attributes ) {
                final int len = attributes.length;
                for ( int i = 0; i < len; i++ ) {
                    GLES32.glBindAttribLocation ( program, i, attributes[ i ] );
                }
            }

            GLES32.glLinkProgram ( program );

            // check the status
            final int[] status = new int[ 1 ];
            GLES32.glGetProgramiv ( program, GLES32.GL_LINK_STATUS, status, 0 );
            if ( GLES32.GL_FALSE == status[ 0 ] ) {
                String infoLog = getProgramInfoLog ( program );

                throw new RuntimeException ( infoLog );
            } else {
                // our program is created and linked, so no need to keep the shaders
                GLES32.glDeleteShader ( vertexShaderHandle );
                GLES32.glDeleteShader ( fragmentShaderHandle );
            }

        } catch ( Exception e ) {
            Log.e ( TAG, "error linking program: " + e.getMessage (), e );
            GLES32.glDeleteShader ( vertexShaderHandle );
            GLES32.glDeleteShader ( fragmentShaderHandle );
            GLES32.glDeleteProgram ( program );
            return 0;
        }

        return program;
    }

    public static String getProgramInfoLog ( final int program ) {
        return GLES32.glGetProgramInfoLog ( program );
    }

    public static int getUniformLocation ( final int program, final String uniformName ) {
        return GLES32.glGetUniformLocation ( program, uniformName );
    }

    public static int getAttributeLocation ( final int program, final String attributeName ) {
        return GLES32.glGetAttribLocation ( program, attributeName );
    }

    public static boolean validateShaderProgram ( final int program ) {
        GLES32.glValidateProgram ( program );
        final int[] validated = new int[ 1 ];
        GLES32.glGetProgramiv ( program, GLES32.GL_VALIDATE_STATUS, validated, 0 );
        String infoLog = GLES32.glGetProgramInfoLog ( program );
        String validationStatus;
        if ( validated[ 0 ] == GLES32.GL_TRUE ) {
            validationStatus = "GL_TRUE";
        } else {
            validationStatus = "GL_FALSE";
        }

        Log.d ( TAG, "Shader Program Validation Status: " + validationStatus );
        Log.d ( TAG, "Shader Program Info Log:\n" + infoLog );
        return validated[ 0 ] != 0;
    }
}
