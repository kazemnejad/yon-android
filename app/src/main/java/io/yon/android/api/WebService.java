package io.yon.android.api;

import java.io.IOException;
import java.lang.annotation.Annotation;

import io.reactivex.Observable;
import io.yon.android.api.request.LoginRequest;
import io.yon.android.api.request.RegisterRequest;
import io.yon.android.api.response.AuthResponse;
import io.yon.android.api.response.BasicResponse;
import io.yon.android.api.response.ShowcaseResponse;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
                .addInterceptor(logInterceptor);

        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        if (authToken != null)
            okBuilder.addInterceptor(chain -> chain.proceed(
                    chain.request()
                            .newBuilder()
                            .header("Authorization", "Token " + authToken)
                            .build()
            ));

        retrofit = retroBuilder.client(okBuilder.build()).build();
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

        // Content providers end-points
        @GET("home/mobile")
        Observable<ShowcaseResponse> getHomePage(@Query("long") double longitude, @Query("latt") double latitude);
    }
}
