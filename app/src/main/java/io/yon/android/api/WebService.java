package io.yon.android.api;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.yon.android.api.request.LoginRequest;
import io.yon.android.api.response.AuthResponse;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by amirhosein on 6/8/17.
 */

public abstract class WebService {
    private static YonWebService webService;

    public static YonWebService getInstance() {
        return webService;
    }

    public static void init(String authToken) {
        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        if (authToken != null)
            retroBuilder.client(new OkHttpClient.Builder()
                    .addInterceptor(chain -> chain.proceed(
                            chain.request()
                                    .newBuilder()
                                    .header("Authorization", "Token " + authToken)
                                    .build()
                    )).build()
            );


        webService = retroBuilder.build().create(YonWebService.class);
    }

    public static void init() {
        init(null);
    }

    public interface YonWebService {
        // User end-points
        @POST("user/auth/login")
        Observable<Response<AuthResponse>> login(@Body LoginRequest request);
    }
}
