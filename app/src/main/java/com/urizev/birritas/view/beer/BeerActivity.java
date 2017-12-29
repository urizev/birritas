package com.urizev.birritas.view.beer;

import android.os.Bundle;
import android.text.TextUtils;

import com.urizev.birritas.app.base.BaseActivity;

public class BeerActivity extends BaseActivity {
    public static final String EXTRA_BEER_ID = "beerId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String beerId = getIntent().getStringExtra(EXTRA_BEER_ID);
        if (TextUtils.isEmpty(beerId)) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, BeerFragment.newInstance(beerId))
                    .commit();
        }
    }
}
