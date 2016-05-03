/**
 * Created by nosti on 4/26/2016.
 */

package com.example.nosti.toolbar;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.*;
import android.util.Log;


public class ExtractBuilder {

    private RenderScript mRS;
    private Allocation mInAllocation;
    private Allocation mOutAllocation;
    private static ScriptC_extract mScript;

    public static Bitmap blur(Context context, Bitmap image, int extract) {
        Log.d("Bitmap", "extarct" + extract);

        Bitmap inputBitmap = Bitmap.createBitmap(image);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        //ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        Allocation tmpIn = Allocation.createFromBitmap(rs,inputBitmap,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        mScript = new ScriptC_extract(rs);

        float [] fl = {(float)1.0* extract / 12, 0, 0, 0,
                       0, 1, 0, 0,
                       0, 0, 1, 0,
                       0, 0, 0, 1};

        mScript.set_colorMat(new Matrix4f(fl));
        mScript.forEach_extract(tmpIn, tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }





}
