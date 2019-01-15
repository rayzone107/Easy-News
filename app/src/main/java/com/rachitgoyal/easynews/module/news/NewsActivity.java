package com.rachitgoyal.easynews.module.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.rachitgoyal.easynews.R;
import com.rachitgoyal.easynews.module.base.BaseActivity;
import com.rachitgoyal.easynews.module.news.news_fragment.NewsFragment;
import com.rachitgoyal.easynews.module.weather.WeatherActivity;
import com.rachitgoyal.easynews.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    private NewsFragment mNewsFragment;
    private String mCategory;

    public static Intent getActivityIntent(Context context, String category) {
        Intent intent = new Intent(context, NewsActivity.class);
        intent.putExtra(Constants.EXTRAS.CATEGORY, category);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        getExtras(getIntent());

        NewsFragmentAdapter mNewsFragmentAdapter = new NewsFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mNewsFragmentAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
    }

    private void getExtras(Intent intent) {
        if (intent != null) {
            mCategory = intent.getStringExtra(Constants.EXTRAS.CATEGORY);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                break;
            case R.id.nav_news:
                mNewsFragment.changeCategory(Constants.NEWS_CATEGORY.NEWS);
                break;
            case R.id.nav_national:
                mNewsFragment.changeCategory(Constants.NEWS_CATEGORY.NATIONAL);
                break;
            case R.id.nav_international:
                mNewsFragment.changeCategory(Constants.NEWS_CATEGORY.INTERNATIONAL);
                break;
            case R.id.nav_sports:
                mNewsFragment.changeCategory(Constants.NEWS_CATEGORY.SPORTS);
                break;
            case R.id.nav_entertainment:
                mNewsFragment.changeCategory(Constants.NEWS_CATEGORY.ENTERTAINMENT);
                break;
            case R.id.nav_business:
                mNewsFragment.changeCategory(Constants.NEWS_CATEGORY.BUSINESS);
                break;
            case R.id.nav_technology:
                mNewsFragment.changeCategory(Constants.NEWS_CATEGORY.TECHNOLOGY);
                break;
            case R.id.nav_health:
                mNewsFragment.changeCategory(Constants.NEWS_CATEGORY.HEALTH);
                break;
            case R.id.nav_weather:
                goToWeather();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToWeather() {
        startActivity(WeatherActivity.getActivityIntent(NewsActivity.this));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }

    public class NewsFragmentAdapter extends FragmentPagerAdapter {
        NewsFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return NewsFragment.newInstance(Constants.NEWS_CATEGORY.FAVOURITES);
                default:
                    mNewsFragment = NewsFragment.newInstance(mCategory == null || mCategory.isEmpty() ? Constants.NEWS_CATEGORY.NEWS : mCategory);
                    return mNewsFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
