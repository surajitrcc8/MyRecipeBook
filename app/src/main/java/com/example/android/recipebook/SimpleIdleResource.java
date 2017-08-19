package com.example.android.recipebook;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by surajitbiswas on 8/18/17.
 */

public class SimpleIdleResource implements IdlingResource {


    private volatile ResourceCallback mCallBack;
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);
    @Override
    public String getName() {
        return this.getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mCallBack = resourceCallback;

    }
    public  void setIdleState(boolean newState){
        mIsIdleNow.set(newState);
        if(newState &&  mCallBack != null){
            mCallBack.onTransitionToIdle();
        }
    }
}
