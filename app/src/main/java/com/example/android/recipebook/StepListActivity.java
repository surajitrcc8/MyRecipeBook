package com.example.android.recipebook;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.recipebook.adapter.StepListAdapter;
//import com.example.android.recipebook.databinding.ActivityRecipeDetailsBinding;
import com.example.android.recipebook.model.Ingredient;
import com.example.android.recipebook.model.Recipe;
import com.example.android.recipebook.model.Step;
import com.example.android.recipebook.util.RecipeUtil;

import java.util.ArrayList;

public class StepListActivity extends AppCompatActivity implements StepListAdapter.OnClickStepItem {

    private Recipe mRecipe;
    private ArrayList<Ingredient> mIngredients;
    //private ActivityRecipeDetailsBinding mActivityRecipeDetailsBinding;
    private GridLayoutManager mGridLayoutManager;
    private StepListAdapter mStepListAdapter;
    private TextView mIngredientsTextView;
    private RecyclerView mStepListRecyclerView;
    private boolean isTwoPane = false;
    private FragmentStepDetails fragmentStepDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        mIngredientsTextView = (TextView) findViewById(R.id.tv_ingredients);
        mStepListRecyclerView = (RecyclerView) findViewById(R.id.rv_step_list);
        Intent intent = getIntent();
        mRecipe = (Recipe) intent.getParcelableExtra(getString(R.string.RECIPE));
        prepareDetailsScreen(mRecipe);
        if (findViewById(R.id.ll_container) != null) {
            isTwoPane = true;
            prepareTwoPaneActivity(null);
        } else {
            isTwoPane = false;
        }

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


//        mActivityRecipeDetailsBinding.tvIngredients.setText(sb);
//        mActivityRecipeDetailsBinding.rvStepList.setLayoutManager(mGridLayoutManager);
//        mActivityRecipeDetailsBinding.rvStepList.setAdapter(mStepListAdapter);

        mIngredientsTextView.setText(sb);
        mStepListRecyclerView.setLayoutManager(mGridLayoutManager);
        mStepListRecyclerView.setAdapter(mStepListAdapter);
    }

    @Override
    public void onStepItemClicked(Step step) {
        Toast.makeText(this, "shortDescription is " + step.getShortDescription(), Toast.LENGTH_SHORT).show();
        if (!isTwoPane) {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra(getString(R.string.STEP), step);
            startActivity(intent);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragmentStepDetails)
                    .commit();
            prepareTwoPaneActivity(step);
        }
    }
}
