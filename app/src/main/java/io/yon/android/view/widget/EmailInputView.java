package io.yon.android.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import io.yon.android.R;
import io.yon.android.view.activity.Activity;

/**
 * Created by amirhosein on 8/24/2017 AD.
 */

public class EmailInputView extends TokenCompleteTextView<String> {
    public EmailInputView(Context context) {
        super(context);
    }

    public EmailInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmailInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(String object) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.contact_token, (ViewGroup) getParent(), false);
        view.setText(object);

        return view;
    }

    @Override
    protected String defaultObject(String completionText) {
        return isValidEmail(completionText)? completionText : null;
    }

    private boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
