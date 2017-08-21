package com.example.android.recipebook;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by surajitbiswas on 8/18/17.
 */

public class SimpleIdleResource implements IdlingResource, Parcelable {


    private volatile ResourceCallback mCallBack;
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    public SimpleIdleResource() {
    }

    protected SimpleIdleResource(Parcel in) {
    }

    public static final Creator<SimpleIdleResource> CREATOR = new Creator<SimpleIdleResource>() {
        @Override
        public SimpleIdleResource createFromParcel(Parcel in) {
            return new SimpleIdleResource(in);
        }

        @Override
        public SimpleIdleResource[] newArray(int size) {
            return new SimpleIdleResource[size];
        }
    };

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mCallBack = resourceCallback;

    }
    public void setIdleState(boolean newState){
        mIsIdleNow.set(newState);
        if(newState &&  mCallBack != null){
            mCallBack.onTransitionToIdle();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
