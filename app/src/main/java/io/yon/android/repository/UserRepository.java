package io.yon.android.repository;

import android.content.Context;

import io.reactivex.Observable;
import io.yon.android.Config;
import io.yon.android.api.WebService;
import io.yon.android.api.request.GoogleSignInRequest;
import io.yon.android.api.request.LoginRequest;
import io.yon.android.api.request.RegisterRequest;
import io.yon.android.api.response.AuthResponse;
import retrofit2.Response;


/**
 * Created by amirhosein on 6/8/17.
 */

public class UserRepository {
    private static UserRepository repo;

    private UserRepository() {}

    public static UserRepository getInstance() {
        if (repo == null)
            repo = new UserRepository();

        return repo;
    }

    public Observable<Lce<Response<AuthResponse>>> getUser(String email, String password) {
        return WebService.getInstance()
                .login(new LoginRequest(email, password))
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }

    public Observable<Lce<Response<AuthResponse>>> createUser(String fname, String lname, String email, String password) {
        return WebService.getInstance()
                .register(new RegisterRequest(fname, lname, email, password))
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }

    public Observable<Lce<Response<AuthResponse>>> getUser(String idToken) {
        return WebService.getInstance()
                .googleAuth(new GoogleSignInRequest(idToken))
                .map(Lce::data)
                .startWith(Lce.loading())
                .onErrorReturn(Lce::error);
    }

    public void saveIfSuccessful(Response<AuthResponse> response, Context context) {
        if (response.code() == 200 && context != null
                && response.body() != null
                && response.body().getUser() != null) {
            response.body().getUser().save(context);
            Config.get(context).edit()
                    .putString(Config.Field.Token, response.body().getToken())
                    .apply();
        }
    }
}
