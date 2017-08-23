package com.example.android.recipebook.service;

import com.example.android.recipebook.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by surajitbiswas on 8/22/17.
 */

public interface RecipeServiceAPI {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
