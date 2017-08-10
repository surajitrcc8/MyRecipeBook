package com.example.android.recipebook;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.recipebook.databinding.ActivityRecipeDetailsBinding;
import com.example.android.recipebook.model.Ingredient;
import com.example.android.recipebook.model.Recipe;

import java.util.ArrayList;

public class RecipeDetailsActivity extends AppCompatActivity {

    private Recipe mRecipe;
    private ArrayList<Ingredient> mIngredients;
    private ActivityRecipeDetailsBinding mActivityRecipeDetailsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityRecipeDetailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_recipe_details);
        Intent intent = getIntent();
        mRecipe = (Recipe)intent.getParcelableExtra(getString(R.string.RECIPE));
        mIngredients = mRecipe.getIngredients();
        StringBuilder sb = new StringBuilder();
        for (Ingredient ingredient : mIngredients){
            String prefix = " ";
            sb.append(ingredient.getQuantity());
            sb.append(prefix);
            sb.append(ingredient.getMeasure());
            sb.append(prefix);
            sb.append(ingredient.getIngredient());
            sb.append(", ");

        }
        //Delete the last comma
        sb.deleteCharAt(sb.length()-2);

        mActivityRecipeDetailsBinding.tvIngredients.setText(sb);
    }
}
