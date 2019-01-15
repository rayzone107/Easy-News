package com.rachitgoyal.easynews.util;

/**
 * Created by Rachit Goyal on 11/01/19.
 */
public class Constants {

    public final static String NEWS_SOURCE_GOOGLE_NEWS = "google-news";

    public interface NEWS_COUNTRY {
        String INDIA = "in";
        String USA = "us";
    }

    public interface NEWS_CATEGORY {
        String NEWS = "news";
        String FAVOURITES = "favourites";
        String NATIONAL = "national";
        String INTERNATIONAL = "international";
        String BUSINESS = "business";
        String ENTERTAINMENT = "entertainment";
        String HEALTH = "health";
        String SPORTS = "sports";
        String TECHNOLOGY = "technology";
    }

    public interface ACTION {
        String FAVOURITE_ADDED = "FAVOURITE_ADDED";
    }

    public interface EXTRAS {
        String IS_CHECKED = "IS_CHECKED";
        String CATEGORY = "CATEGORY";
    }

    public interface REQUEST_CODE {
        int LOCATION_CODE = 100;
    }

    public interface WEATHER_CITY {
        String MUMBAI = "Mumbai";
        String DELHI = "Delhi";
        String KOLKATA = "Kolkata";
        String CHENNAI = "Chennai";
        String HYDERABAD = "Hyderabad";
        String PUNE = "Pune";
    }
}
