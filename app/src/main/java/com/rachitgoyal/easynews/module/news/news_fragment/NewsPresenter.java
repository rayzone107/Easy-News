package com.rachitgoyal.easynews.module.news.news_fragment;

import android.util.Log;

import com.rachitgoyal.easynews.MyApplication;
import com.rachitgoyal.easynews.model.news.Article;
import com.rachitgoyal.easynews.model.news.Response;
import com.rachitgoyal.easynews.network.ApiService;
import com.rachitgoyal.easynews.network.RetrofitSingleton;
import com.rachitgoyal.easynews.persistence.EasyNewsRepository;
import com.rachitgoyal.easynews.util.Constants;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Rachit Goyal on 11/01/19.
 */
public class NewsPresenter implements NewsContract.Presenter {

    private NewsContract.View mView;
    private EasyNewsRepository mEasyNewsRepository;

    NewsPresenter(NewsContract.View view) {
        mView = view;
        mEasyNewsRepository = new EasyNewsRepository(MyApplication.getInstance());
    }

    @Override
    public void getNews(String category) {
        switch (category) {
            case Constants.NEWS_CATEGORY.NEWS:
                getTopHeadlines();
                break;
            case Constants.NEWS_CATEGORY.NATIONAL:
                getNewsByCountry(Constants.NEWS_COUNTRY.INDIA);
                break;
            case Constants.NEWS_CATEGORY.INTERNATIONAL:
                getNewsByCountry(Constants.NEWS_COUNTRY.USA);
                break;
            default:
                getNewsByCategory(category);
                break;
        }
    }

    private void getTopHeadlines() {
        mView.showLoading(true);
        RetrofitSingleton.get().getNewsRetrofit().create(ApiService.class)
                .getTopHeadlines(Constants.NEWS_SOURCE_GOOGLE_NEWS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response response) {
                        mView.showLoading(false);
                        mView.displayArticles(response.getArticles());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoading(false);
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void getNewsByCountry(final String countryCode) {
        mView.showLoading(true);
        RetrofitSingleton.get().getNewsRetrofit().create(ApiService.class)
                .getNewsByCountry(countryCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response response) {
                        mView.showLoading(false);
                        mView.displayArticles(response.getArticles());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoading(false);
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getNewsByCategory(final String category) {
        mView.showLoading(true);
        RetrofitSingleton.get().getNewsRetrofit().create(ApiService.class)
                .getNewsByCategory(category, Constants.NEWS_COUNTRY.INDIA)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response response) {
                        mView.showLoading(false);
                        mView.displayArticles(response.getArticles());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoading(false);
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void addOrRemoveArticle(Article article, boolean toBeAdded) {
        if (toBeAdded && mEasyNewsRepository.getArticlesByTitle(article.getTitle()).isEmpty()) {
            mEasyNewsRepository.insertArticle(article);
        } else if (!toBeAdded) {
            mEasyNewsRepository.deleteByTitle(article.getTitle());
        }
    }

    @Override
    public void getFavourites() {
        mView.showLoading(false);
        List<Article> articles = mEasyNewsRepository.getAllArticles();
        Log.d("CHECK-APP", "getFavourites: articles = " + articles.size());
        if (articles.isEmpty()) {
            Log.d("CHECK-APP", "getFavourites: ");
            mView.showEmptyFavourites();
        } else {
            mView.displayArticles(articles);
        }
    }
}
