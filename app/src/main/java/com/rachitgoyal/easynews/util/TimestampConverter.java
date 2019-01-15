package com.rachitgoyal.easynews.util;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Rachit Goyal on 11/01/19.
 */
public class TimestampConverter {

    @TypeConverter
    Date fromTimeStamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    Long dateToTimeStamp(Date date) {
        return date.getTime();
    }
}
