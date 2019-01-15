package com.rachitgoyal.easynews.module.news.news_adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rachitgoyal.easynews.R;
import com.rachitgoyal.easynews.model.news.Article;

import java.util.List;

/**
 * Created by Rachit Goyal on 11/01/19.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private List<Article> mArticles;
    private String mType;
    private NewsOnClickListener mListener;

    public NewsAdapter(List<Article> articles, String type, NewsOnClickListener listener) {
        mArticles = articles;
        mType = type;
        mListener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_article, parent, false);
        return new NewsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int position) {
        newsViewHolder.bind(mArticles.get(position), mType, mListener);
    }

    @Override
    public int getItemCount() {
        return mArticles == null || mArticles.isEmpty() ? 0 : mArticles.size();
    }

    public void addArticles(List<Article> articles) {
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    public void resetArticles(List<Article> articles) {
        mArticles.clear();
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    public void clear() {
        mArticles.clear();
        notifyDataSetChanged();
    }

    public interface NewsOnClickListener {
        void onBookmarkClicked(Article article, boolean isChecked);

        void onArticleClicked(String url);
    }
}
