package com.urizev.birritas.view.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.urizev.birritas.R;
import com.urizev.birritas.domain.usecases.FavoriteBeersUseCase;

import javax.inject.Inject;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;

public class BeerListWidget extends AppWidgetProvider {

    @Inject
    FavoriteBeersUseCase mFavoriteBeersUseCase;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        int widgetText = BeerListWidgetConfigureActivity.loadPref (context, appWidgetId);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.beer_list_widget_loading);
        if (widgetText <= 0) {
            views.setViewVisibility(R.id.widget_title, View.GONE);
        }
        else {
            views.setViewVisibility(R.id.widget_title, View.VISIBLE);
            views.setTextViewText(R.id.widget_title, context.getText(widgetText));
        }

        Intent intent = new Intent(context, BeerListWidgetUpdateService.class);
        intent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
        context.startService(intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            BeerListWidgetConfigureActivity.deletePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

