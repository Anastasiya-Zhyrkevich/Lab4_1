package com.example.nosti.toolbar;
import android.content.Context;
import android.graphics.Bitmap;
//import android.renderscript.Allocation;
import android.renderscript.Element;
//import android.renderscript.RenderScript;
//import android.support.v8.renderscript.RenderScript;

import android.support.v8.renderscript.*;
import android.util.Log;


public class BrightBuilder {


    private RenderScript mRS;
    private Allocation mInAllocation;
    private Allocation mOutAllocation;
    private static ScriptC_invert mScript;

    public static Bitmap blur(Context context, Bitmap image, int brigthness) {
        Log.d("Bitmap", "Blur" + brigthness);

        Bitmap inputBitmap = Bitmap.createBitmap(image);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        //ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        Allocation tmpIn = Allocation.createFromBitmap(rs,inputBitmap,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        //mScript = new ScriptC_invert(rs, context.getResources(), brigthness);
        mScript = new ScriptC_invert(rs);

        //mScript.set_gIn(tmpIn);
        //mScript.set_gOut(tmpOut);
        mScript.set_brightness((short)brigthness);
        //mScript.set_gScript(mScript);

        mScript.forEach_invert(tmpIn, tmpOut);

        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }





}