package com.example.android.recipebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.recipebook.model.Recipe;
import com.example.android.recipebook.util.RecipeUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by surajitbiswas on 8/17/17.
 */

public class RecipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeWidgetFactory(this.getApplicationContext());
    }
}

class RecipeWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    ArrayList<Recipe>mRecipe;

    public RecipeWidgetFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Type listType = new TypeToken<ArrayList<Recipe>>() {
        }.getType();
        mRecipe = new GsonBuilder().create().fromJson(RecipeUtil.loadJSONFromAsset(context), listType);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return (mRecipe == null)? 0: mRecipe.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_recipe_list_item);
        views.setTextViewText(R.id.tv_widget_recipe_name,mRecipe.get(i).getName());
        views.setTextViewText(R.id.tv_widget_recipe_servings,String.valueOf(mRecipe.get(i).getServings()));
        Bundle bundle = new Bundle();
        Intent fillinIntent = new Intent();
        bundle.putParcelable(context.getString(R.string.RECIPE),mRecipe.get(i));
        fillinIntent.putExtras(bundle);
        views.setOnClickFillInIntent(R.id.ll_widget_recipe_list_container,fillinIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
