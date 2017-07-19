package io.yon.android.contract;

import android.support.annotation.NonNull;

import io.yon.android.api.response.AuthResponse;
import io.yon.android.view.MvpView;
import retrofit2.Response;

/**
 * Created by amirhosein on 6/9/17.
 */

public abstract class AuthContract extends Contract {
    public interface Presenter {
        void login(@NonNull String email, @NonNull String password);

        void register(@NonNull String email, @NonNull String password, @NonNull String fname, @NonNull String lname);
    }

    public interface View extends MvpView {
        void showLoading();

        void showError(Throwable e);

        void handleResponse(Response<AuthResponse> response);
    }
}
