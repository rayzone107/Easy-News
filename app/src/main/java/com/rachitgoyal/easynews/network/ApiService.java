package com.rachitgoyal.easynews.network;

import com.rachitgoyal.easynews.model.news.Response;
import com.rachitgoyal.easynews.model.weather.WeatherResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Rachit Goyal on 11/01/19.
 */
public interface ApiService {

    @GET("top-headlines")
    Observable<Response> getTopHeadlines(@Query("sources") String source);

    @GET("top-headlines")
    Observable<Response> getNewsByCountry(@Query("country") String country);

    @GET("top-headlines")
    Observable<Response> getNewsByCategory(@Query("category") String category,
                                           @Query("country") String country);

    @GET("weather")
    Observable<WeatherResponse> getWeatherByCityName(@Query("q") String cityName,
                                                     @Query("units") String units,
                                                     @Query("apiKey") String apiKey);
}
