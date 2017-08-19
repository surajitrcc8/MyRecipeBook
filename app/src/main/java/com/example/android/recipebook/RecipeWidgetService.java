package com.example.android.recipebook;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.recipebook.model.Ingredient;
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
    public static Recipe mRecipe;
    ArrayList<Ingredient>mIngredients;

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
        ArrayList<Recipe>recipes = new GsonBuilder().create().fromJson(RecipeUtil.loadJSONFromAsset(context), listType);

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.RECIPE),Context.MODE_PRIVATE);
        String recipeName = sharedPreferences.getString(context.getString(R.string.RECIPE),"");
        for(Recipe recipe : recipes){
            if(recipe.getName().equals(recipeName)){
                mRecipe = recipe;
                mIngredients = recipe.getIngredients();
                break;
            }
        }
        if(recipeName.trim().length() <= 1){
            mRecipe = recipes.get(0);
            mIngredients = recipes.get(0).getIngredients();
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context,RecipeBookAppWidget.class);
        int []appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        RecipeBookAppWidget.setWidgetRecipeName(mRecipe.getName(),context,appWidgetManager,appWidgetIds);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return (mIngredients == null)? 0: mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_recipe_list_item);
        views.setTextViewText(R.id.tv_widget_recipe_name,mIngredients.get(i).getIngredient());
        views.setTextViewText(R.id.tv_widget_recipe_servings,String.valueOf(mIngredients.get(i).getQuantity()) + String.valueOf(mIngredients.get(i).getMeasure()));
        Bundle bundle = new Bundle();
        Intent fillinIntent = new Intent();
        bundle.putParcelable(context.getString(R.string.RECIPE),mRecipe);
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
