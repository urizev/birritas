package com.urizev.birritas.view.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.urizev.birritas.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BeerListWidgetConfigureActivity extends Activity {
    private static final String PREFS_NAME = "com.urizev.birritas.view.widget.BeerListWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    static void savePref(Context context, int appWidgetId, int option) {
        context.getSharedPreferences(PREFS_NAME, 0)
                .edit()
                .putInt(PREF_PREFIX_KEY + appWidgetId, option)
                .apply();
    }

    static int loadPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId, -1);
    }

    public static void deletePref(Context context, int appWidgetId) {
        context.getSharedPreferences(PREFS_NAME, 0)
                .edit()
                .remove(PREF_PREFIX_KEY + appWidgetId)
                .apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.beer_list_widget_configure);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    @OnClick(R.id.featured)
    public void onFeaturedClicked() {
        onOptionClicked(R.string.title_featured);
    }

    @OnClick(R.id.favorites)
    public void onFavoritesClicked() {
        onOptionClicked(R.string.title_favorites);
    }

    public void onOptionClicked(int option) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        BeerListWidget.updateAppWidget(this, appWidgetManager, mAppWidgetId);

        savePref(this, mAppWidgetId, option);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

