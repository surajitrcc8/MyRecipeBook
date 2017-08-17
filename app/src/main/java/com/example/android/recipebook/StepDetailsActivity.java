package com.example.android.recipebook;

import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.recipebook.model.Recipe;
import com.example.android.recipebook.model.Step;

import java.util.ArrayList;

public class StepDetailsActivity extends AppCompatActivity implements FragmentStepDetails.OnButtonClick{
    private FragmentStepDetails fragmentStepDetails;
    private ArrayList<Step>mSteps;
    private Step mStep;
    private int mIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        if(savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.STEP_INDEX))){
            mIndex = savedInstanceState.getInt(getString(R.string.STEP_INDEX));
        }
        Bundle bundle = new Bundle();
        mSteps = getIntent().getParcelableArrayListExtra(getString(R.string.STEPS));
        mStep = getIntent().getParcelableExtra(getString(R.string.STEP));
        bundle.putParcelable(getString(R.string.STEP),mStep);
        bundle.putInt(getString(R.string.STEPS),mSteps.size());
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
        enableDisableButton();
    }
}
