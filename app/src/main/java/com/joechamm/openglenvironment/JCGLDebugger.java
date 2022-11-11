package com.joechamm.openglenvironment;

import android.opengl.GLES32;
import android.opengl.GLU;
import android.util.Log;

import androidx.annotation.NonNull;

public class JCGLDebugger
        implements GLES32.DebugProc {

    // TODO: handle debugging on/off later
    public static final boolean DEBUGGING = BuildConfig.IS_DEBUGGING;

    private static final String TAG = "jcglenv:debugger";

    private static int MAX_LOG_MESSAGES = - 1;

    public static void checkGLError ( String op ) {
        int error;
        while ( ( error = GLES32.glGetError () ) != GLES32.GL_NO_ERROR ) {
            String errStr = GLU.gluErrorString ( error );
            Log.e ( TAG, op + ": glError " + errStr );
        }
    }

    private static String[] getFirstNDebugMessages ( int numMsgs ) {
        if ( MAX_LOG_MESSAGES < 0 ) {
            int[] maxDebugLoggedMessages = new int[ 1 ];
            GLES32.glGetIntegerv ( GLES32.GL_MAX_DEBUG_LOGGED_MESSAGES, maxDebugLoggedMessages, 0 );
            MAX_LOG_MESSAGES = maxDebugLoggedMessages[ 0 ];
        }

        if ( numMsgs > MAX_LOG_MESSAGES ) {
            numMsgs = MAX_LOG_MESSAGES;
        }

        int[] maxMsgLen = new int[ 1 ];
        GLES32.glGetIntegerv ( GLES32.GL_MAX_DEBUG_MESSAGE_LENGTH, maxMsgLen, 0 );

        int buffSize = numMsgs * maxMsgLen[ 0 ];
        int[] msgSrcs = new int[ numMsgs ];
        int[] msgTypes = new int[ numMsgs ];
        int[] msgIds = new int[ numMsgs ];
        int[] msgSevs = new int[ numMsgs ];
        int[] msgLens = new int[ numMsgs ];
        byte[] msgLog = new byte[ buffSize ];


        int numFound = GLES32.glGetDebugMessageLog ( numMsgs, buffSize, msgSrcs, 0,
                                                     msgTypes, 0, msgIds, 0,
                                                     msgSevs, 0, msgLens, 0,
                                                     msgLog, 0 );

        String[] debugMessages = GLES32.glGetDebugMessageLog ( numFound, msgSrcs, 0,
                                                               msgTypes, 0, msgIds, 0,
                                                               msgSevs, 0 );

        return debugMessages;
    }

    public static void logFirstNDebugMessages ( int numMsgs ) {
        String[] debugMessages = getFirstNDebugMessages ( numMsgs );

        for ( String str : debugMessages ) {
            Log.d ( TAG, str );
        }
    }

    private static void setObjectLabel ( int identifier, int name, int length, String label ) {
        GLES32.glObjectLabel ( identifier, name, length, label );
    }

    public static void setBufferLabel ( int name, @NonNull String label ) {
        int labelLength = label.length ();
        setObjectLabel ( GLES32.GL_BUFFER, name, labelLength, label );
    }

    public static void setShaderLabel ( int name, @NonNull String label ) {
        int labelLength = label.length ();
        setObjectLabel ( GLES32.GL_SHADER, name, labelLength, label );
    }

    public static void setProgramLabel ( int name, @NonNull String label ) {
        int labelLength = label.length ();
        setObjectLabel ( GLES32.GL_PROGRAM, name, labelLength, label );
    }

    public static void setVertexArrayLabel ( int name, @NonNull String label ) {
        int labelLength = label.length ();
        setObjectLabel ( GLES32.GL_VERTEX_ARRAY, name, labelLength, label );
    }

    public static void setQueryLabel ( int name, @NonNull String label ) {
        int labelLength = label.length ();
        setObjectLabel ( GLES32.GL_QUERY, name, labelLength, label );
    }

    public static void setProgramPipelineLabel ( int name, @NonNull String label ) {
        int labelLength = label.length ();
        setObjectLabel ( GLES32.GL_PROGRAM_PIPELINE, name, labelLength, label );
    }

    public static void setTransformFeedbackLabel ( int name, @NonNull String label ) {
        int labelLength = label.length ();
        setObjectLabel ( GLES32.GL_TRANSFORM_FEEDBACK, name, labelLength, label );
    }

    public static void setSamplerLabel ( int name, @NonNull String label ) {
        int labelLength = label.length ();
        setObjectLabel ( GLES32.GL_SAMPLER, name, labelLength, label );
    }

    public static void setTextureLabel ( int name, @NonNull String label ) {
        int labelLength = label.length ();
        setObjectLabel ( GLES32.GL_TEXTURE, name, labelLength, label );
    }

    public static void setRenderbufferLabel ( int name, @NonNull String label ) {
        int labelLength = label.length ();
        setObjectLabel ( GLES32.GL_RENDERBUFFER, name, labelLength, label );
    }

    public static void setFramebufferLabel ( int name, @NonNull String label ) {
        int labelLength = label.length ();
        setObjectLabel ( GLES32.GL_FRAMEBUFFER, name, labelLength, label );
    }

    private static String getObjectLabel ( int identifier, int name ) {
        return GLES32.glGetObjectLabel ( identifier, name );
    }

    public static String getBufferLabel ( int name ) {
        return getObjectLabel ( GLES32.GL_BUFFER, name );
    }

    public static String getShaderLabel ( int name ) {
        return getObjectLabel ( GLES32.GL_SHADER, name );
    }

    public static String getProgramLabel ( int name ) {
        return getObjectLabel ( GLES32.GL_PROGRAM, name );
    }

    public static String getProgramPipelineLabel ( int name ) {
        return getObjectLabel ( GLES32.GL_PROGRAM_PIPELINE, name );
    }

    public static String getVertexArrayLabel ( int name ) {
        return getObjectLabel ( GLES32.GL_VERTEX_ARRAY, name );
    }

    public static String getQueryLabel ( int name ) {
        return getObjectLabel ( GLES32.GL_QUERY, name );
    }

    public static String getTransformFeedbackLabel ( int name ) {
        return getObjectLabel ( GLES32.GL_TRANSFORM_FEEDBACK, name );
    }

    public static String getSamplerLabel ( int name ) {
        return getObjectLabel ( GLES32.GL_SAMPLER, name );
    }

    public static String getTextureLabel ( int name ) {
        return getObjectLabel ( GLES32.GL_TEXTURE, name );
    }

    public static String getRenderbufferLabel ( int name ) {
        return getObjectLabel ( GLES32.GL_RENDERBUFFER, name );
    }

    public static String getFramebufferLabel ( int name ) {
        return getObjectLabel ( GLES32.GL_FRAMEBUFFER, name );
    }

    @Override
    public void onMessage ( int source, int type, int id, int severity, String message ) {

        if ( DEBUGGING ) {
            switch ( source ) {
                case GLES32.GL_DEBUG_SOURCE_API:
                    Log.d ( TAG, "GL_DEBUG_SOURCE_API" );
                    break;
                case GLES32.GL_DEBUG_SOURCE_APPLICATION:
                    Log.d ( TAG, "GL_DEBUG_SOURCE_APPLICATION" );
                    break;
                case GLES32.GL_DEBUG_SOURCE_SHADER_COMPILER:
                    Log.d ( TAG, "GL_DEBUG_SOURCE_SHADER_COMPILER" );
                    break;
                case GLES32.GL_DEBUG_SOURCE_WINDOW_SYSTEM:
                    Log.d ( TAG, "GL_DEBUG_SOURCE_WINDOW_SYSTEM" );
                    break;
                case GLES32.GL_DEBUG_SOURCE_THIRD_PARTY:
                    Log.d ( TAG, "GL_DEBUG_SOURCE_THIRD_PARTY" );
                    break;
                case GLES32.GL_DEBUG_SOURCE_OTHER:
                    Log.d ( TAG, "GL_DEBUG_SOURCE_OTHER" );
                    break;
                default:
                    Log.d ( TAG, "SOURCE_UNKNOWN" );
                    break;
            }

            switch ( type ) {
                case GLES32.GL_DEBUG_TYPE_ERROR:
                    Log.d ( TAG, "GL_DEBUG_TYPE_ERROR" );
                case GLES32.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
                    Log.d ( TAG, "GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR" );
                    break;
                case GLES32.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
                    Log.d ( TAG, "GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR" );
                    break;
                case GLES32.GL_DEBUG_TYPE_PORTABILITY:
                    Log.d ( TAG, "GL_DEBUG_TYPE_PORTABILITY" );
                    break;
                case GLES32.GL_DEBUG_TYPE_PERFORMANCE:
                    Log.d ( TAG, "GL_DEBUG_TYPE_PERFORMANCE" );
                    break;
                case GLES32.GL_DEBUG_TYPE_MARKER:
                    Log.d ( TAG, "GL_DEBUG_TYPE_MARKER" );
                    break;
                case GLES32.GL_DEBUG_TYPE_PUSH_GROUP:
                    Log.d ( TAG, "GL_DEBUG_TYPE_PUSH_GROUP" );
                    break;
                case GLES32.GL_DEBUG_TYPE_POP_GROUP:
                    Log.d ( TAG, "GL_DEBUG_TYPE_POP_GROUP" );
                    break;
                case GLES32.GL_DEBUG_TYPE_OTHER:
                    Log.d ( TAG, "GL_DEBUG_TYPE_OTHER" );
                    break;
                default:
                    Log.d ( TAG, "TYPE_UNKNOWN" );
                    break;
            }

            switch ( severity ) {
                case GLES32.GL_DEBUG_SEVERITY_NOTIFICATION:
                    Log.d ( TAG, "GL_DEBUG_SEVERITY_NOTIFICATION" );
                    break;
                case GLES32.GL_DEBUG_SEVERITY_LOW:
                    Log.d ( TAG, "GL_DEBUG_SEVERITY_LOW" );
                    break;
                case GLES32.GL_DEBUG_SEVERITY_MEDIUM:
                    Log.d ( TAG, "GL_DEBUG_SEVERITY_MEDIUM" );
                    break;
                case GLES32.GL_DEBUG_SEVERITY_HIGH:
                    Log.d ( TAG, "GL_DEBUG_SEVERITY_HIGH" );
                    break;
                default:
                    Log.d ( TAG, "SEVERITY_UNKNOWN" );
                    break;
            }

            Log.d ( TAG, "DEBUG ID: " + id + "\nMESSAGE:\n" + message );
        }
    }


}
