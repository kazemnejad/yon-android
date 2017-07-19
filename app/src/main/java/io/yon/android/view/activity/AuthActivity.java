package io.yon.android.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.widget.TextView;

import io.yon.android.R;
import io.yon.android.util.Auth;
import io.yon.android.util.ViewUtils;
import io.yon.android.view.fragment.LoginFragment;
import io.yon.android.view.fragment.RegisterFragment;

/**
 * Created by amirhosein on 6/8/17.
 */

public class AuthActivity extends Activity {

    private ViewPager viewPager;
    private TextView toolbarLogin;
    private TextView toolbarRegister;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_auth;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDisplayHomeAsUpEnabled(true);

        initView();
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
