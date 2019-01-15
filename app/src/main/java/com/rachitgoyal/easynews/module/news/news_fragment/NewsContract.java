package com.rachitgoyal.easynews.module.news.news_fragment;

import com.rachitgoyal.easynews.model.news.Article;

import java.util.List;

/**
 * Created by Rachit Goyal on 11/01/19.
 */
public interface NewsContract {

    interface View {

        void displayArticles(List<Article> articles);

        void showError(String message);

        void showEmptyFavourites();

        void showLoading(boolean isLoading);
    }

    interface Presenter {

        void getNews(String category);

        void addOrRemoveArticle(Article article, boolean toBeAdded);

        void getFavourites();
    }
}
