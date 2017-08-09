package com.example.android.recipebook.util;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.example.android.recipebook.model.VideoThumbnailUrl;

import java.io.InputStream;

/**
 * Created by surajitbiswas on 8/8/17.
 */

public class VideoThumbnailFactory implements ModelLoaderFactory<VideoThumbnailUrl, InputStream> {


    @Override
    public ModelLoader<VideoThumbnailUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
        return new VideoThumbnailLoader();
    }

    @Override
    public void teardown() {

    }


}
