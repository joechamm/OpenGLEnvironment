package com.joechamm.openglenvironment;

import android.content.Context;

public class VelocityShader extends ShaderProgram {

    protected final int aLoc_position;
    protected final int uLoc_color;

    VelocityShader ( Context context ) {
        super ( context, R.raw.velocity_vert, R.raw.velocity_frag );

        aLoc_position = ShaderHelper.getAttributeLocation ( mProgram, "a_position" );
        uLoc_color = ShaderHelper.getUniformLocation ( mProgram, "u_color" );
    }

    public int getPositionLoc () {
        return aLoc_position;
    }

    public int getColorLoc () {
        return uLoc_color;
    }
}
