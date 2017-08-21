package com.example.android.recipebook;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.recipebook.adapter.StepListAdapter;
import com.example.android.recipebook.model.Ingredient;
import com.example.android.recipebook.model.Recipe;
import com.example.android.recipebook.model.Step;
import com.example.android.recipebook.util.RecipeUtil;

import java.util.ArrayList;

public class StepListActivity extends AppCompatActivity implements StepListAdapter.OnClickStepItem {

    private Recipe mRecipe;
    private ArrayList<Ingredient> mIngredients;
    private GridLayoutManager mGridLayoutManager;
    private StepListAdapter mStepListAdapter;
    private TextView mIngredientsTextView;
    private RecyclerView mStepListRecyclerView;
    public boolean isTwoPane = false;
    private FragmentStepDetails fragmentStepDetails;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        mIngredientsTextView = (TextView) findViewById(R.id.tv_ingredients);
        mStepListRecyclerView = (RecyclerView) findViewById(R.id.rv_step_list);
        Intent intent = getIntent();
        mRecipe = (Recipe) intent.getParcelableExtra(getString(R.string.RECIPE));
        mActionBar = getSupportActionBar();
        if(mActionBar != null && mRecipe!=null){
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(mRecipe.getName());
        }
        prepareDetailsScreen(mRecipe);
        if (findViewById(R.id.ll_container) != null) {
            isTwoPane = true;
            prepareTwoPaneActivity(null);
        } else {
            isTwoPane = false;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.step_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }else if(item.getItemId() == R.id.menu_item_add_to_widget){
            //Save the selected recipe to shared preference
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.RECIPE),MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.RECIPE),mRecipe.getName());
            editor.apply();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            ComponentName componentName = new ComponentName(this,RecipeBookAppWidget.class);
            int []appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.lv_recipe_widget);

            RecipeBookAppWidget.setWidgetRecipeName(mRecipe.getName(),this,appWidgetManager,appWidgetIds);
        }
        return super.onOptionsItemSelected(item);
    }
    private void prepareTwoPaneActivity(Step step) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.STEP), (step == null) ? mRecipe.getSteps().get(0) : step);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentStepDetails = (FragmentStepDetails) fragmentManager.findFragmentByTag(getString(R.string.TAG_STEP_DETAILS_FRAGMENT));
        if (fragmentStepDetails == null) {
            fragmentStepDetails = new FragmentStepDetails();
            fragmentStepDetails.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragmentStepDetails, getString(R.string.TAG_STEP_DETAILS_FRAGMENT))
                    .commit();
        }


    }

    private void prepareDetailsScreen(Recipe mRecipe) {
        if(mRecipe != null) {
            mIngredients = mRecipe.getIngredients();
            mGridLayoutManager = new GridLayoutManager(this, RecipeUtil.getTileSpan(this));
            StringBuilder sb = new StringBuilder();
            for (Ingredient ingredient : mIngredients) {
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
            mStepListAdapter = new StepListAdapter(this);
            mStepListAdapter.setSteps(mRecipe.getSteps());
            mIngredientsTextView.setText(sb);
            mStepListRecyclerView.setLayoutManager(mGridLayoutManager);
            mStepListRecyclerView.setAdapter(mStepListAdapter);
        }
    }

    @Override
    public void onStepItemClicked(Step step,int index) {
        if (!isTwoPane) {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra(getString(R.string.STEP), step);
            intent.putParcelableArrayListExtra(getString(R.string.STEPS), mRecipe.getSteps());
            startActivity(intent);
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.STEP), (step == null) ? mRecipe.getSteps().get(0) : step);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentStepDetails = new FragmentStepDetails();
            fragmentStepDetails.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragmentStepDetails, getString(R.string.TAG_STEP_DETAILS_FRAGMENT))
                    .commit();

        }
    }


}
