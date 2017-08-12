package com.example.android.recipebook.util;

import android.content.Context;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.example.android.recipebook.model.VideoThumbnailUrl;

import java.io.InputStream;

/**
 * Created by surajitbiswas on 8/8/17.
 */

public class VideoThumbnailFactory implements ModelLoaderFactory<VideoThumbnailUrl, InputStream> {

    private Context mContext;
    public VideoThumbnailFactory(Context context) {
        this.mContext = context;
    }

    @Override
    public ModelLoader<VideoThumbnailUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
        return new VideoThumbnailLoader(this.mContext);
    }

    @Override
    public void teardown() {
    }


}
