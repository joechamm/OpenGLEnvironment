package com.joechamm.openglenvironment;

import android.content.Context;

public class DensityShader extends ShaderProgram {

    private final int aLoc_position;
    private final int aLoc_uv;
    private final int uLoc_density;
    private final int uLoc_color;

    DensityShader ( Context context ) {
        super ( context, R.raw.density_vert, R.raw.density_frag );

        aLoc_position = ShaderHelper.getAttributeLocation ( mProgram, "a_position" );
        aLoc_uv = ShaderHelper.getAttributeLocation ( mProgram, "a_uv" );

        uLoc_density = ShaderHelper.getUniformLocation ( mProgram, "u_density" );
        uLoc_color = ShaderHelper.getUniformLocation ( mProgram, "u_color" );
    }

    public int getPositionLoc () {
        return aLoc_position;
    }

    public int getUVLoc () {
        return aLoc_uv;
    }

    public int getDensityLoc () {
        return uLoc_density;
    }

    public int getColorLoc () {
        return uLoc_color;
    }


}
