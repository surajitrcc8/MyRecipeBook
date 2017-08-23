package com.example.android.recipebook;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.recipebook.model.Step;
import com.example.android.recipebook.util.RecipeUtil;
import com.google.android.exoplayer2.ExoPlayerFactory;
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

public class FragmentStepDetails extends Fragment implements View.OnClickListener{
    private Bundle bundle;
    private Step mStep;
    private int mStepLength;
    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private TextView mStepInstructionTextView;
    private Button mPreviousButton;
    private Button mNextButton;

    private static final String PLAYER_STATE = "playerstate";
    private static final String TAG = FragmentStepDetails.class.getSimpleName();
    private OnButtonClick onButtonClick = null;



    public interface OnButtonClick{
        public void onButtonClicked(View view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        bundle = getArguments();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!RecipeUtil.isTablet(context)) {
            onButtonClick = (OnButtonClick) context;
            if (onButtonClick == null) {
                try {
                    throw new Exception("Please implement FragmentStepDetails.OnButtonClick");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details,container,false);
        mExoPlayerView = (SimpleExoPlayerView)view.findViewById(R.id.exo_player_view);
        mStepInstructionTextView = (TextView)view.findViewById(R.id.tv_step_instruction);
        mPreviousButton = (Button)view.findViewById(R.id.b_prev);
        mNextButton = (Button)view.findViewById(R.id.b_next);
        //mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),R.drawable.no_image));

        if(bundle != null && bundle.getParcelable(getString(R.string.STEP)) != null) {
            mStep = bundle.getParcelable(getString(R.string.STEP));
            mStepLength = bundle.getInt(getString(R.string.STEPS));
            if(mStepInstructionTextView != null){
                mStepInstructionTextView.setText(mStep.getDescription());
            }

            if(mPreviousButton != null && mNextButton != null) {
                mPreviousButton.setOnClickListener(this);
                mNextButton.setOnClickListener(this);
                if(savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.PREV_ENABLED))
                        && savedInstanceState.containsKey(getString(R.string.NEXT_ENABLED))) {
                    mPreviousButton.setEnabled(savedInstanceState.getBoolean(getString(R.string.PREV_ENABLED)));
                    mNextButton.setEnabled(savedInstanceState.getBoolean(getString(R.string.NEXT_ENABLED)));
                }else{
                    if(mStep.getId() == 0){
                        mPreviousButton.setEnabled(false);
                        mNextButton.setEnabled(true);
                    }else if(mStep.getId() <  (mStepLength -1)){
                        mPreviousButton.setEnabled(true);
                        mNextButton.setEnabled(true);
                    }else{
                        mPreviousButton.setEnabled(true);
                        mNextButton.setEnabled(false);
                    }
                }
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mPreviousButton != null && mNextButton != null) {
            outState.putBoolean(getString(R.string.PREV_ENABLED), mPreviousButton.isEnabled());
            outState.putBoolean(getString(R.string.NEXT_ENABLED), mNextButton.isEnabled());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.mExoPlayer == null){
            initiateExoPlayer(Uri.parse(mStep.getVideoURL()));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseExoPlayer();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseExoPlayer();
    }

    public void initiateExoPlayer(Uri videoUri) {
        if(videoUri != null){
            Activity activity = this.getActivity();
            TrackSelector trackSelector =
                    new DefaultTrackSelector();
            this.mExoPlayer =  ExoPlayerFactory.newSimpleInstance(activity.getApplicationContext(),trackSelector);
            this.mExoPlayerView.setPlayer(mExoPlayer);

            prepareAndPlayExoPlayer(videoUri);

        }

    }
    public void prepareAndPlayExoPlayer(Uri videoUri){
        Activity activity = this.getActivity();
        this.mExoPlayer.prepare(getMediaSource(activity,videoUri));
        this.mExoPlayer.setPlayWhenReady(true);
    }
    public void showStepInstruction(String instruction, Bundle bundle){
        this.bundle = bundle;
        if(mStepInstructionTextView != null){
            mStepInstructionTextView.setText(instruction);
        }
    }
    public MediaSource getMediaSource(Activity activity, Uri videoUri){
        String userAgent = Util.getUserAgent(activity.getApplicationContext(),TAG);

        MediaSource mediaSource = new ExtractorMediaSource(videoUri,new DefaultDataSourceFactory(activity.getApplicationContext(),userAgent)
                ,new DefaultExtractorsFactory(),null,null);
        return mediaSource;
    }
    private void releaseExoPlayer(){
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void setEnableButton(int buttonName, boolean enabled){
        switch(buttonName){
            case R.string.previous:
                if(mPreviousButton != null){
                    mPreviousButton.setEnabled(enabled);
                }
                break;
            case R.string.next:
                if(mNextButton != null){
                    mNextButton.setEnabled(enabled);
                }
                break;
        }
    }
    @Override
    public void onClick(View view) {
        onButtonClick.onButtonClicked(view);
    }
}
