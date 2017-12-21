package com.urizev.birritas.view.main;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.urizev.birritas.R;
import com.urizev.birritas.app.base.BaseActivity;
import com.urizev.birritas.view.favorites.FavoriteBeersFragment;
import com.urizev.birritas.view.featured.FeaturedFragment;
import com.urizev.birritas.view.nearby.NearbyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_pager) ViewPager viewPager;
    @BindView(R.id.main_navigation) BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.setAdapter(new MainPageAdapter(getSupportFragmentManager()));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        int position = viewPager.getCurrentItem();
        switch (item.getItemId()) {
            case R.id.navigation_featured:
                position = MainPageAdapter.POSITION_FEATURED;
                break;
            case R.id.navigation_map:
                position = MainPageAdapter.POSITION_MAP;
                break;
            case R.id.navigation_favorites:
                position = MainPageAdapter.POSITION_FAVORITES;
                break;
        }

        viewPager.setCurrentItem(position, false);

        return true;
    };

    private class MainPageAdapter extends FragmentStatePagerAdapter {
        static final int POSITION_FEATURED = 0;
        static final int POSITION_MAP = 1;
        static final int POSITION_FAVORITES = 2;

        MainPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case POSITION_FEATURED:
                    return new FeaturedFragment();
                case POSITION_MAP:
                    return new NearbyFragment();
                case POSITION_FAVORITES:
                    return new FavoriteBeersFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
