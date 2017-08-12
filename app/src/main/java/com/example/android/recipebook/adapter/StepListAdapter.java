package com.example.android.recipebook.adapter;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;
import com.bumptech.glide.Glide;
import com.example.android.recipebook.R;
import com.example.android.recipebook.model.Step;
import com.example.android.recipebook.model.VideoThumbnailUrl;
import com.example.android.recipebook.util.GlideApp;

import java.util.ArrayList;

/**
 * Created by surajitbiswas on 8/10/17.
 */

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepViewHolder> {

    private ArrayList<Step>mSteps = null;
    private OnClickStepItem onClickStepItem;

    public interface OnClickStepItem{
        public void onStepItemClicked(Step step);
    }

    public StepListAdapter(OnClickStepItem onClickStepItem) {
        this.onClickStepItem = onClickStepItem;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.step_list_item,parent,false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        if(mSteps != null){
            holder.bindingView.setVariable(BR.step,mSteps.get(position));
            holder.bindingView.executePendingBindings();
        }
    }
    @BindingAdapter("app:srcUrl")
    public static void loadGridImage(ImageView image, String imageUrl){
        Glide.with(image.getContext()).load(Uri.parse(imageUrl)).into(image);
        //GlideApp.with(image.getContext()).load(new VideoThumbnailUrl(imageUrl)).placeholder(R.drawable.recipe_place_holder).error(R.drawable.recipe_place_holder).into(image);
    }
    @Override
    public int getItemCount() {
        return (mSteps == null) ? 0 : mSteps.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ViewDataBinding bindingView;
        private ImageView mRecipeImageView;
        private TextView mShortDescTextView;
        public StepViewHolder(View itemView) {
            super(itemView);
            bindingView = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int index = getAdapterPosition();
            onClickStepItem.onStepItemClicked(mSteps.get(index));
        }
    }
    public void setSteps(ArrayList<Step> steps){
        if(steps != null){
            mSteps = steps;
            notifyDataSetChanged();
        }
    }
}
