package com.joechamm.openglenvironment;

import android.opengl.GLES32;
import android.util.Log;

@SuppressWarnings("SpellCheckingInspection")
public class ShaderHelper {

    private static final String TAG = "jcglenv:shaderhelper";

    enum ShaderType {
        SHADER_TYPE_VERTEX,
        SHADER_TYPE_FRAGMENT,
        SHADER_TYPE_GEOMETRY,
        SHADER_TYPE_TESS_CONTROL,
        SHADER_TYPE_TESS_EVAL,
        SHADER_TYPE_COMPUTE,
        SHADER_TYPE_COUNT
    }

    class ShaderTypeInit {
        public boolean isCompute = false;
        public boolean hasGeometryShader = false;
        public boolean hasTessShaders = false;

        public int[] shaderHandles = new int[ 6 ];
    }

//    // ShaderHandles abstract class holds the handles created by glCreateShader(), before they hav
//    abstract class ShaderHandles {
//        abstract boolean isValid ();
//        abstract boolean isRenderType();
//        abstract boolean isComputeType();
//        abstract boolean hasType(ShaderType type);
//        abstract int getHandle(ShaderType type);
//    }
//
//    class ComputeShaderHandles extends ShaderHandles {
//
//        private int mComputeShaderHandle;
//
//        private ComputeShaderHandles(int handle) {
//            mComputeShaderHandle = handle;
//        }
//
//        @Override
//        boolean isValid () {
//            return GLES32.glIsShader ( mComputeShaderHandle );
//        }
//
//        @Override
//        boolean isRenderType () {
//            return false;
//        }
//
//        @Override
//        boolean isComputeType () {
//            return true;
//        }
//
//        boolean hasType(ShaderType type) {
//            return ( type == ShaderType.SHADER_TYPE_COMPUTE );
//        }
//
//        ShaderType getType () {
//            return ShaderType.SHADER_TYPE_COMPUTE;
//        }
//
//        int getHandle(ShaderType type) {
//            if ( ShaderType.SHADER_TYPE_COMPUTE == type ) {
//                return mComputeShaderHandle;
//            }
//            return -1;
//        }
//    }
//
//    class RenderShaderHandles extends ShaderHandles {
//        private int mVertexShaderHandle;
//        private int mFragmentShaderHandle;
//        private int mGeometryShaderHandle;
//        private int mTessControlShaderHandle;
//        private int mTessEvalShaderHandle;
//
//        private RenderShaderHandles (
//                int vertexShaderHandle,
//                int fragmentShaderHandle,
//                int geometryShaderHandle,
//                int tessControlShaderHandle,
//                int tessEvalShaderHandle
//        ) {
//
//
//        }
//
//        @Override
//        boolean isValid () {
//            return false;
//        }
//
//        @Override
//        boolean isRenderType () {
//            return false;
//        }
//
//        @Override
//        boolean isComputeType () {
//            return false;
//        }
//
//        @Override
//        boolean hasType ( ShaderType type ) {
//            return false;
//        }
//
//        @Override
//        int getHandle ( ShaderType type ) {
//            return 0;
//        }
//    }

//    public static String loadStringResource ( Context context, int resourceID ) {
//        // recipe from 'OpenGL ES 2 for Android: A Quick-Start Guide' by Kevin Brothaler
//        StringBuilder stringBuilder = new StringBuilder ();
//
//        try {
//            InputStream inputStream = context.getResources ().openRawResource ( resourceID );
//            InputStreamReader inputStreamReader = new InputStreamReader ( inputStream );
//            BufferedReader bufferedReader = new BufferedReader ( inputStreamReader );
//
//            String nextLine;
//
//            while ( ( nextLine = bufferedReader.readLine () ) != null ) {
//                stringBuilder.append ( nextLine );
//                stringBuilder.append ( '\n' );
//            }
//        } catch ( IOException e ) {
//            Log.e ( TAG, "Could not open resource: " + resourceID, e );
//            throw new RuntimeException ( "Could not open resource: " + resourceID, e );
//        } catch ( Resources.NotFoundException notFoundException ) {
//            Log.e ( TAG, "Resource not found: " + resourceID, notFoundException );
//            throw new RuntimeException ( "Resource not found: " + resourceID, notFoundException );
//        }
//
//        return stringBuilder.toString ();
//    }

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

    public static int compileGeometryShader ( String shaderCode ) {
        Log.d ( TAG, "compileGeometryShader called" );
        Log.v ( TAG, "Shader Code: \n" + shaderCode );
        return compileShader ( GLES32.GL_GEOMETRY_SHADER, shaderCode );
    }

    public static int compileTessEvaluationShader ( String shaderCode ) {
        Log.d ( TAG, "compileTessEvaluationShader called" );
        Log.v ( TAG, "Shader Code: \n" + shaderCode );
        return compileShader ( GLES32.GL_TESS_EVALUATION_SHADER, shaderCode );
    }

    public static int compileTessControlShader ( String shaderCode ) {
        Log.d ( TAG, "compileTessControlShader called" );
        Log.v ( TAG, "Shader Code: \n" + shaderCode );
        return compileShader ( GLES32.GL_TESS_CONTROL_SHADER, shaderCode );
    }

    public static int compileComputeShader ( String shaderCode ) {
        Log.d ( TAG, "compileComputeShader called" );
        Log.v ( TAG, "Shader Code: \n" + shaderCode );
        return compileShader ( GLES32.GL_COMPUTE_SHADER, shaderCode );
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

    public static int createAndLinkComputeProgram ( final int computeShader ) {
        final int program = GLES32.glCreateProgram ();

        try {
            if ( 0 == computeShader ) {
                throw new RuntimeException ( "Invalid compute shader handle passed" );
            }

            if ( 0 == program ) {
                throw new RuntimeException ( "Error creating program" );
            }

            GLES32.glAttachShader ( program, computeShader );

            GLES32.glLinkProgram ( program );

            // check the status
            final int[] status = new int[ 1 ];
            GLES32.glGetProgramiv ( program, GLES32.GL_LINK_STATUS, status, 0 );
            if ( GLES32.GL_FALSE == status[ 0 ] ) {
                String infoLog = getProgramInfoLog ( program );

                throw new RuntimeException ( infoLog );
            } else {
                // our program is created and linked, so no need to keep the shaders
                GLES32.glDeleteShader ( computeShader );
            }
        } catch ( Exception e ) {
            Log.e ( TAG, "error linking program: " + e.getMessage (), e );
            GLES32.glDeleteShader ( computeShader );
            GLES32.glDeleteProgram ( program );
            return 0;
        }

        return program;
    }

    // vertex, fragment, and geometry shaders
    public static int createAndLinkRenderProgram (
            final int vertexShaderHandle,
            final int fragmentShaderHandle,
            final int geometryShaderHandle,
            final String[] attributes
    ) {
        final int program = GLES32.glCreateProgram ();

        try {

            if ( 0 == vertexShaderHandle
                    || 0 == fragmentShaderHandle
                    || 0 == geometryShaderHandle ) {
                throw new RuntimeException ( "Invalid shader handle passed" );
            }

            if ( 0 == program ) {
                throw new RuntimeException ( "Error creating program" );
            }

            GLES32.glAttachShader ( program, vertexShaderHandle );
            GLES32.glAttachShader ( program, fragmentShaderHandle );
            GLES32.glAttachShader ( program, geometryShaderHandle );

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
                GLES32.glDeleteShader ( geometryShaderHandle );
            }

        } catch ( Exception e ) {
            Log.e ( TAG, "error linking program: " + e.getMessage (), e );
            GLES32.glDeleteShader ( vertexShaderHandle );
            GLES32.glDeleteShader ( fragmentShaderHandle );
            GLES32.glDeleteShader ( geometryShaderHandle );
            GLES32.glDeleteProgram ( program );
            return 0;
        }

        return program;
    }

    // vertex, fragment, tessControl, tessEval
    public static int createAndLinkRenderProgram (
            final int vertexShaderHandle,
            final int fragmentShaderHandle,
            final int tessControlShaderHandle,
            final int tessEvalShaderhandle,
            final String[] attributes
    ) {
        final int program = GLES32.glCreateProgram ();

        try {

            if ( 0 == vertexShaderHandle
                    || 0 == fragmentShaderHandle
                    || 0 == tessControlShaderHandle
                    || 0 == tessEvalShaderhandle ) {
                throw new RuntimeException ( "Invalid shader handle passed" );
            }

            if ( 0 == program ) {
                throw new RuntimeException ( "Error creating program" );
            }

            GLES32.glAttachShader ( program, vertexShaderHandle );
            GLES32.glAttachShader ( program, fragmentShaderHandle );
            GLES32.glAttachShader ( program, tessControlShaderHandle );
            GLES32.glAttachShader ( program, tessEvalShaderhandle );

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
                GLES32.glDeleteShader ( tessControlShaderHandle );
                GLES32.glDeleteShader ( tessEvalShaderhandle );
            }

        } catch ( Exception e ) {
            Log.e ( TAG, "error linking program: " + e.getMessage (), e );
            GLES32.glDeleteShader ( vertexShaderHandle );
            GLES32.glDeleteShader ( fragmentShaderHandle );
            GLES32.glDeleteShader ( tessControlShaderHandle );
            GLES32.glDeleteShader ( tessEvalShaderhandle );
            GLES32.glDeleteProgram ( program );
            return 0;
        }

        return program;
    }

    // vertex, fragment, geometry, tessControl, tessEval
    public static int createAndLinkRenderProgram (
            final int vertexShaderHandle,
            final int fragmentShaderHandle,
            final int geometryShaderHandle,
            final int tessControlShaderHandle,
            final int tessEvalShaderhandle,
            final String[] attributes
    ) {
        final int program = GLES32.glCreateProgram ();

        try {

            if ( 0 == vertexShaderHandle
                    || 0 == fragmentShaderHandle
                    || 0 == geometryShaderHandle
                    || 0 == tessControlShaderHandle
                    || 0 == tessEvalShaderhandle ) {
                throw new RuntimeException ( "Invalid shader handle passed" );
            }

            if ( 0 == program ) {
                throw new RuntimeException ( "Error creating program" );
            }

            GLES32.glAttachShader ( program, vertexShaderHandle );
            GLES32.glAttachShader ( program, fragmentShaderHandle );
            GLES32.glAttachShader ( program, geometryShaderHandle );
            GLES32.glAttachShader ( program, tessControlShaderHandle );
            GLES32.glAttachShader ( program, tessEvalShaderhandle );

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
                GLES32.glDeleteShader ( geometryShaderHandle );
                GLES32.glDeleteShader ( tessControlShaderHandle );
                GLES32.glDeleteShader ( tessEvalShaderhandle );
            }

        } catch ( Exception e ) {
            Log.e ( TAG, "error linking program: " + e.getMessage (), e );
            GLES32.glDeleteShader ( vertexShaderHandle );
            GLES32.glDeleteShader ( fragmentShaderHandle );
            GLES32.glDeleteShader ( geometryShaderHandle );
            GLES32.glDeleteShader ( tessControlShaderHandle );
            GLES32.glDeleteShader ( tessEvalShaderhandle );
            GLES32.glDeleteProgram ( program );
            return 0;
        }

        return program;
    }

    // create and link a program using the supplied handles and attributes
    public static int createAndLinkProgram (
            final ShaderTypeInit shaderTypeInit,
            final String[] attributes
    ) {

        final int vertIdx = 0;  // SHADER_TYPE_VERTEX
        final int fragIdx = 1;  // SHADER_TYPE_FRAGMENT
        final int geomIdx = 2;  // SHADER_TYPE_GEOMETRY
        final int tcIdx = 3;    // SHADER_TYPE_TESS_CONTROL
        final int teIdx = 4;    // SHADER_TYPE_TESS_EVAL
        final int compIdx = 5;  // SHADER_TYPE_COMPUTE

        if ( shaderTypeInit.isCompute ) {
            return createAndLinkComputeProgram ( shaderTypeInit.shaderHandles[ compIdx ] );
        } else {
            int vs = shaderTypeInit.shaderHandles[ vertIdx ];
            int fs = shaderTypeInit.shaderHandles[ fragIdx ];

            if ( shaderTypeInit.hasGeometryShader ) {
                int gs = shaderTypeInit.shaderHandles[ geomIdx ];

                if ( shaderTypeInit.hasTessShaders ) {
                    int tc = shaderTypeInit.shaderHandles[ tcIdx ];
                    int te = shaderTypeInit.shaderHandles[ teIdx ];

                    return createAndLinkRenderProgram ( vs, fs, gs, tc, te, attributes );
                }

                return createAndLinkRenderProgram ( vs, fs, gs, attributes );
            }

            if ( shaderTypeInit.hasTessShaders ) {
                int tc = shaderTypeInit.shaderHandles[ tcIdx ];
                int te = shaderTypeInit.shaderHandles[ teIdx ];

                return createAndLinkRenderProgram ( vs, fs, tc, te, attributes );
            }

            return createAndLinkProgram ( vs, fs, attributes );
        }
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

    public static String[] getProgramActiveAttribs ( final int program ) {
        int[] numAttribs = new int[ 1 ];
        int[] attribSize = new int[ 1 ];
        int[] attribType = new int[ 1 ];
        GLES32.glGetProgramiv ( program, GLES32.GL_ACTIVE_ATTRIBUTES, numAttribs, 0 );

        int attribCount = numAttribs[ 0 ];

        String[] activeAttribs = new String[ attribCount ];

        for ( int i = 0; i < attribCount; i++ ) {
            activeAttribs[ i ] = GLES32.glGetActiveAttrib ( program, i, attribSize, 0, attribType, 0 );
        }

        return activeAttribs;
    }

    public static void logProgramActiveAttribs ( final int program ) {
        String[] activeAttribs = getProgramActiveAttribs ( program );

        String preamble = "Shader Program ";
        if ( JCGLDebugger.DEBUGGING ) {
            String progLabel = JCGLDebugger.getProgramLabel ( program );
            preamble.concat ( progLabel );
        } else {
            preamble.concat ( String.valueOf ( program ) );
        }

        preamble.concat ( " has " + activeAttribs.length + " active attribs" );

        Log.d ( TAG, preamble );
        for ( String attribStr : activeAttribs ) {
            Log.d ( TAG, attribStr );
        }
    }

    public static String[] getProgramActiveUniforms ( final int program ) {
        int[] numUniforms = new int[ 1 ];
        int[] uniformSize = new int[ 1 ];
        int[] uniformType = new int[ 1 ];

        GLES32.glGetProgramiv ( program, GLES32.GL_ACTIVE_UNIFORMS, numUniforms, 0 );

        int uniformCount = numUniforms[ 0 ];

        String[] activeUniforms = new String[ uniformCount ];

        for ( int i = 0; i < uniformCount; i++ ) {
            activeUniforms[ i ] = GLES32.glGetActiveUniform ( program, i,
                                                              uniformSize, 0,
                                                              uniformType, 0 );

        }

        return activeUniforms;
    }

    public static void logProgramActiveUniforms ( final int program ) {
        String[] activeUniforms = getProgramActiveUniforms ( program );

        String preamble = "Shader Program ";
        if ( JCGLDebugger.DEBUGGING ) {
            String progLabel = JCGLDebugger.getProgramLabel ( program );
            preamble.concat ( progLabel );
        } else {
            preamble.concat ( String.valueOf ( program ) );
        }

        preamble.concat ( " has " + activeUniforms.length + " active uniforms" );

        Log.d ( TAG, preamble );
        for ( String uniStr : activeUniforms ) {
            Log.d ( TAG, uniStr );
        }
    }
}
