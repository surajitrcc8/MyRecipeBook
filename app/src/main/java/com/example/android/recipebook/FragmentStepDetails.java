package com.example.android.recipebook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.recipebook.model.Recipe;
import com.example.android.recipebook.model.Step;
import com.example.android.recipebook.util.RecipeUtil;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by surajitbiswas on 8/11/17.
 */

public class FragmentStepDetails extends Fragment {
    private Bundle bundle;
    private Step mStep;
    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private TextView mStepInstructionTextView;
    private static final String PLAYER_STATE = "playerstate";
    private static final String TAG = FragmentStepDetails.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        bundle = getArguments();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details,container,false);
        mExoPlayerView = (SimpleExoPlayerView)view.findViewById(R.id.exo_player_view);
        mStepInstructionTextView = (TextView)view.findViewById(R.id.tv_step_instruction);


        if(!RecipeUtil.isTablet(getContext())) {
            if (RecipeUtil.isPotraite(this.getContext())) {
                mExoPlayerView.setMinimumHeight(RecipeUtil.convertToPixel(getContext(), getResources().getDimension(R.dimen.dp250)));
            } else {
                DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
                float height = displayMetrics.heightPixels / displayMetrics.density;
                mExoPlayerView.setMinimumHeight(RecipeUtil.convertToPixel(getContext(), height));
            }
        }

        if(bundle != null && bundle.getParcelable(getString(R.string.STEP)) != null) {
            mStep = bundle.getParcelable(getString(R.string.STEP));

            if(RecipeUtil.isTablet(getContext())){
                mStepInstructionTextView.setText(mStep.getDescription());
            }else{
                if(RecipeUtil.isPotraite(this.getContext())){
                    mStepInstructionTextView.setText(mStep.getDescription());
                }
            }


            //Check if this the first time we are creating this player view.
            if(this.mExoPlayer == null){
                initiateExoPlayer(Uri.parse(mStep.getVideoURL()));
            }
            else{
                //Player already exists so add to view.
                mExoPlayerView.setPlayer(mExoPlayer);
                //Get the previous player state
                if(savedInstanceState != null && savedInstanceState.containsKey(PLAYER_STATE))
                mExoPlayer.setPlayWhenReady(savedInstanceState.getBoolean(PLAYER_STATE));
            }
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Store the current player state
        outState.putBoolean(PLAYER_STATE,mExoPlayer.getPlayWhenReady());
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mExoPlayer.setPlayWhenReady(false);
    }


    public void initiateExoPlayer(Uri videoUri) {
        if(videoUri != null){
            Activity activity = this.getActivity();
            TrackSelector trackSelector =
                    new DefaultTrackSelector();
            this.mExoPlayer =  ExoPlayerFactory.newSimpleInstance(activity.getApplicationContext(),trackSelector);
            this.mExoPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(activity.getApplicationContext(),TAG);

            MediaSource mediaSource = new ExtractorMediaSource(videoUri,new DefaultDataSourceFactory(activity.getApplicationContext(),userAgent)
                    ,new DefaultExtractorsFactory(),null,null);
            this.mExoPlayer.prepare(mediaSource);
            this.mExoPlayer.setPlayWhenReady(true);

        }

    }
    private void releaseExoPlayer(){
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseExoPlayer();
    }
}
