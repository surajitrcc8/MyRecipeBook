package com.example.android.recipebook;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StepDetailsActivity extends AppCompatActivity {
    private FragmentStepDetails fragmentStepDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.STEP),getIntent().getParcelableExtra(getString(R.string.STEP)));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentStepDetails = (FragmentStepDetails)fragmentManager.findFragmentByTag(getString(R.string.TAG_STEP_DETAILS_FRAGMENT));
        if(fragmentStepDetails == null) {
            fragmentStepDetails = new FragmentStepDetails();
            fragmentStepDetails.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragmentStepDetails, getString(R.string.TAG_STEP_DETAILS_FRAGMENT))
                    .commit();
        }

    }
}