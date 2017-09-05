package io.yon.android.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import io.yon.android.R;
import io.yon.android.api.response.AuthResponse;
import io.yon.android.contract.AuthContract;
import io.yon.android.presenter.AuthPresenter;
import io.yon.android.util.Auth;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.dialog.ProgressBarDialog;
import io.yon.android.view.fragment.LoginFragment;
import io.yon.android.view.fragment.RegisterFragment;
import retrofit2.Response;

/**
 * Created by amirhosein on 6/8/17.
 */

public class AuthActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener, AuthContract.View {

    private static final int RC_SIGN_IN = 993;

    private ViewPager viewPager;
    private TextView toolbarLogin;
    private TextView toolbarRegister;
    private ProgressBarDialog progressBarDialog;

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount loggedInAccount;

    private AuthPresenter presenter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_auth;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBarDialog = new ProgressBarDialog(this);

        mGoogleApiClient = Auth.Google.createGoogleApiClient(this, this);

        setDisplayHomeAsUpEnabled(true);

        initView();

        presenter = ViewModelProviders.of(this).get(AuthPresenter.class);
        presenter.bindView(this);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            setResult(Auth.FAIL);
            finish();
            super.onBackPressed();
        } else {
            goToLogin();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = com.google.android.gms.auth.api.Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.unable_to_connect_to_server, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        progressBarDialog.show();
    }

    @Override
    public void showError(Throwable e) {
        progressBarDialog.cancel();
        Snackbar.make(getRootView(), R.string.unable_to_connect_to_server, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, v -> presenter.sendGoogleAuthenticationResult(presenter.getLoggedInAccount().getIdToken()))
                .show();
    }

    @Override
    public void handleResponse(Response<AuthResponse> response) {
        progressBarDialog.cancel();
        if (response.code() == 200)
            onSuccessfulAuth();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.auth_view_pager);
        viewPager.setAdapter(new AuthPagesAdaptor(getSupportFragmentManager()));

        toolbarLogin = (TextView) findViewById(R.id.toolbar_text_main2);
        toolbarLogin.setText(R.string.login_to_user_account);

        toolbarRegister = (TextView) findViewById(R.id.toolbar_text_main);
        toolbarRegister.setText(R.string.create_user_account);
    }

    public void goToLogin() {
        viewPager.setCurrentItem(0, true);
        toolbarRegister.animate()
                .translationX(ViewUtils.px(this, 100))
                .alpha(0)
                .setDuration(200)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();

        toolbarLogin.animate()
                .translationX(ViewUtils.px(this, 0))
                .alpha(1)
                .setDuration(200)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }

    public void goToRegister() {
        viewPager.setCurrentItem(1, true);
        toolbarLogin.animate()
                .translationX(-ViewUtils.px(this, 100))
                .alpha(0)
                .setDuration(200)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();

        toolbarRegister.animate()
                .translationX(-ViewUtils.px(this, 0))
                .alpha(1)
                .setDuration(200)
                .setInterpolator(new FastOutSlowInInterpolator())
                .start();
    }

    public void onSuccessfulAuth() {
        setResult(Auth.OK);
        finish();
    }

    public void doGoogleSignIn() {
        Intent signInIntent = com.google.android.gms.auth.api.Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess() && result.getSignInAccount() != null)
            presenter.sendGoogleAuthenticationResult(result.getSignInAccount().getIdToken());
        else
            Toast.makeText(this, R.string.google_sign_in_is_canceled, Toast.LENGTH_SHORT)
                    .show();
    }

    private static class AuthPagesAdaptor extends FragmentStatePagerAdapter {

        private LoginFragment loginFragment;
        private RegisterFragment registerFragment;

        AuthPagesAdaptor(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (loginFragment == null)
                        loginFragment = LoginFragment.create();

                    return loginFragment;

                case 1:
                    if (registerFragment == null)
                        registerFragment = RegisterFragment.create();

                    return registerFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
