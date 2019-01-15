package com.rachitgoyal.easynews.module.news.news_fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rachitgoyal.easynews.R;
import com.rachitgoyal.easynews.model.news.Article;
import com.rachitgoyal.easynews.module.base.BaseFragment;
import com.rachitgoyal.easynews.module.news.news_adapter.NewsAdapter;
import com.rachitgoyal.easynews.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rachit Goyal on 11/01/19.
 */
public class NewsFragment extends BaseFragment implements NewsContract.View, NewsAdapter
        .NewsOnClickListener {

    @BindView(R.id.news_rv)
    RecyclerView mNewsRV;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.empty_favourites_tv)
    TextView mEmptyFavouritesTV;

    @BindView(R.id.reload_srl)
    SwipeRefreshLayout mReloadSRL;

    private NewsAdapter mNewsAdapter;
    private List<Article> mArticles;
    private NewsContract.Presenter mPresenter;
    private FavouriteAddListener mFavouriteAddListener;
    private String mCategory;

    public NewsFragment() {
        mArticles = new ArrayList<>();
        mPresenter = new NewsPresenter(this);
    }

    public static NewsFragment newInstance(String category) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRAS.CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            mCategory = getArguments().getString(Constants.EXTRAS.CATEGORY);
        }

        mFavouriteAddListener = new FavouriteAddListener();
        mNewsAdapter = new NewsAdapter(mArticles, mCategory, this);
        mNewsRV.setAdapter(mNewsAdapter);
        mNewsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        if (mCategory != null && mCategory.equals(Constants.NEWS_CATEGORY.FAVOURITES)) {
            mPresenter.getFavourites();
        } else {
            mPresenter.getNews(mCategory == null || mCategory.isEmpty() ? Constants.NEWS_CATEGORY
                    .NEWS : mCategory);
        }

        mReloadSRL.setOnRefreshListener(() -> {
            if (mCategory != null && mCategory.equals(Constants.NEWS_CATEGORY.FAVOURITES)) {
                mPresenter.getFavourites();
            } else {
                mPresenter.getNews(mCategory);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            getActivity().registerReceiver(mFavouriteAddListener, new IntentFilter(Constants
                    .ACTION.FAVOURITE_ADDED));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() != null && mFavouriteAddListener != null) {
            getActivity().unregisterReceiver(mFavouriteAddListener);
        }
    }

    public void changeCategory(String category) {
        mCategory = category;
        mPresenter.getNews(category);
    }

    @Override
    public void displayArticles(List<Article> articles) {
        mReloadSRL.setRefreshing(false);
        mEmptyFavouritesTV.setVisibility(View.GONE);
        mNewsAdapter.resetArticles(articles);
        mNewsRV.scrollToPosition(0);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyFavourites() {
        mEmptyFavouritesTV.setVisibility(View.VISIBLE);
        mNewsAdapter.clear();
    }

    @Override
    public void showLoading(boolean isLoading) {
        mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBookmarkClicked(Article article, boolean isChecked) {
        mPresenter.addOrRemoveArticle(article, isChecked);
        sendFavouriteAddedBroadcast(isChecked);
    }

    @Override
    public void onArticleClicked(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void sendFavouriteAddedBroadcast(boolean isChecked) {
        if (getActivity() != null) {
            Intent intent = new Intent(Constants.ACTION.FAVOURITE_ADDED);
            intent.putExtra(Constants.EXTRAS.IS_CHECKED, isChecked);
            getActivity().sendBroadcast(intent);
        }
    }

    private class FavouriteAddListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mCategory.equals(Constants.NEWS_CATEGORY.FAVOURITES)) {
                mPresenter.getFavourites();
            }
        }
    }
}
