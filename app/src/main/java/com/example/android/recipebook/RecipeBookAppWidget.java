package com.example.android.recipebook;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeBookAppWidget extends AppWidgetProvider {
    private static final String TAG = RecipeBookAppWidget.class.getSimpleName();
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views;
        Bundle bundle = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = bundle.getInt(appWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        Log.d(TAG,"Width is " + width);
        if(width < 300){
            views = getSimpleWidget(context,appWidgetManager);
        }else{
            views = getListWidget(context);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews getListWidget(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_recipe_list_view);
        Intent adapterIntent = new Intent(context,RecipeWidgetService.class);
        views.setRemoteAdapter(R.id.lv_recipe_widget,adapterIntent);

        Intent intent = new Intent(context,StepListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.lv_recipe_widget,pendingIntent);

        return views;
    }

    private static RemoteViews getSimpleWidget(Context context, AppWidgetManager appWidgetManager) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_book_app_widget);
        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.iv_recipe_widget,pendingIntent);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
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

