package io.yon.android.api;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import io.reactivex.Observable;
import io.yon.android.api.request.GoogleSignInRequest;
import io.yon.android.api.request.LoginRequest;
import io.yon.android.api.request.RegisterRequest;
import io.yon.android.api.response.AuthResponse;
import io.yon.android.api.response.BasicResponse;
import io.yon.android.api.response.SearchResponse;
import io.yon.android.api.response.ZoneSearchResponse;
import io.yon.android.model.MenuSection;
import io.yon.android.model.OpeningInterval;
import io.yon.android.model.Reservation;
import io.yon.android.model.Restaurant;
import io.yon.android.model.RestaurantList;
import io.yon.android.model.Tag;
import io.yon.android.model.UserReview;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        if (authToken != null)
            okBuilder.addInterceptor(chain -> chain.proceed(
                    chain.request()
                            .newBuilder()
                            .header("Authorization", "JWT " + authToken)
                            .build()
            ));

        okBuilder.addInterceptor(
                new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
        );

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

    public static <T extends BasicResponse> T getBodyFromJson(String content, Class<T> clazz) {
        Converter<ResponseBody, T> converter = retrofit.responseBodyConverter(clazz, new Annotation[0]);
        T result;

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        ResponseBody responseBody = ResponseBody.create(JSON, content);

        try {
            result = converter.convert(responseBody);
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

        @POST("user/auth/signup/google")
        Observable<Response<AuthResponse>> googleAuth(@Body GoogleSignInRequest request);

        // Content providers end-points
        @GET("home/mobile")
        Observable<Response<ResponseBody>> getHomePage(@Query("long") Double longitude, @Query("lat") Double latitude, @Query("zone_slug") String zoneSlug);

        // Restaurant content delivery
        @GET("restaurant")
        Observable<List<Restaurant>> getRestaurantsByZone(@Query("zone") String zone, @Query("long") Double longitude, @Query("lat") Double latitude);

        @GET("restaurant")
        Observable<List<Restaurant>> getRestaurantsByTags(@Query("long") Double longitude, @Query("lat") Double latitude, @Query("tag") String... tag);

        @GET("restaurant/{id}")
        Observable<Restaurant> getRestaurant(@Path("id") int id);

        @GET("restaurant/{id}/menu")
        Observable<List<MenuSection>> getRestaurantMenu(@Path("id") int id);

        @GET("restaurant/{id}/review")
        Observable<List<UserReview>> getRestaurantReview(@Path("id") int id);

        @GET("restaurant/{id}/hour")
        Observable<List<OpeningInterval>> getRestaurantOpenHours(@Path("id") int restaurantId, @Query("date") long date);

        @GET("restaurant/{id}/reservation")
        Observable<List<Reservation>> getReservations(@Path("id") int restaurantId, @Query("datetime") long datetime);

        @POST("restaurant/{id}/reservation/new")
        Observable<Response<Reservation>> saveNewReservation(@Path("id") int restaurantId, @Body Reservation reservation);

        @POST("restaurant/{id}/reservation/new?table=true")
        Observable<Response<Reservation>> saveNewReservationWithTable(@Path("id") int restaurantId, @Body Reservation reservation);

        @DELETE("restaurant/{id}/reservation/{reservation_id}")
        Observable<Response<BasicResponse>> cancelReservation(@Path("id") int restaurantId, @Path("reservation_id") int reservationId);

        // Restaurant list end-points
        @GET("restaurant/list/{id}")
        Observable<RestaurantList> getRestaurantList(@Path("id") int listId, @Query("long") Double longitude, @Query("lat") Double latitude);


        // Zone content delivery
        @GET("location/search")
        Observable<ZoneSearchResponse> searchZones(@Query("name") String name, @Query("long") Double longitude, @Query("lat") Double latitude);

        // Tag content delivery
        @GET("tag")
        Observable<List<Tag>> getTags();

        // Central Search provider
        @GET("search")
        Observable<SearchResponse> search(@Query("q") String query);
    }
}
