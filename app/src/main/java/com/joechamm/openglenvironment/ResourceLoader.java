package com.joechamm.openglenvironment;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceLoader {

    private static final String TAG = "jcglenv:resloader";

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
}
