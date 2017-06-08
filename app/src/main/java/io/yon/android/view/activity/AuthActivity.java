package io.yon.android.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import io.yon.android.R;

/**
 * Created by amirhosein on 6/8/17.
 */

public class AuthActivity extends Activity {
    private ImageButton btnPasswordVisibility;
    private EditText etPassword;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_auth;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.login_to_user_account);
        setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        btnPasswordVisibility = (ImageButton) findViewById(R.id.password_visible);
        btnPasswordVisibility.setOnClickListener(v -> passwordVisibilityToggleRequested());

        etPassword = (EditText) findViewById(R.id.password);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    btnPasswordVisibility.setVisibility(View.VISIBLE);
                else
                    btnPasswordVisibility.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void passwordVisibilityToggleRequested() {
        if (btnPasswordVisibility.getVisibility() == View.VISIBLE) {
            final int selection = etPassword.getSelectionEnd();

            if (hasPasswordTransformation()) {
                etPassword.setTransformationMethod(null);
                btnPasswordVisibility.setImageResource(R.drawable.ic_visibility);
            } else {
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                btnPasswordVisibility.setImageResource(R.drawable.ic_visibility_off);
            }

            etPassword.setSelection(selection);
        }
    }

    private boolean hasPasswordTransformation() {
        return etPassword != null && etPassword.getTransformationMethod() instanceof PasswordTransformationMethod;
    }
}
