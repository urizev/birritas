package com.urizev.birritas.view.brewery;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.urizev.birritas.app.base.BaseActivity;

public class BreweryActivity extends BaseActivity {
    public static final String EXTRA_BREWERY_ID = "breweryId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String brewery = getIntent().getStringExtra(EXTRA_BREWERY_ID);
        if (TextUtils.isEmpty(brewery)) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, BreweryFragment.newInstance(brewery))
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
