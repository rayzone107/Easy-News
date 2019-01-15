package com.rachitgoyal.easynews.network;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rachit Goyal on 11/01/19.
 */
public class RetrofitSingleton {

    private static final String NEWS_BASE_URL = "https://newsapi.org/v2/";
    private static final String WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static RetrofitSingleton INSTANCE;
    private Retrofit mNewsRetrofit;
    private Retrofit mWeatherRetrofit;

    public static RetrofitSingleton get() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            throw new IllegalStateException("Singleton not initialized");
        }
    }

    public Retrofit getNewsRetrofit() {
        return mNewsRetrofit;
    }

    public Retrofit getWeatherRetrofit() {
        return mWeatherRetrofit;
    }

    private RetrofitSingleton(Context context) {
        mNewsRetrofit = getRetrofit(getGson(), getOkHttpClient(getHttpCache(context)), true);
        mWeatherRetrofit = getRetrofit(getGson(), getOkHttpClient(getHttpCache(context)), false);
    }

    public static void init(Context context) {
        INSTANCE = new RetrofitSingleton(context);
    }

    private Cache getHttpCache(Context appContext) {
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(appContext.getCacheDir(), cacheSize);
    }

    private Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder().setLenient();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    private OkHttpClient getOkHttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();

        client.connectTimeout(20, TimeUnit.SECONDS);
        client.readTimeout(20, TimeUnit.SECONDS);
        client.addInterceptor(new HttpInterceptor());

        client.cache(cache);
        client.retryOnConnectionFailure(true);

        return client.build();
    }

    private Retrofit getRetrofit(Gson gson, OkHttpClient okHttpClient, boolean isNews) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(isNews ? NEWS_BASE_URL : WEATHER_BASE_URL)
                .client(okHttpClient)
                .build();
    }
}
