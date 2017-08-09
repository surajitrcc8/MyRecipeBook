package com.example.android.recipebook;

import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.recipebook.adapter.RecipeListAdapter;
import com.example.android.recipebook.model.Recipe;
import com.example.android.recipebook.util.RecipeLoader;
import com.example.android.recipebook.util.RecipeUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>>,RecipeListAdapter.OnRecipeItemClicked {


    private static final int PORTRAIT_SPAN = 1;
    private static final int LANDSCAPE_SPAN = 2;
    private RecipeListAdapter mRecipeListAdapter;
    private RecyclerView mRecipeListRecyclerView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Recipe> mRecipes = null;
    private static final String RECIPE_LIST = "recipelist";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecipeListRecyclerView = (RecyclerView)findViewById(R.id.rv_recipe_list);
        LoaderManager loaderManager = getSupportLoaderManager();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecipeListRecyclerView.setLayoutManager(new GridLayoutManager(this, PORTRAIT_SPAN));
        } else {
            mRecipeListRecyclerView.setLayoutManager(new GridLayoutManager(this, LANDSCAPE_SPAN));
        }
        mRecipeListAdapter = new RecipeListAdapter(this,this);
        mRecipeListRecyclerView.setAdapter(mRecipeListAdapter);
        if(savedInstanceState != null && savedInstanceState.containsKey(RECIPE_LIST)){
            mRecipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
            mRecipeListAdapter.setData(mRecipes);
        }else {
            if (loaderManager == null) {
                loaderManager.initLoader(RecipeLoader.LOAD_RECIPE_LIST, null, this);
            } else {
                loaderManager.restartLoader(RecipeLoader.LOAD_RECIPE_LIST, null, this);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST,mRecipes);
    }

    public static void success(){

    }
    public static void error(){

    }
    public static void showIndicator(){

    }
    public static void hideIndicator(){

    }

    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Recipe>>(this) {
            ArrayList<Recipe>mRecipe = null;
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(mRecipe != null){
                    deliverResult(mRecipe);
                }else{
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Recipe> loadInBackground() {
                Type listType = new TypeToken<ArrayList<Recipe>>() {
                }.getType();
                mRecipe = new GsonBuilder().create().fromJson(RecipeUtil.loadJSONFromAsset(getApplicationContext()), listType);
                return mRecipe;
            }

            @Override
            public void deliverResult(ArrayList<Recipe> data) {
                super.deliverResult(data);
                if(data != null){
                    mRecipe = data;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        if(data != null){
            Log.d(TAG , "Recipe list is " + data.size());
            mRecipeListAdapter.setData(data);
            mRecipes = data;
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

    }

    @Override
    public void onClickRecipeItem(Recipe recipe) {

    }
}
