package com.example.android.recipebook.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.example.android.recipebook.model.VideoThumbnailUrl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by surajitbiswas on 8/8/17.
 */

public class VideoThumbnailFetcher implements DataFetcher<InputStream> {
    private VideoThumbnailUrl videoThumbnailUrl;
    private static final String TAG = VideoThumbnailFetcher.class.getSimpleName();
    public VideoThumbnailFetcher(VideoThumbnailUrl videoThumbnailUrl) {
        this.videoThumbnailUrl = videoThumbnailUrl;
    }

    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
        MediaMetadataRetriever mediaMetadataRetriever = null;
        Bitmap bitmap = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            Log.d("Load data " , videoThumbnailUrl.url);

            mediaMetadataRetriever.setDataSource(videoThumbnailUrl.url,new HashMap<String, String>());

            bitmap = mediaMetadataRetriever.getFrameAtTime(1000);

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(mediaMetadataRetriever != null){
                mediaMetadataRetriever.release();
            }
        }

        if (bitmap == null) {
            callback.onLoadFailed(new Exception("Bitmap is null"));
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            ByteArrayInputStream bs = new ByteArrayInputStream(bos.toByteArray());

            callback.onDataReady(bs);
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
