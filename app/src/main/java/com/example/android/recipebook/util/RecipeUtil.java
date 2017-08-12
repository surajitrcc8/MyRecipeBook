package com.example.android.recipebook.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by surajitbiswas on 8/7/17.
 */

public class RecipeUtil {

    private static final String TAG = RecipeUtil.class.getSimpleName();
    private static int TILE_OFFSET = 4;
    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("recipe_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public static int getTileSpan(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float deviceWidth = displayMetrics.widthPixels / displayMetrics.density;
        deviceWidth = deviceWidth /100;

        Log.d(TAG,"Device width is " + deviceWidth);
        switch(context.getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                if(deviceWidth >= 6.00){
                    TILE_OFFSET = 2;
                }else{
                    TILE_OFFSET = 1;
                }
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                if(deviceWidth >= 8.00){
                    TILE_OFFSET = 3;
                }else{
                    TILE_OFFSET = 2;
                }
                break;
        }
        return TILE_OFFSET;
    }
}
