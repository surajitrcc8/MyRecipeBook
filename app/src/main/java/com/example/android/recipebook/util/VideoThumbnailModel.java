package com.example.android.recipebook.util;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.android.recipebook.model.VideoThumbnailUrl;

import java.io.InputStream;


/**
 * Created by surajitbiswas on 8/8/17.
 */
@GlideModule
public class VideoThumbnailModel extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

        registry.replace(VideoThumbnailUrl.class,InputStream.class,new VideoThumbnailFactory(context));
    }
}
