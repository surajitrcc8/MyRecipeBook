package com.example.android.recipebook;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.recipebook.model.Recipe;
import com.example.android.recipebook.util.RecipeUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by surajitbiswas on 8/19/17.
 */
@RunWith(AndroidJUnit4.class)
public class StepListActivityScreenTest {
    private ArrayList<Recipe>mRecipes;
    @Rule
    public ActivityTestRule<StepListActivity> stepListActivityTestRule = new ActivityTestRule<>(StepListActivity.class,false,false);

    @Test
    public void stepList_ScreenTest(){
        Type listType = new TypeToken<ArrayList<Recipe>>() {
        }.getType();
        mRecipes = new GsonBuilder().create().fromJson(RecipeUtil.getRecipe(), listType);
        Intent intent = new Intent();
        intent.putExtra("recipe",mRecipes.get(0));
        stepListActivityTestRule.launchActivity(intent);
        onView(withId(R.id.rv_step_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,
                click()));
        onView(withId(R.id.tv_step_instruction)).check(matches(withText("Recipe Introduction")));
    }
}
