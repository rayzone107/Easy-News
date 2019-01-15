package com.rachitgoyal.easynews.module.weather;

import com.rachitgoyal.easynews.model.weather.WeatherResponse;

/**
 * Created by Rachit Goyal on 15/01/19.
 */
public interface WeatherContract {

    interface View {

        void showWeatherForCity(String cityName, WeatherResponse weatherResponse);
    }

    interface Presenter {

        void getWeatherForCity(String cityName);
    }
}
