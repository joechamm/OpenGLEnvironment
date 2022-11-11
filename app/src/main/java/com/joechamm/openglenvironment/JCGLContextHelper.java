package com.joechamm.openglenvironment;

import android.opengl.EGL14;
import android.opengl.EGL15;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLES31;
import android.opengl.GLES32;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Set;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.opengles.GL10;

public class JCGLContextHelper {

    private static final String TAG = "jcglenv:ctxhelper";

    private static EGL10 sEGL10;
    private static EGL11 sEGL11;
    private static EGL14 sEGL14;
    private static EGL15 sEGL15;

    private static GLES20 sGLES20;
    private static GLES30 sGLES30;
    private static GLES31 sGLES31;
    private static GLES32 sGLES32;

    private static HashMap<Integer, String> eglIntAttribToStringMap = new HashMap<> ();
    private static HashMap<Integer, String> eglIntErrorToStringMap = new HashMap<> ();
    private static HashMap<Integer, String> glesIntErrorToStringMap = new HashMap<> ();
    private static HashMap<Integer, String> glesBufferTargetToStringMap = new HashMap<> ();
    private static HashMap<Integer, String> glesBufferUsageToStringMap = new HashMap<> ();
    private static HashMap<Integer, String> glesDataTypeToStringMap = new HashMap<> ();
    private static HashMap<Integer, String> glesDrawModeToStringMap = new HashMap<> ();
    private static HashMap<Integer, String> glesShaderTypeToStringMap = new HashMap<> ();
    private static HashMap<Integer, String> glesIntAttribTypeToStringMap = new HashMap<> ();

    // TODO: texture targets
    // TODO: texture internal formats
    // TODO: texture formats
    // TODO: texture types
    // TODO: texture parameters
    // TODO: cull face modes
    // TODO: front face directions
    // TODO: pixel store alignments
    // TODO: shader attrib types
    // TODO: shader uniform types
    // TODO: multisample operations
    // TODO: stencil tests
    // TODO: depth tests
    // TODO: blend modes, functions
    // TODO: framebuffers, renderbuffers

    private static void initAttribs () {
        // initialize our mapping of EGL integer attributes to their string names
        if ( eglIntAttribToStringMap.isEmpty () ) {
            eglIntAttribToStringMap.put ( EGL10.EGL_BUFFER_SIZE, "EGL_BUFFER_SIZE" );
            eglIntAttribToStringMap.put ( EGL10.EGL_RED_SIZE, "EGL_RED_SIZE" );
            eglIntAttribToStringMap.put ( EGL10.EGL_GREEN_SIZE, "EGL_GREEN_SIZE" );
            eglIntAttribToStringMap.put ( EGL10.EGL_BLUE_SIZE, "EGL_BLUE_SIZE" );
            eglIntAttribToStringMap.put ( EGL10.EGL_ALPHA_SIZE, "EGL_ALPHA_SIZE" );
            eglIntAttribToStringMap.put ( EGL10.EGL_CONFIG_ID, "EGL_CONFIG_ID" );
            eglIntAttribToStringMap.put ( EGL10.EGL_DEPTH_SIZE, "EGL_DEPTH_SIZE" );
            eglIntAttribToStringMap.put ( EGL10.EGL_LEVEL, "EGL_LEVEL" );
            eglIntAttribToStringMap.put ( EGL10.EGL_MAX_PBUFFER_WIDTH, "EGL_MAX_PBUFFER_WIDTH" );
            eglIntAttribToStringMap.put ( EGL10.EGL_MAX_PBUFFER_HEIGHT, "EGL_MAX_PBUFFER_HEIGHT" );
            eglIntAttribToStringMap.put ( EGL10.EGL_MAX_PBUFFER_PIXELS, "EGL_MAX_PBUFFER_PIXELS" );
            eglIntAttribToStringMap.put ( EGL10.EGL_NATIVE_VISUAL_ID, "EGL_NATIVE_VISUAL_ID" );
            eglIntAttribToStringMap.put ( EGL10.EGL_NATIVE_VISUAL_TYPE, "EGL_NATIVE_VISUAL_TYPE" );
            eglIntAttribToStringMap.put ( EGL10.EGL_SAMPLE_BUFFERS, "EGL_SAMPLE_BUFFERS" );
            eglIntAttribToStringMap.put ( EGL10.EGL_SAMPLES, "EGL_SAMPLES" );
            eglIntAttribToStringMap.put ( EGL10.EGL_STENCIL_SIZE, "EGL_STENCIL_SIZE" );
            eglIntAttribToStringMap.put ( EGL10.EGL_TRANSPARENT_RED_VALUE, "EGL_TRANSPARENT_RED_VALUE" );
            eglIntAttribToStringMap.put ( EGL10.EGL_TRANSPARENT_GREEN_VALUE, "EGL_TRANSPARENT_GREEN_VALUE" );
            eglIntAttribToStringMap.put ( EGL10.EGL_TRANSPARENT_BLUE_VALUE, "EGL_TRANSPARENT_BLUE_VALUE" );
            eglIntAttribToStringMap.put ( EGL10.EGL_LUMINANCE_SIZE, "EGL_LUMINANCE_SIZE" );
            eglIntAttribToStringMap.put ( EGL10.EGL_ALPHA_MASK_SIZE, "EGL_ALPHA_MASK_SIZE" );
        }
    }

    private static void initErrors () {
        // initialzie mapping of EGL errors to their string names
        if ( eglIntErrorToStringMap.isEmpty () ) {
            eglIntErrorToStringMap.put ( EGL10.EGL_SUCCESS, "EGL_SUCCESS" );
            eglIntErrorToStringMap.put ( EGL10.EGL_NOT_INITIALIZED, "EGL_NOT_INITIALIZED" );
            eglIntErrorToStringMap.put ( EGL10.EGL_BAD_ACCESS, "EGL_BAD_ACCESS" );
            eglIntErrorToStringMap.put ( EGL10.EGL_BAD_ALLOC, "EGL_BAD_ALLOC" );
            eglIntErrorToStringMap.put ( EGL10.EGL_BAD_ATTRIBUTE, "EGL_BAD_ATTRIBUTE" );
            eglIntErrorToStringMap.put ( EGL10.EGL_BAD_CONFIG, "EGL_BAD_CONFIG" );
            eglIntErrorToStringMap.put ( EGL10.EGL_BAD_CONTEXT, "EGL_BAD_CONTEXT" );
            eglIntErrorToStringMap.put ( EGL10.EGL_BAD_CURRENT_SURFACE, "EGL_BAD_CURRENT_SURFACE" );
            eglIntErrorToStringMap.put ( EGL10.EGL_BAD_DISPLAY, "EGL_BAD_DISPLAY" );
            eglIntErrorToStringMap.put ( EGL10.EGL_BAD_MATCH, "EGL_BAD_MATCH" );
            eglIntErrorToStringMap.put ( EGL10.EGL_BAD_NATIVE_PIXMAP, "EGL_BAD_NATIVE_PIXMAP" );
            eglIntErrorToStringMap.put ( EGL10.EGL_BAD_NATIVE_WINDOW, "EGL_BAD_NATIVE_WINDOW" );
            eglIntErrorToStringMap.put ( EGL10.EGL_BAD_PARAMETER, "EGL_BAD_PARAMETER" );
        }
        // same for OpenGL ES errors
        if ( glesIntErrorToStringMap.isEmpty () ) {
            glesIntErrorToStringMap.put ( GLES32.GL_NO_ERROR, "GL_NO_ERROR" );
            glesIntErrorToStringMap.put ( GLES32.GL_INVALID_ENUM, "GL_INVALID_ENUM" );
            glesIntErrorToStringMap.put ( GLES32.GL_INVALID_FRAMEBUFFER_OPERATION, "GL_INVALID_FRAMEBUFFER_OPERATION" );
            glesIntErrorToStringMap.put ( GLES32.GL_INVALID_VALUE, "GL_INVALID_VALUE" );
            glesIntErrorToStringMap.put ( GLES32.GL_INVALID_OPERATION, "GL_INVALID_OPERATION" );
            glesIntErrorToStringMap.put ( GLES32.GL_OUT_OF_MEMORY, "GL_OUT_OF_MEMORY" );
        }
    }

    private static void initGLESBufferTargetMap () {
        if ( glesBufferTargetToStringMap.isEmpty () ) {
            glesBufferTargetToStringMap.put ( GLES32.GL_ARRAY_BUFFER, "GL_ARRAY_BUFFER" );
            glesBufferTargetToStringMap.put ( GLES32.GL_ELEMENT_ARRAY_BUFFER, "GL_ELEMENT_ARRAY_BUFFER" );
            glesBufferTargetToStringMap.put ( GLES32.GL_UNIFORM_BUFFER, "GL_UNIFORM_BUFFER" );
            glesBufferTargetToStringMap.put ( GLES32.GL_TRANSFORM_FEEDBACK_BUFFER, "GL_TRANSFORM_FEEDBACK_BUFFER" );
            glesBufferTargetToStringMap.put ( GLES32.GL_SHADER_STORAGE_BUFFER, "GL_SHADER_STORAGE_BUFFER" );
            glesBufferTargetToStringMap.put ( GLES32.GL_PIXEL_UNPACK_BUFFER, "GL_PIXEL_UNPACK_BUFFER" );
            glesBufferTargetToStringMap.put ( GLES32.GL_PIXEL_PACK_BUFFER, "GL_PIXEL_PACK_BUFFER" );
            glesBufferTargetToStringMap.put ( GLES32.GL_DISPATCH_INDIRECT_BUFFER, "GL_DISPATCH_INDIRECT_BUFFER" );
            glesBufferTargetToStringMap.put ( GLES32.GL_DRAW_INDIRECT_BUFFER, "GL_DRAW_INDIRECT_BUFFER" );
            glesBufferTargetToStringMap.put ( GLES32.GL_COPY_WRITE_BUFFER, "GL_COPY_WRITE_BUFFER" );
            glesBufferTargetToStringMap.put ( GLES32.GL_COPY_READ_BUFFER, "GL_COPY_READ_BUFFER" );
            glesBufferTargetToStringMap.put ( GLES32.GL_ATOMIC_COUNTER_BUFFER, "GL_ATOMIC_COUNTER_BUFFER" );
        }
    }

    private static void initGLESBufferUsageMap () {
        if ( glesBufferUsageToStringMap.isEmpty () ) {
            glesBufferUsageToStringMap.put ( GLES32.GL_STATIC_DRAW, "GL_STATIC_DRAW" );
            glesBufferUsageToStringMap.put ( GLES32.GL_STREAM_DRAW, "GL_STREAM_DRAW" );
            glesBufferUsageToStringMap.put ( GLES32.GL_DYNAMIC_DRAW, "GL_DYNAMIC_DRAW" );
            glesBufferUsageToStringMap.put ( GLES32.GL_STATIC_READ, "GL_STATIC_READ" );
            glesBufferUsageToStringMap.put ( GLES32.GL_DYNAMIC_READ, "GL_DYNAMIC_DRAW" );
            glesBufferUsageToStringMap.put ( GLES32.GL_STREAM_READ, "GL_DYNAMIC_DRAW" );
            glesBufferUsageToStringMap.put ( GLES32.GL_STATIC_COPY, "GL_STATIC_COPY" );
            glesBufferUsageToStringMap.put ( GLES32.GL_DYNAMIC_COPY, "GL_DYNAMIC_COPY" );
            glesBufferUsageToStringMap.put ( GLES32.GL_STREAM_COPY, "GL_STREAM_COPY" );
        }
    }

    private static void initGLESDataTypeMap () {
        if ( glesDataTypeToStringMap.isEmpty () ) {
            glesDataTypeToStringMap.put ( GLES32.GL_BOOL, "GL_BOOL" );
            glesDataTypeToStringMap.put ( GLES32.GL_BYTE, "GL_BYTE" );
            glesDataTypeToStringMap.put ( GLES32.GL_UNSIGNED_BYTE, "GL_UNSIGNED_BYTE" );
            glesDataTypeToStringMap.put ( GLES32.GL_SHORT, "GL_SHORT" );
            glesDataTypeToStringMap.put ( GLES32.GL_UNSIGNED_SHORT, "GL_UNSIGNED_SHORT" );
            glesDataTypeToStringMap.put ( GLES32.GL_INT, "GL_INT" );
            glesDataTypeToStringMap.put ( GLES32.GL_UNSIGNED_INT, "GL_UNSIGNED_INT" );
            glesDataTypeToStringMap.put ( GLES32.GL_FLOAT, "GL_FLOAT" );
        }
    }

    private static void initGLESDrawModeMap () {
        if ( glesDrawModeToStringMap.isEmpty () ) {
            glesDrawModeToStringMap.put ( GLES32.GL_POINTS, "GL_POINTS" );
            glesDrawModeToStringMap.put ( GLES32.GL_LINE_STRIP, "GL_LINE_STRIP" );
            glesDrawModeToStringMap.put ( GLES32.GL_LINE_LOOP, "GL_LINE_LOOP" );
            glesDrawModeToStringMap.put ( GLES32.GL_LINES, "GL_LINES" );
            glesDrawModeToStringMap.put ( GLES32.GL_TRIANGLE_STRIP, "GL_TRIANGLE_STRIP" );
            glesDrawModeToStringMap.put ( GLES32.GL_TRIANGLE_FAN, "GL_TRIANGLE_FAN" );
            glesDrawModeToStringMap.put ( GLES32.GL_TRIANGLES, "GL_TRIANGLES" );
        }
    }

    private static void initGLESShaderTypeMap () {
        if ( glesShaderTypeToStringMap.isEmpty () ) {
            glesShaderTypeToStringMap.put ( GLES32.GL_VERTEX_SHADER, "GL_VERTEX_SHADER" );
            glesShaderTypeToStringMap.put ( GLES32.GL_FRAGMENT_SHADER, "GL_FRAGMENT_SHADER" );
            glesShaderTypeToStringMap.put ( GLES32.GL_GEOMETRY_SHADER, "GL_GEOMETRY_SHADER" );
            glesShaderTypeToStringMap.put ( GLES32.GL_TESS_CONTROL_SHADER, "GL_TESS_CONTROL_SHADER" );
            glesShaderTypeToStringMap.put ( GLES32.GL_TESS_EVALUATION_SHADER, "GL_TESS_EVALUATION_SHADER" );
            glesShaderTypeToStringMap.put ( GLES32.GL_COMPUTE_SHADER, "GL_COMPUTE_SHADER" );
        }
    }

    private static void initGLESAttribTypeMap () {
        if ( glesIntAttribTypeToStringMap.isEmpty () ) {
            glesIntAttribTypeToStringMap.put ( GLES32.GL_FLOAT, "GL_FLOAT" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_FLOAT_VEC2, "GL_FLOAT_VEC2" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_FLOAT_VEC3, "GL_FLOAT_VEC3" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_FLOAT_VEC4, "GL_FLOAT_VEC4" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_FLOAT_MAT2, "GL_FLOAT_MAT2" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_FLOAT_MAT3, "GL_FLOAT_MAT3" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_FLOAT_MAT4, "GL_FLOAT_MAT4" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_FLOAT_MAT2x3, "GL_FLOAT_MAT2x3" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_FLOAT_MAT2x4, "GL_FLOAT_MAT2x4" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_FLOAT_MAT3x2, "GL_FLOAT_MAT3x2" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_FLOAT_MAT3x4, "GL_FLOAT_MAT3x4" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_FLOAT_MAT4x2, "GL_FLOAT_MAT4x2" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_FLOAT_MAT4x3, "GL_FLOAT_MAT4x3" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_INT, "GL_INT" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_INT_VEC2, "GL_INT_VEC2" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_INT_VEC3, "GL_INT_VEC3" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_INT_VEC4, "GL_INT_VEC4" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_UNSIGNED_INT, "GL_UNSIGNED_INT" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_UNSIGNED_INT_VEC2, "GL_UNSIGNED_INT_VEC2" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_UNSIGNED_INT_VEC3, "GL_UNSIGNED_INT_VEC3" );
            glesIntAttribTypeToStringMap.put ( GLES32.GL_UNSIGNED_INT_VEC4, "GL_UNSIGNED_INT_VEC4" );
        }
    }

    public static String getEGLAttributeString ( int attrib ) {

        // initialize our mapping of EGL integer attributes to their string names
        if ( eglIntAttribToStringMap.isEmpty () ) {
            initAttribs ();
        }

        return eglIntAttribToStringMap.getOrDefault ( attrib, "UNKOWN_ATTRIB" );
    }

    public static String getEGLErrorString ( int err ) {

       /* // initialzie mapping of EGL errors to their string names
        if(eglIntErrorToStringMap.isEmpty ()) {
            initErrors ();
        }

        return eglIntErrorToStringMap.getOrDefault ( err, "UNKNOWN_ERROR" );*/

        return GLUtils.getEGLErrorString ( err );
    }

    public static String getGLErrorString ( int err ) {
        return GLU.gluErrorString ( err );
    }

    public static Set<Integer> getEGLAttributes () {

        if ( eglIntAttribToStringMap.isEmpty () ) {
            initAttribs ();
        }

        return eglIntAttribToStringMap.keySet ();
    }

    public static Set<Integer> getEGLErrors () {

        if ( eglIntErrorToStringMap.isEmpty () ) {
            initErrors ();
        }

        return eglIntErrorToStringMap.keySet ();
    }


    public static Set<Integer> getOpenGLESBufferTargets () {
        if ( glesBufferTargetToStringMap.isEmpty () ) {
            initGLESBufferTargetMap ();
        }

        return glesBufferTargetToStringMap.keySet ();
    }

    public static Set<Integer> getOpenGLESBufferUsages () {
        if ( glesBufferUsageToStringMap.isEmpty () ) {
            initGLESBufferUsageMap ();
        }

        return glesBufferUsageToStringMap.keySet ();
    }

    public static Set<Integer> getOpenGLESDataTypes () {
        if ( glesDataTypeToStringMap.isEmpty () ) {
            initGLESDataTypeMap ();
        }
        return glesDataTypeToStringMap.keySet ();
    }

    public static Set<Integer> getOpenGLESDrawModes () {
        if ( glesDrawModeToStringMap.isEmpty () ) {
            initGLESDrawModeMap ();
        }

        return glesDrawModeToStringMap.keySet ();
    }

    public static Set<Integer> getOpenGLESShaderTypes () {
        if ( glesShaderTypeToStringMap.isEmpty () ) {
            initGLESShaderTypeMap ();
        }

        return glesShaderTypeToStringMap.keySet ();
    }

    public static String getOpenGLESBufferTargetString ( int target ) {
        if ( glesBufferTargetToStringMap.isEmpty () ) {
            initGLESBufferTargetMap ();
        }

        return glesBufferTargetToStringMap.getOrDefault ( target, "UNKNOWN_TARGET" );
    }

    public static String getOpenGLESBufferUsageString ( int usage ) {
        if ( glesBufferUsageToStringMap.isEmpty () ) {
            initGLESBufferUsageMap ();
        }

        return glesBufferUsageToStringMap.getOrDefault ( usage, "UNKNOWN_USAGE" );
    }

    public static String getOpenGLESDataTypeString ( int type ) {
        if ( glesDataTypeToStringMap.isEmpty () ) {
            initGLESDataTypeMap ();
        }

        return glesDataTypeToStringMap.getOrDefault ( type, "UNKNOWN_TYPE" );
    }

    public static String getOpenGLDrawModeString ( int mode ) {
        if ( glesDrawModeToStringMap.isEmpty () ) {
            initGLESDrawModeMap ();
        }

        return glesDrawModeToStringMap.getOrDefault ( mode, "UNKNOWN_MODE" );
    }

    public static String getOpenGLShaderTypeString ( int type ) {
        if ( glesShaderTypeToStringMap.isEmpty () ) {
            initGLESShaderTypeMap ();
        }

        return glesShaderTypeToStringMap.getOrDefault ( type, "UNKNOWN_SHADER_TYPE" );
    }

    public static String getOpenGLAttribTypeString ( int type ) {
        if ( glesIntAttribTypeToStringMap.isEmpty () ) {
            initGLESAttribTypeMap ();
        }

        return glesIntAttribTypeToStringMap.getOrDefault ( type, "UNKNOWN_ATTRIB_TYPE" );
    }

    public static void logEGLErrors ( @NonNull EGL10 egl, String op ) {
        int err = egl.eglGetError ();
        while ( EGL10.EGL_SUCCESS != err ) {
            String errString = getEGLErrorString ( err );
            Log.e ( TAG, op + "produced EGL ERR: " + errString );
            err = egl.eglGetError ();
        }
    }

    public static void logGLVersion ( GL10 gl ) {
        String versionStr = getGLVersionString ( gl );
        Log.d ( TAG, "OpenGL ES Version: " + versionStr );
    }

    public static void logGLSLVersion ( GL10 gl ) {
        String glslVersionStr = getGLSLVersionString ( gl );
        Log.d ( TAG, "OpenGL Shading Language Version: " + glslVersionStr );
    }

    public static void logGLExtensions ( GL10 gl ) {
        String[] extensions = getGLExtensions ( gl );
        Log.d ( TAG, "Found " + extensions.length + " OpenGL ES Extensions" );
        for ( String extStr : extensions ) {
            Log.d ( TAG, extStr );
        }
    }

    public static void logGLVendor ( GL10 gl ) {
        String vendorStr = getGLVendorString ( gl );
        Log.d ( TAG, "OpenGL ES Vendor: " + vendorStr );
    }

    public static void logGLRenderer ( GL10 gl ) {
        String rendererStr = getGLRendererString ( gl );
        Log.d ( TAG, "OpenGL ES Renderer: " + rendererStr );
    }

    public static String getGLVersionString ( @NonNull GL10 gl ) {
        // Create a minimum supported OpenGL ES context, then check:
        String version = gl.glGetString ( GL10.GL_VERSION );
        return version;
    }

    public static String getGLExtensionsString ( @NonNull GL10 gl ) {
        String extensions = gl.glGetString ( GL10.GL_EXTENSIONS );
        return extensions;
    }

    public static String[] getGLExtensions ( GL10 gl ) {
        String extStr = gl.glGetString ( GL10.GL_EXTENSIONS );
        String[] extensions = extStr.split ( " " );
        return extensions;
    }

    public static String getGLVendorString ( @NonNull GL10 gl ) {
        String vendor = gl.glGetString ( GL10.GL_VENDOR );
        return vendor;
    }

    public static String getGLRendererString ( @NonNull GL10 gl ) {
        String renderer = gl.glGetString ( GL10.GL_RENDERER );
        return renderer;
    }

    @Nullable
    public static String getGLSLVersionString ( GL10 gl ) {
        if ( sGLES20 != null ) {
            return gl.glGetString ( GLES20.GL_SHADING_LANGUAGE_VERSION );
        }
        return null;
    }

    public static void setGLES32 ( GLES32 gles32 ) {
        sGLES32 = gles32;
        if ( gles32 != null ) {
            sGLES31 = gles32;
            sGLES30 = gles32;
            sGLES20 = gles32;
        }
    }

    public static GLES32 getGLES32 () {
        return sGLES32;
    }

    public static void setEGL15 ( EGL15 egl15 ) {
        sEGL15 = egl15;
    }

    public static void setEGL14 ( EGL14 egl14 ) {
        sEGL14 = egl14;
    }

    public static void setEGL11 ( EGL11 egl11 ) {
        sEGL11 = egl11;
        if ( egl11 != null ) {
            sEGL10 = egl11;
        }
    }

    public static void setEGL10 ( EGL10 egl10 ) {
        if ( null == sEGL11 ) {
            sEGL10 = egl10;
        }
    }

    public static EGL15 getEGL15 () {
        return sEGL15;
    }

    public static EGL14 getEGL14 () {
        return sEGL14;
    }

    public static EGL11 getEGL11 () {
        return sEGL11;
    }

    public static EGL10 getEGL10 () {
        return sEGL10;
    }
}
