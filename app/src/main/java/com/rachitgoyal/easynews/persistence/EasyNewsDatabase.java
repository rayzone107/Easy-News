package com.rachitgoyal.easynews.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.rachitgoyal.easynews.model.news.Article;

/**
 * Created by Rachit Goyal on 11/01/19.
 */
@Database(entities = {Article.class}, version = 1, exportSchema = false)
abstract class EasyNewsDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "EASY_NEWS_DB";

    private static volatile EasyNewsDatabase INSTANCE;

    abstract ArticleDao articlesDao();

    static EasyNewsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EasyNewsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EasyNewsDatabase.class, DATABASE_NAME).allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
