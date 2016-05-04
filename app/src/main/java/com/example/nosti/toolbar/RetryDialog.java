package com.example.nosti.toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by nosti on 5/4/2016.
 */
public class RetryDialog extends DialogFragment {
    private boolean rewrite = true;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.existed_filename)
                    .setPositiveButton("Rewrite", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            rewrite = false;
                        }
            });
            return builder.create();
            }
    public boolean getRewrite(){
        return rewrite;
    }
}
