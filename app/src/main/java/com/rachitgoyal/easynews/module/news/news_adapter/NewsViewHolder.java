package com.rachitgoyal.easynews.module.news.news_adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rachitgoyal.easynews.MyApplication;
import com.rachitgoyal.easynews.R;
import com.rachitgoyal.easynews.model.news.Article;
import com.rachitgoyal.easynews.persistence.EasyNewsRepository;
import com.rachitgoyal.easynews.util.Constants;
import com.rachitgoyal.easynews.util.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rachit Goyal on 11/01/19.
 */
class NewsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.news_cv)
    CardView mNewsCV;

    @BindView(R.id.image_iv)
    ImageView mImageIV;

    @BindView(R.id.title_tv)
    TextView mTitleTV;

    @BindView(R.id.description_tv)
    TextView mDescriptionTV;

    @BindView(R.id.bookmark_tv)
    TextView mBookmarkTV;

    NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bind(final Article article, String type, final NewsAdapter.NewsOnClickListener listener) {

        mNewsCV.setOnClickListener(v -> {
            listener.onArticleClicked(article.getUrl());
        });

        final EasyNewsRepository easyNewsRepository = new EasyNewsRepository(MyApplication.getInstance());

        if (article.getUrlToImage() == null || article.getUrlToImage().isEmpty()) {
            mImageIV.setVisibility(View.GONE);
        } else {
            GlideApp.with(mImageIV.getContext()).
                    load(article.getUrlToImage()).
                    placeholder(R.drawable.news_placeholder).
                    into(mImageIV);
        }
        mTitleTV.setText(article.getTitle());
        if (article.getDescription() == null || article.getDescription().isEmpty()) {
            mDescriptionTV.setVisibility(View.GONE);
        } else {
            mDescriptionTV.setText(article.getDescription());
        }

        if (type.equals(Constants.NEWS_CATEGORY.FAVOURITES) || !easyNewsRepository.getArticlesByTitle(article.getTitle()).isEmpty()) {
            setBookmark(true);
        }

        mBookmarkTV.setOnClickListener(v -> {
            boolean toBeChecked = easyNewsRepository.getArticlesByTitle(article.getTitle()).isEmpty();
            setBookmark(toBeChecked);
            listener.onBookmarkClicked(article, toBeChecked);
        });
    }

    private void setBookmark(boolean isChecked) {
        mBookmarkTV.setCompoundDrawablesWithIntrinsicBounds(isChecked ? R.drawable.ic_bookmark_checked : R.drawable.ic_bookmark_unchecked,
                0, 0, 0);
        mBookmarkTV.setText(isChecked ? MyApplication.getInstance().getString(R.string.bookmarked) :
                MyApplication.getInstance().getString(R.string.bookmark));
    }
}
