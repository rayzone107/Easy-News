package com.rachitgoyal.easynews.persistence;

import android.app.Application;

import com.rachitgoyal.easynews.model.news.Article;

import java.util.List;

/**
 * Created by Rachit Goyal on 11/01/19.
 */
public class EasyNewsRepository {
    private ArticleDao mArticlesDao;

    public EasyNewsRepository(Application application) {
        EasyNewsDatabase db = EasyNewsDatabase.getDatabase(application);
        mArticlesDao = db.articlesDao();
    }

    public void insertArticle(Article article) {
        mArticlesDao.insert(article);
    }

    public void insert(List<Article> articles) {
        mArticlesDao.insert(articles);
    }

    public List<Article> getAllArticles() {
        return mArticlesDao.getAllArticles();
    }

    public List<Article> getArticlesByTitle(String title) {
        return mArticlesDao.getArticlesByTitle(title);
    }

    public void updateArticle(Article article) {
        mArticlesDao.updateArticle(article);
    }

    public void delete(Article article) {
        mArticlesDao.delete(article);
    }

    public void deleteByTitle(String title) {
        mArticlesDao.deleteByTitle(title);
    }

    public void delete(List<Article> articles) {
        mArticlesDao.delete(articles);
    }
}
