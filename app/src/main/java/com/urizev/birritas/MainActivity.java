package com.urizev.birritas;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_pager) ViewPager viewPager;
    @BindView(R.id.main_navigation) BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        int res;
        switch (item.getItemId()) {
            case R.id.navigation_featured:
                res = R.string.title_featured;
                Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.navigation_map:
                res = R.string.title_map;
                Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.navigation_favorites:
                res = R.string.title_favorites;
                Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
    };

}
