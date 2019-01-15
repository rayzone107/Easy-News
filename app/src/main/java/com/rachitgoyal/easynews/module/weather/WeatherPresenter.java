package com.rachitgoyal.easynews.module.weather;

import android.util.Log;

import com.rachitgoyal.easynews.BuildConfig;
import com.rachitgoyal.easynews.model.weather.WeatherResponse;
import com.rachitgoyal.easynews.network.ApiService;
import com.rachitgoyal.easynews.network.RetrofitSingleton;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Rachit Goyal on 15/01/19.
 */
public class WeatherPresenter implements WeatherContract.Presenter {

    private WeatherContract.View mView;

    WeatherPresenter(WeatherContract.View view) {
        mView = view;
    }

    @Override
    public void getWeatherForCity(String cityName) {
        RetrofitSingleton.get().getWeatherRetrofit().create(ApiService.class)
                .getWeatherByCityName(cityName + ",in", "metric", BuildConfig.WEATHER_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(WeatherResponse weatherResponse) {
                        mView.showWeatherForCity(cityName, weatherResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("WEATHER-CHECK", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
