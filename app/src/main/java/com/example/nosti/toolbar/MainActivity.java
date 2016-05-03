package com.example.nosti.toolbar;

import android.support.v8.renderscript.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.RadioGroup;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private CustomView customView;
    private RadioGroup radioGroup;
    private SeekBar seekBar;
    private Bitmap bitmap;

    int[] curProgress;
    private int indexProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ex);
        indexProgress = 1;
        curProgress = new int[3];
        for (int i= 0; i<3; i++)
            curProgress[i] = 0;
        curProgress[2] = 12;

        customView = (CustomView) findViewById(R.id.custom_view);
        customView.setBitmap(bitmap);
        customView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = customView.getWidth();
                int heigth = customView.getHeight();
                if (width > 0 && heigth > 0) {
                    customView.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });



        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("Progress", "" + progress);
                if (fromUser) {
                    Bitmap copyBit = bitmap.copy(bitmap.getConfig(), true);
                    curProgress[indexProgress] = progress;
                    Bitmap blurredBitmap = performTranformation(copyBit);

                    customView.setBitmap(blurredBitmap);
                    Log.d("Progress", "" + indexProgress + " " + curProgress[0] + " " + curProgress[1] + " " + curProgress[2] + " ");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.radio_blur:
                        seekBar.setProgress(curProgress[1]);
                        indexProgress = 1;
                        break;
                    case R.id.radio_bright:
                        seekBar.setProgress(curProgress[0]);
                        indexProgress = 0;
                        break;
                    case R.id.radio_extract:
                        seekBar.setProgress(curProgress[2]);
                        indexProgress = 2;

                        break;
                    default:
                        break;
                }
            }
        });



    }

    private Bitmap performTranformation(Bitmap image)
    {
        Bitmap blurredBitmap = BlurBuilder.blur(MainActivity.this, image, (float)1.0 * curProgress[1] + 1);
        blurredBitmap = BrightBuilder.blur(MainActivity.this, blurredBitmap, curProgress[0]);
        blurredBitmap = ExtractBuilder.blur(MainActivity.this, blurredBitmap, curProgress[2]);
        return blurredBitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
