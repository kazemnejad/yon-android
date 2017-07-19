package io.yon.android.presenter;

import android.app.Application;
import android.support.annotation.NonNull;

import com.waylonbrown.lifecycleawarerx.LifecycleBinder;

import io.reactivex.Observable;
import io.yon.android.Config;
import io.yon.android.api.response.AuthResponse;
import io.yon.android.contract.AuthContract;
import io.yon.android.repository.Lce;
import io.yon.android.repository.UserRepository;
import io.yon.android.util.RxUtils;
import io.yon.android.view.MvpView;
import retrofit2.Response;

/**
 * Created by amirhosein on 7/18/17.
 */

public class AuthPresenter extends Presenter implements AuthContract.Presenter {
    private AuthContract.View view;

    private Observable<Lce<Response<AuthResponse>>> loginObservable;
    private Observable<Lce<Response<AuthResponse>>> registerObservable;

    public AuthPresenter(Application application) {
        super(application);
    }

    @Override
    public void bindView(MvpView view) {
        this.view = (AuthContract.View) view;
    }

    @Override
    public void login(@NonNull String email, @NonNull String password) {
        if (loginObservable == null)
            loginObservable = UserRepository.getInstance().getUser(email, password)
                    .compose(RxUtils.applySchedulers())
                    .cache();

        loginObservable.takeWhile(LifecycleBinder.notDestroyed(view))
                .compose(LifecycleBinder.subscribeWhenReady(view, new Lce.Observer<>(
                        lce -> {
                            if (lce.isLoading())
                                view.showLoading();
                            else if (lce.hasError())
                                view.showError(lce.getError());
                            else {
                                UserRepository.getInstance().saveIfSuccessful(lce.getData(), getApplication());
                                view.handleResponse(lce.getData());
                            }

                            loginObservable = null;
                        }
                )));
    }

    @Override
    public void register(@NonNull String fname, @NonNull String lname, @NonNull String email, @NonNull String password) {
        if (registerObservable == null)
            registerObservable = UserRepository.getInstance().createUser(fname, lname, email, password)
                    .compose(RxUtils.applySchedulers())
                    .cache();

        registerObservable.takeWhile(LifecycleBinder.notDestroyed(view))
                .compose(LifecycleBinder.subscribeWhenReady(view, new Lce.Observer<>(
                        lce -> {
                            if (lce.isLoading())
                                view.showLoading();
                            else if (lce.hasError())
                                view.showError(lce.getError());
                            else {
                                saveUser(fname, lname, email, lce.getData());
                                view.handleResponse(lce.getData());
                            }

                            registerObservable = null;
                        }
                )));
    }

    private void saveUser(@NonNull String fname, @NonNull String lname, @NonNull String email,
                          Response<AuthResponse> response) {
        if (response.code() != 200 || response.body() == null)
            return;

        Config.get(getApplication()).edit()
                .putString(Config.Field.Token, response.body().getToken())
                .apply();

        Config.getUser(getApplication()).edit()
                .putString(Config.Field.FirstName, fname)
                .putString(Config.Field.LastName, lname)
                .putString(Config.Field.Email, email)
                .apply();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        view = null;
    }
}
