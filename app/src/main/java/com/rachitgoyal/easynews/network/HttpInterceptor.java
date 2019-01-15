package com.rachitgoyal.easynews.network;

import android.support.annotation.NonNull;

import com.rachitgoyal.easynews.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.addHeader("Authorization", BuildConfig.NEWS_KEY);
        builder.addHeader("Content-Type", "application/json; charset=utf-8");
        request = builder.build();
        return chain.proceed(request);
    }
}