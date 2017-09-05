package io.yon.android.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;

import io.yon.android.R;

/**
 * Created by amirhosein on 9/5/2017 AD.
 */

public class ProgressBarDialog extends AppCompatDialog {
    public ProgressBarDialog(Context context) {
        super(context, R.style.Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress_bar);
        setCancelable(false);
    }
}
