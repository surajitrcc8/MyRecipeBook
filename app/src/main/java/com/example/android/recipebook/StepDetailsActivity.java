package com.example.android.recipebook;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.android.recipebook.model.Step;
import com.example.android.recipebook.util.RecipeUtil;

import java.util.ArrayList;

public class StepDetailsActivity extends AppCompatActivity implements FragmentStepDetails.OnButtonClick{
    private FragmentStepDetails fragmentStepDetails;
    private ArrayList<Step>mSteps;
    private Step mStep;
    private int mIndex = -1;

    private ActionBar mActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mDecorView = getWindow().getDecorView();
        Bundle bundle = new Bundle();
        mSteps = getIntent().getParcelableArrayListExtra(getString(R.string.STEPS));
        mStep = getIntent().getParcelableExtra(getString(R.string.STEP));
        bundle.putParcelable(getString(R.string.STEP),mStep);
        bundle.putInt(getString(R.string.STEPS),mSteps.size());
        mActionBar = getSupportActionBar();
        if(mActionBar != null){
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(mStep.getShortDescription());
        }

        if(!RecipeUtil.isPotraite(this)){
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }else{

        }

        setContentView(R.layout.activity_step_details);

        if(savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.STEP_INDEX))){
            mIndex = savedInstanceState.getInt(getString(R.string.STEP_INDEX));
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentStepDetails = (FragmentStepDetails)fragmentManager.findFragmentByTag(getString(R.string.TAG_STEP_DETAILS_FRAGMENT));
        if(fragmentStepDetails == null) {
            fragmentStepDetails = new FragmentStepDetails();
            mIndex = mStep.getId();
            fragmentStepDetails.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragmentStepDetails, getString(R.string.TAG_STEP_DETAILS_FRAGMENT))
                    .commit();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
    public void enableDisableButton(){
        if(mIndex == 0){
            fragmentStepDetails.setEnableButton(R.string.previous,false);
            fragmentStepDetails.setEnableButton(R.string.next,true);
        }else if(mIndex == (mSteps.size() - 1)){
            fragmentStepDetails.setEnableButton(R.string.previous,true);
            fragmentStepDetails.setEnableButton(R.string.next,false);
        }else{
            fragmentStepDetails.setEnableButton(R.string.previous,true);
            fragmentStepDetails.setEnableButton(R.string.next,true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.STEP_INDEX),mIndex);
    }

    @Override
    public void onButtonClicked(View view) {
        switch (view.getId()){
            case R.id.b_prev:
                if(mIndex > 0) {
                    mIndex--;
                }
                break;
            case R.id.b_next:
                if(mIndex < (mSteps.size() - 1)) {
                    mIndex++;
                }
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.STEP),mSteps.get(mIndex));
        bundle.putInt(getString(R.string.STEPS),mSteps.size());
        fragmentStepDetails.prepareAndPlayExoPlayer(Uri.parse(mSteps.get(mIndex).getVideoURL()));
        fragmentStepDetails.showStepInstruction(mSteps.get(mIndex).getDescription(),bundle);
        if(mActionBar != null){
            mActionBar.setTitle(mSteps.get(mIndex).getShortDescription());
        }

        enableDisableButton();
    }
}
