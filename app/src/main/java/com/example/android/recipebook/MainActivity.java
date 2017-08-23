package com.example.android.recipebook;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.recipebook.adapter.RecipeListAdapter;
import com.example.android.recipebook.databinding.ActivityMainBinding;
import com.example.android.recipebook.model.Ingredient;
import com.example.android.recipebook.model.Recipe;
import com.example.android.recipebook.util.RecipeUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>>,RecipeListAdapter.OnRecipeItemClicked {


    private RecipeListAdapter mRecipeListAdapter;
    private RecyclerView mRecipeListRecyclerView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Recipe> mRecipes = null;
    private static final String RECIPE_LIST = "recipelist";
    private ProgressBar mRecipeListProgressBar;
    private TextView mRecipeListErrorTextView;
    private ActivityMainBinding mActivityMainBinding;
    private boolean isStop = false;
    private SimpleIdleResource mSimpleIdleResource;
    private static final int LOAD_RECIPE_LIST = 1;
    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);


        mRecipeListRecyclerView = mActivityMainBinding.rvRecipeList;
        mRecipeListProgressBar = mActivityMainBinding.pbRecipeList;
        mRecipeListErrorTextView = mActivityMainBinding.tvRecipeListError;
        LoaderManager loaderManager = getSupportLoaderManager();

        Log.d(TAG,"Tile span is " + RecipeUtil.getTileSpan(this));

        mRecipeListRecyclerView.setLayoutManager(new GridLayoutManager(this, RecipeUtil.getTileSpan(this)));
        mRecipeListAdapter = new RecipeListAdapter(this,this);
        mRecipeListRecyclerView.setAdapter(mRecipeListAdapter);
        getIdlingResource();
        if(savedInstanceState != null && savedInstanceState.containsKey(RECIPE_LIST)){
            mRecipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
            mRecipeListAdapter.setData(mRecipes);
        }else {
            Bundle bundle = new Bundle();
            bundle.putParcelable("Idle",mSimpleIdleResource);
            if (loaderManager == null) {
                loaderManager.initLoader(LOAD_RECIPE_LIST, bundle, this);
            } else {
                loaderManager.restartLoader(LOAD_RECIPE_LIST, bundle, this);
            }
        }
    }
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mSimpleIdleResource == null) {
            mSimpleIdleResource = new SimpleIdleResource();
        }
        return mSimpleIdleResource;
    }
    @Override
    protected void onStop() {
        super.onStop();
        isStop = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST,mRecipes);
    }

    public void success(){
        mRecipeListErrorTextView.setVisibility(View.INVISIBLE);
        hideIndicator();
    }
    public void error(){
        mRecipeListRecyclerView.setVisibility(View.INVISIBLE);
        mRecipeListErrorTextView.setVisibility(View.VISIBLE);
        hideIndicator();
    }
    public void showIndicator(){
        mRecipeListProgressBar.setVisibility(View.VISIBLE);
    }
    public void hideIndicator(){
        mRecipeListProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Recipe>>(this) {
            ArrayList<Recipe>mRecipe = null;
            SimpleIdleResource simpleIdleResource = null;
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args.getParcelable("Idle") != null){
                    simpleIdleResource = args.getParcelable("Idle");
                    if(simpleIdleResource!= null){
                        simpleIdleResource.setIdleState(false);
                    }
                }
                showIndicator();
                if(mRecipe != null){
                    deliverResult(mRecipe);
                }else{
                    forceLoad();
                }
            }
            @Override
            public ArrayList<Recipe> loadInBackground() {
                mRecipe = RecipeUtil.getRecipe(BASE_URL);
                return mRecipe;
            }

            @Override
            public void deliverResult(ArrayList<Recipe> data) {
                super.deliverResult(data);
                if(data != null){
                    success();
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
            success();
            mSimpleIdleResource.setIdleState(true);
        }else{
            error();
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

    }

    @Override
    public void onClickRecipeItem(Recipe recipe) {
        Intent intent = new Intent(this,StepListActivity.class);
        intent.putExtra(getString(R.string.RECIPE),recipe);
        startActivity(intent);
    }
    @VisibleForTesting
    public String getIngredients(){
        ArrayList<Ingredient> ingredients = mRecipes.get(0).getIngredients();
        StringBuilder sb = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            String prefix = " ";
            sb.append(ingredient.getQuantity());
            sb.append(prefix);
            sb.append(ingredient.getMeasure());
            sb.append(prefix);
            sb.append(ingredient.getIngredient());
            sb.append(", ");

        }
        //Delete the last comma
        sb.deleteCharAt(sb.length() - 2);
        return sb.toString();
    }
}
