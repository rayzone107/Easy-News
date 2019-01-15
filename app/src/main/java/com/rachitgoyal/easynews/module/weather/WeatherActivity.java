package com.rachitgoyal.easynews.module.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rachitgoyal.easynews.R;
import com.rachitgoyal.easynews.model.weather.WeatherResponse;
import com.rachitgoyal.easynews.module.news.NewsActivity;
import com.rachitgoyal.easynews.util.Constants;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends FragmentActivity implements WeatherContract.View, OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener, ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    private WeatherContract.Presenter mPresenter;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private double mCurrentLatitude;
    private double mCurrentLongitude;

    public static Intent getActivityIntent(Context context) {
        return new Intent(context, WeatherActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setNumUpdates(1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.REQUEST_CODE.LOCATION_CODE);
        }

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    mCurrentLongitude = location.getLongitude();
                    mCurrentLatitude = location.getLatitude();
                }
                addMarkerForCurrentLocation();
            }
        };

        mPresenter = new WeatherPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mPresenter.getWeatherForCity(Constants.WEATHER_CITY.MUMBAI);
        mPresenter.getWeatherForCity(Constants.WEATHER_CITY.DELHI);
        mPresenter.getWeatherForCity(Constants.WEATHER_CITY.CHENNAI);
        mPresenter.getWeatherForCity(Constants.WEATHER_CITY.KOLKATA);
        mPresenter.getWeatherForCity(Constants.WEATHER_CITY.HYDERABAD);
        mPresenter.getWeatherForCity(Constants.WEATHER_CITY.PUNE);

        LatLng india = new LatLng(20.5937, 78.9629);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(india, 4.7f));
        if (isLocationPermissionGiven()) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMyLocationButtonClickListener(() -> {
            if (!isLocationPermissionGiven()) {
                ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.REQUEST_CODE.LOCATION_CODE);
            }
            return false;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE.LOCATION_CODE) {
            if (grantResults.length <= 0 ||
                    grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                break;
            case R.id.nav_weather:
                break;
            case R.id.nav_news:
                goToNews(Constants.NEWS_CATEGORY.SPORTS);
                break;
            case R.id.nav_national:
                goToNews(Constants.NEWS_CATEGORY.NATIONAL);
                break;
            case R.id.nav_international:
                goToNews(Constants.NEWS_CATEGORY.INTERNATIONAL);
                break;
            case R.id.nav_sports:
                goToNews(Constants.NEWS_CATEGORY.SPORTS);
                break;
            case R.id.nav_entertainment:
                goToNews(Constants.NEWS_CATEGORY.ENTERTAINMENT);
                break;
            case R.id.nav_business:
                goToNews(Constants.NEWS_CATEGORY.BUSINESS);
                break;
            case R.id.nav_technology:
                goToNews(Constants.NEWS_CATEGORY.TECHNOLOGY);
                break;
            case R.id.nav_health:
                goToNews(Constants.NEWS_CATEGORY.HEALTH);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToNews(String category) {
        startActivity(NewsActivity.getActivityIntent(WeatherActivity.this, category));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!isLocationPermissionGiven()) {
            return;
        }

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location == null) {
                        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                        return;
                    }
                    mCurrentLatitude = location.getLatitude();
                    mCurrentLongitude = location.getLongitude();
                    addMarkerForCurrentLocation();
                });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLatitude = location.getLatitude();
        mCurrentLongitude = location.getLongitude();
        addMarkerForCurrentLocation();
    }

    private void addMarkerForCurrentLocation() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(mCurrentLatitude, mCurrentLongitude, 1);
            String cityName = addresses.get(0).getLocality();
            mPresenter.getWeatherForCity(cityName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isLocationPermissionGiven() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void showWeatherForCity(String cityName, WeatherResponse weatherResponse) {
        LatLng city = new LatLng(weatherResponse.getCoord().getLat(), weatherResponse.getCoord().getLon());
        MarkerOptions marker = new MarkerOptions().position(city).title(cityName + " - " + weatherResponse.getMain().getTemp() + "°");
        marker.icon(BitmapDescriptorFactory.defaultMarker(new SecureRandom().nextInt(360)));
        mMap.addMarker(marker);
    }
}
