package com.example.android.recipebook.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.recipebook.R;
import com.example.android.recipebook.model.Recipe;
import com.example.android.recipebook.model.Step;
import com.example.android.recipebook.model.VideoThumbnailUrl;
import com.example.android.recipebook.util.GlideApp;

import java.util.ArrayList;


/**
 * Created by surajitbiswas on 8/7/17.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private ArrayList<Recipe>mRecipes = null;
    private Context mContex;
    private OnRecipeItemClicked mOnRecipeItemClicked;

    public interface OnRecipeItemClicked{
        public void onClickRecipeItem(Recipe recipe);
    }

    public RecipeListAdapter(Context mContex, OnRecipeItemClicked mOnRecipeItemClicked) {
        this.mContex = mContex;
        this.mOnRecipeItemClicked = mOnRecipeItemClicked;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        if(mRecipes != null){
            holder.mRecipeNameTextView.setText(mRecipes.get(position).getName());
            holder.mDiningCount.setText(String.valueOf(mRecipes.get(position).getServings()));
            if(mRecipes.get(position).getImage() != null && mRecipes.get(position).getImage().length() > 1){
                GlideApp.with(mContex).load(Uri.parse(mRecipes.get(position).getImage())).placeholder(R.drawable.recipe_place_holder).into(holder.mRecipeImageView);
            }else{
                Step step = mRecipes.get(position).getSteps().get(mRecipes.get(position).getSteps().size() - 1);
                String imageUrl = step.getVideoURL();
                try {
                    GlideApp.with(mContex).load(new VideoThumbnailUrl(imageUrl)).placeholder(R.drawable.recipe_place_holder).into(holder.mRecipeImageView);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return (mRecipes != null && mRecipes.size() > 0) ? mRecipes.size() : 0;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mRecipeImageView;
        private TextView mRecipeNameTextView;
        private TextView mDiningCount;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeImageView = (ImageView) itemView.findViewById(R.id.iv_recipe);
            mRecipeNameTextView = (TextView)itemView.findViewById(R.id.tv_recipe_name);
            mDiningCount = (TextView)itemView.findViewById(R.id.tv_dining_count);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            mOnRecipeItemClicked.onClickRecipeItem(mRecipes.get(getAdapterPosition()));
        }
    }
    public void setData(ArrayList<Recipe> recipes){
        mRecipes = recipes;
        notifyDataSetChanged();
    }
    public static Bitmap retriveVideoFrameFromVideo(String videoPath)

    {


         return ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
    }

}
