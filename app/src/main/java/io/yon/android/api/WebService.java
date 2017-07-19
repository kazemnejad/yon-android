package io.yon.android.api;

import java.io.IOException;
import java.lang.annotation.Annotation;

import io.reactivex.Observable;
import io.yon.android.api.request.LoginRequest;
import io.yon.android.api.request.RegisterRequest;
import io.yon.android.api.response.AuthResponse;
import io.yon.android.api.response.BasicResponse;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
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
    private static Retrofit retrofit;

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

        retrofit = retroBuilder.build();
        webService = retrofit.create(YonWebService.class);
    }

    public static void init() {
        init(null);
    }

    public static <T extends BasicResponse> T getErrorBody(Response<?> response, Class<T> clazz) {
        Converter<ResponseBody, T> converter = retrofit.responseBodyConverter(clazz, new Annotation[0]);

        T result;

        try {
            result = converter.convert(response.errorBody());
        } catch (IOException e) {
            return null;
        }

        return result;
    }

    public interface YonWebService {
        // User end-points
        @POST("user/auth/login")
        Observable<Response<AuthResponse>> login(@Body LoginRequest request);

        @POST("user/auth/signup")
        Observable<Response<AuthResponse>> register(@Body RegisterRequest request);
    }
}
