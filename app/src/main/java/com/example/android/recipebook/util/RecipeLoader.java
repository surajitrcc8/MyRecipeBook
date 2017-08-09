package com.example.android.recipebook.util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.android.recipebook.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by surajitbiswas on 8/7/17.
 */

public class RecipeLoader implements LoaderManager.LoaderCallbacks {
    public static final int LOAD_RECIPE_LIST = 1;
    private Context mContext;

    public RecipeLoader(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Loader onCreateLoader(final int id, Bundle args) {
        switch(id){
            case LOAD_RECIPE_LIST:
                return new AsyncTaskLoader(mContext) {
                    ArrayList<Recipe> recipes = null;
                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        switch (id){
                            case LOAD_RECIPE_LIST:
                                if(recipes != null){
                                    deliverResult(recipes);
                                }else{
                                    forceLoad();
                                }
                                break;
                        }
                    }

                    @Override
                    public Object loadInBackground() {
                        switch (id) {
                            case LOAD_RECIPE_LIST:
                                Type listType = new TypeToken<ArrayList<Recipe>>() {
                                }.getType();
                                recipes = new GsonBuilder().create().fromJson(RecipeUtil.loadJSONFromAsset(mContext), listType);
                                return recipes;
                        }
                        return null;
                    }

                    @Override
                    public void deliverResult(Object data) {
                        super.deliverResult(data);
                        if(data != null){
                            if(data instanceof ArrayList){
                                recipes = (ArrayList<Recipe>) data;
                            }
                        }
                    }
                };
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()){
            case LOAD_RECIPE_LIST:

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
