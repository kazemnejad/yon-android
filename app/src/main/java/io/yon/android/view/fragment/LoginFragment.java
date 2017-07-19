package io.yon.android.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.yon.android.R;
import io.yon.android.api.WebService;
import io.yon.android.api.response.AuthResponse;
import io.yon.android.contract.AuthContract;
import io.yon.android.presenter.AuthPresenter;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.activity.AuthActivity;
import retrofit2.Response;

/**
 * Created by amirhosein on 7/17/17.
 */

public class LoginFragment extends Fragment implements Validator.ValidationListener, AuthContract.View {

    private ProgressBar mProgressBar;

    @Email(messageResId = R.string.login_invalid_email_format)
    private EditText etEmail;

    @Password(min = 6, scheme = Password.Scheme.ANY, messageResId = R.string.login_invalid_password_format)
    private EditText etPassword;

    private ImageButton btnPasswordVisibility;
    private Button btnLogin, btnGoogleSignIn, btnGoRegister;
    private TextInputLayout tilEmail, tilPassword;

    private Validator validator;

    private AuthPresenter presenter;

    public static LoginFragment create() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        findViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        validator = new Validator(this);
        validator.setValidationListener(this);

        initViews();

        presenter = ViewModelProviders.of(this).get(AuthPresenter.class);
        presenter.bindView(this);
    }

    private void findViews(View view) {
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        btnGoRegister = (Button) view.findViewById(R.id.btn_go_to_register);
        btnLogin = (Button) view.findViewById(R.id.login);
        btnGoogleSignIn = (Button) view.findViewById(R.id.google_sign_in);
        btnPasswordVisibility = (ImageButton) view.findViewById(R.id.password_visible);

        etEmail = (EditText) view.findViewById(R.id.email);
        etPassword = (EditText) view.findViewById(R.id.password);

        tilEmail = (TextInputLayout) view.findViewById(R.id.til_email);
        tilPassword = (TextInputLayout) view.findViewById(R.id.til_password);
    }

    private void initViews() {
        btnPasswordVisibility.setOnClickListener(v -> passwordVisibilityToggleRequested());
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
        btnLogin.setOnClickListener(v -> validator.validate());
        btnGoRegister.setOnClickListener(v -> {
            try {
                ((AuthActivity) getActivity()).goToRegister();
            } catch (Exception exp) {
                Logger.e(exp, "Host activity is not AuthActivity instance");
            }
        });

        ViewUtils.connectToTextInputLayout(etEmail, tilEmail);
        ViewUtils.connectToTextInputLayout(etPassword, tilPassword);
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

    private void clearVisibilities() {
        mProgressBar.setVisibility(View.INVISIBLE);

        btnPasswordVisibility.setEnabled(true);
        btnGoogleSignIn.setEnabled(true);
        btnLogin.setEnabled(true);
        btnGoRegister.setEnabled(true);

        etEmail.setEnabled(true);
        etPassword.setEnabled(true);
    }

    @Override
    public void showLoading() {
        clearVisibilities();

        btnPasswordVisibility.setEnabled(false);
        btnGoogleSignIn.setEnabled(false);
        btnLogin.setEnabled(false);
        btnGoRegister.setEnabled(false);

        etEmail.setEnabled(false);
        etPassword.setEnabled(false);

        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable e) {
        clearVisibilities();

        Logger.d(e);
        Toast.makeText(
                getContext(),
                getString(R.string.unable_to_connect_to_server),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void handleResponse(Response<AuthResponse> response) {
        if (response.code() == 200)
            ((AuthActivity) getActivity()).onSuccessfulAuth();

        if (response.code() == 401) {
            clearVisibilities();
            AuthResponse authResponse = WebService.getErrorBody(response, AuthResponse.class);
            Logger.d(authResponse.getCode());
            if (authResponse.getCode() == 2053)
                tilEmail.setError(getString(R.string.email_password_doesnt_match));

        }
    }

    @Override
    public void onValidationSucceeded() {
        ViewUtils.closeKeyboard(this);
        presenter.login(etEmail.getText().toString(), etPassword.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            switch (view.getId()) {
                case R.id.email:
                    tilEmail.setError(message);
                    break;

                case R.id.password:
                    tilPassword.setError(message);
                    break;
            }
        }
    }
}
