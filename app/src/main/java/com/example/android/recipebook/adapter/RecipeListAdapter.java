package com.example.android.recipebook.adapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.recipebook.BR;
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
            holder.bindingView.setVariable(BR.recipe,mRecipes.get(position));
            Step step = mRecipes.get(position).getSteps().get(mRecipes.get(position).getSteps().size() - 1);
            holder.bindingView.setVariable(BR.step,step);
            holder.bindingView.executePendingBindings();
        }

    }
    @BindingAdapter("app:srcUrl")
    public static void loadGridImage(ImageView image,String imageUrl){
        if(imageUrl.length() > 2) {
            GlideApp.with(image.getContext()).load(new VideoThumbnailUrl(imageUrl)).placeholder(R.drawable.recipe_place_holder).into(image);
        }
    }
    @Override
    public int getItemCount() {
        return (mRecipes != null && mRecipes.size() > 0) ? mRecipes.size() : 0;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ViewDataBinding bindingView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            bindingView = DataBindingUtil.bind(itemView);
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

}
