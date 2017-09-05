package io.yon.android.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import io.yon.android.Config;
import io.yon.android.api.Constants;
import io.yon.android.db.AppDatabase;
import io.yon.android.model.User;
import io.yon.android.view.activity.Activity;
import io.yon.android.view.activity.AuthActivity;

/**
 * Created by amirhosein on 6/8/17.
 */

public class Auth {

    public static final int OK = 5;
    public static final int FAIL = -1;

    public static void login(Activity activity, OnAuthResultListener listener) {
        if (activity == null)
            return;

        if (listener != null) {
            int requestCode = activity.getNewRequestCode();
            activity.addOnActivityResultListener(requestCode, (resultCode, data) -> listener.onResult(resultCode == OK));
            activity.startActivityForResult(new Intent(activity, AuthActivity.class), requestCode);
        } else {
            activity.startActivity(new Intent(activity, AuthActivity.class));
        }
    }

    public static boolean check(Context c) {
        return Config.get(c).contains(Config.Field.Token);
    }

    public static String getToken(Context c) throws UnAuthenticatedException {
        String token = Config.get(c).getString(Config.Field.Token, null);
        if (token == null)
            throw new UnAuthenticatedException();

        return token;
    }

    @Nullable
    public static String getTokenNoException(Context c) {
        return Config.get(c).getString(Config.Field.Token, null);
    }

    public static void logout(Context context) {
        Config.get(context).edit()
                .remove(Config.Field.Token)
                .apply();

        Config.getUser(context).edit()
                .remove(Config.Field.FirstName)
                .remove(Config.Field.LastName)
                .remove(Config.Field.Email)
                .remove(Config.Field.Avatar)
                .apply();

        new Thread(() -> AppDatabase.getInstance(context.getApplicationContext())
                .reservationDao()
                .deleteAll())
                .start();
    }

    public static User user(Context context) {
        if (!check(context))
            return null;

        SharedPreferences pref = Config.getUser(context);
        User user = new User();
        user.setFirstName(pref.getString(Config.Field.FirstName, ""));
        user.setLastName(pref.getString(Config.Field.LastName, ""));
        user.setEmail(pref.getString(Config.Field.Email, ""));
        user.setAvatar(pref.getString(Config.Field.Avatar, ""));


        return user;
    }

    public static class UnAuthenticatedException extends Exception {
    }

    public interface OnAuthResultListener {
        void onResult(boolean isSuccessful);
    }

    public static class Google {
        public static GoogleApiClient createGoogleApiClient(Activity activity, GoogleApiClient.OnConnectionFailedListener listener) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestId()
                    .requestIdToken(Constants.GoogleServerClientId)
                    .requestEmail()
                    .requestProfile()
                    .build();

            return new GoogleApiClient.Builder(activity)
                    .enableAutoManage(activity, listener)
                    .addApi(com.google.android.gms.auth.api.Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
    }
}
