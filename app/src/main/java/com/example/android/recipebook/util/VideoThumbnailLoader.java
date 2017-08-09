package com.example.android.recipebook.util;

import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import com.example.android.recipebook.model.VideoThumbnailUrl;

import java.io.InputStream;

/**
 * Created by surajitbiswas on 8/8/17.
 */

public class VideoThumbnailLoader implements ModelLoader<VideoThumbnailUrl, InputStream>{


    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(VideoThumbnailUrl videoThumbnailUrl, int width, int height, Options options) {
        return new ModelLoader.LoadData(new ObjectKey(videoThumbnailUrl), new VideoThumbnailFetcher(videoThumbnailUrl));
    }

    @Override
    public boolean handles(VideoThumbnailUrl videoThumbnailUrl) {
        return true;
    }



}
