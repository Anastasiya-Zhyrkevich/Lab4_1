package com.example.nosti.toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements EditNameDialog.EditNameDialogListener {

    private CustomView customView;
    private RadioGroup radioGroup;
    private SeekBar seekBar;
    private Bitmap bitmap;

    int[] curProgress;
    private int indexProgress;

    private static final int SELECT_PICTURE_ACTIVITY_REQUEST_CODE = 0;
    private static final int SAVE_PICTURE_ACTIVITY_REQUEST_CODE = 1;
    private static final int DIALOG_EXIT = 1;

    private static final String INVALIDE_FILENAME = "Invalide_filename";
    private static final String EXISTED_FILENAME = "Existed_filename";
    private static final String NOTHING_TO_SAVE = "Nothing to save";

    String position1;
    String position2;

    private void initDecoration(Bitmap newBitmap, String file) {
        Log.d("Main", "initDecor");
        bitmap =  newBitmap.copy(newBitmap.getConfig(), true);
        indexProgress = 1;
        curProgress = new int[3];
        for (int i= 0; i<3; i++)
            curProgress[i] = 0;
        curProgress[2] = 12;
        position1 = "";
        position2 = "";
        try {
            ExifInterface exif = new ExifInterface(file);
            position1 = convertLatLon(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            position2 = convertLatLon(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("Main", "lat" + position1 + "lon" + position2);
        customView.setBitmap(bitmap, "lat : " + position1 + ", lon : " + position2);
        initSeekBar();
    }

    private void initSeekBar() {
        seekBar.setEnabled(true);
        seekBar.setProgress(curProgress[indexProgress]);
    }
    private String convertLatLon(String position){
        if (position == null)
            return "Unknown";
        String[] parts = position.split("/");
        return parts[0];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Main", "OnCreate");
        super.onCreate(savedInstanceState);
        Log.d("Main", "OnCreateSuper");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ex);

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
        seekBar.setEnabled(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("Progress", "" + progress);
                if (fromUser && bitmap != null) {
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
                if (bitmap == null) return;
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
        if (image == null) return null;
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
        if (id == R.id.load) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_PICTURE_ACTIVITY_REQUEST_CODE);
            Log.d("Load", "endLoad");
            return true;
        }
        else
        if (id == R.id.save)
        {
            EditNameDialog editNameDialog = new EditNameDialog();
            editNameDialog.show(getSupportFragmentManager(), "fragment_edit_name");
        }
        return super.onOptionsItemSelected(item);
    }
    public void onFinishEditDialog(String filename)
    {
        try {
            if (bitmap == null)
                throw new IOException(NOTHING_TO_SAVE);
            Log.d("Main", filename);
            if (filename.equals(""))
                throw new IOException(INVALIDE_FILENAME);
            File file = getOutputMediaFile(filename);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream ostream = new FileOutputStream(file);
            if (ostream == null)
            {
                Log.d("Main", "Nullostream");
            }
            performTranformation(bitmap).compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            ostream.flush();
            ostream.close();
            Log.d("Main", "Saved");
            customView.setBitmap(performTranformation(bitmap), file.toString());
            galleryAddPic(file.toString());
            showVerdictSaving(getResources().getString(R.string.saved_good), getResources().getString(R.string.ok));
        }
        catch (Exception e) {
            switch (e.getMessage()) {
                case INVALIDE_FILENAME:
                    showVerdictSaving(getResources().getString(R.string.invalide_filename),
                            getResources().getString(R.string.ok));
                    break;
                case NOTHING_TO_SAVE:
                    showVerdictSaving(getResources().getString(R.string.no_image),
                            getResources().getString(R.string.ok));
                    break;
                default:
                    showVerdictSaving(getResources().getString(R.string.saved_bad), getResources().getString(R.string.ok));
                    e.printStackTrace();
                    break;

            }
        }
    }

    private void galleryAddPic(String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void showVerdictSaving(final CharSequence message, final CharSequence button){
        DialogFragment df = new DialogFragment () {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(message)
                        .setPositiveButton(button, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                return builder.create();
            }
        };
        df.show(getSupportFragmentManager(), "dialog");
    }

    private File getOutputMediaFile(String filename)
    {
        File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (mediaStorageDir == null || ! mediaStorageDir.exists() && !mediaStorageDir.mkdir())
            return null;
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + filename + ".jpg");
        Log.d("Main", Uri.fromFile(mediaFile).toString());
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PICTURE_ACTIVITY_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        Bitmap newBitmap = BitmapFactory.decodeFile(filePath);
                        if (newBitmap == null) {
                            Log.d("Main", "nullBitmap");
                        }
                        Log.d("Main", "newBitMap");
                        initDecoration(newBitmap, filePath);

                    }
                    cursor.close();
                }
                break;
            }
            case SAVE_PICTURE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                }
                break;
            }
    }
}
