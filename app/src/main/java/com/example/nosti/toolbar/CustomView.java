package com.example.nosti.toolbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by nosti on 4/25/2016.
 */
public class CustomView extends View {

    private Paint paint;
    private Bitmap bitmap;
    private Rect srcRect;
    private Rect dstRect;
    private Rect imgRect;
    private int width;
    private int height;
    private String text;

    public CustomView(Context context) {
        super(context);
        init(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        paint = new Paint();
        text = "Hello";
        //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ex);
        //srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        srcRect = null;
        dstRect = new Rect();
        imgRect = new Rect();
    }

    public void setBitmap(Bitmap bitmap, String file)
    {
        this.bitmap = bitmap;
        this.text = file;
        Log.d("Main", "setBitMap");
        //invalidate();
        requestLayout();
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
        Log.d("Main", "setBitMap");
        //invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("Main", "onDraw");
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, srcRect, imgRect, paint);
            Log.d("Main", "Drawing");

            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            float padding = dstRect.height() * (float)0.1;
            canvas.drawText(text, getWidth() - dstRect.width() - padding, imgRect.height() + dstRect.height() + padding, paint);
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("Main", "onLayout");
        width = getWidth();
        height = getHeight();

        if (bitmap != null) {
            paint.getTextBounds(text, 0, text.length(), dstRect);
            double padding = dstRect.height() * 0.1;
            height -= dstRect.height() + padding;
            double scale = Math.min(1.0 * width / bitmap.getWidth(), 1.0 * height / bitmap.getHeight());
            imgRect.set(0, 0, (int) (scale * bitmap.getWidth()), (int) (scale * bitmap.getHeight()));
        }

        //dstRect.set(, 0 , , height);
    }
}
