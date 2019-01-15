package com.rachitgoyal.easynews.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.rachitgoyal.easynews.model.news.Article;

import java.util.List;

/**
 * Created by Rachit Goyal on 13/11/18.
 */
@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Article article);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Article> articles);

    @Query("SELECT * FROM Article")
    List<Article> getAllArticles();

    @Query("SELECT * FROM Article WHERE title = :title LIMIT 1")
    List<Article> getArticlesByTitle(String title);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateArticle(Article article);

    @Delete
    void delete(Article article);

    @Query("DELETE FROM Article where title = :title")
    void deleteByTitle(String title);

    @Delete
    void delete(List<Article> articles);
}
