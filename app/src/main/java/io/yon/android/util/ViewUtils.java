package io.yon.android.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by amirhosein on 6/1/17.
 */

public class ViewUtils {
    public static int px(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int dp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static void closeKeyboard(Activity activity) {
        if (activity != null) {
            IBinder windowToken = activity.getWindow().getDecorView().getWindowToken();

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken, 0);
        }
    }

    public static void closeKeyboard(Fragment fragment) {
        if (fragment != null) {
            closeKeyboard(fragment.getActivity());
        }
    }

    public static void connectToTextInputLayout(EditText et, TextInputLayout til) {
        if (et == null || til == null)
            return;

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (til.isErrorEnabled())
                    til.setErrorEnabled(false);
            }
        });
    }
}
