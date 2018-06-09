package com.urizev.birritas.view.beer;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
