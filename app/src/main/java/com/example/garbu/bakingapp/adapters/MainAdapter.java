package com.example.garbu.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.garbu.bakingapp.R;
import com.example.garbu.bakingapp.RecipeDetailActivity;
import com.example.garbu.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by garbu on 7/10/2018.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {


    private List<Recipe> mRecipes = new ArrayList<>();
    private Recipe mRecipe;
    private List<Integer> mRecipeImages = new ArrayList<>();
    private final RecipeClickHandler mClickHandler;

    public interface RecipeClickHandler{
        void onClickRecipe(Recipe recipe);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.recipe_image)
        ImageView recipeImage;
        @BindView(R.id.recipe_name)
        TextView recipeName;
        @BindView(R.id.recipe_servings)
        TextView recipeServings;

        public ViewHolder(View itemView) {
            super(itemView);
            //bind the view
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int recipePosition = getAdapterPosition();
            mRecipe = mRecipes.get(recipePosition);
            mClickHandler.onClickRecipe(mRecipe);
        }
    }
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, final int position) {
        final Context context = holder.itemView.getContext();
        final String recipeKey = context.getString(R.string.recipe_key);
        mRecipe = mRecipes.get(position);
        holder.recipeName.setText(mRecipe.getName());
        String servings = context.getString(R.string.servings_label) + mRecipe.getServings();
        holder.recipeServings.setText(servings);

        /*
        populate array list of temporary images for current recipes
        Sample Images released under Creative Commons CC0

        cheesecake
        https://pixabay.com/en/cake-cheesecake-eat-plate-fork-862/
        brownies
        https://pixabay.com/en/cake-pastry-sweet-sugar-unhealthy-2201782/
        nutella_pie
        https://pixabay.com/en/cake-pie-chocolate-1384702/

        yellow cake
        https://www.flickr.com/photos/kimberlykv/4491172696/in/photolist-7QSrb1-8gbrkp-
        4BxGSt-74fyVZ-ohTCVn-aPnqFT-8NJKh3-233EBM8-aqJ6sK-a95h71-21GtLjz-aBJLaH-GHJxko-
        e3eRGv-Fg1GSX-E4Z7LX-3ixWbs-s3ms6W-ZranTs-TBuge8-9EfDqT-4o5WV9-9ixTT4-jkR5A2-dAT1hL-
        ZoiaCs-8vXPNv-bTTiBZ-bobQhF-akfvwY-G7ZyGw-fqm3w1-7j5f7f-aVDG8g-21cL7xJ-82GWhQ-foAd1D-75TXdC-
        abarhy-6Qhtft-39vbJT-9FDjUA-kUvjNe-Z3mWbN-9CYkb9-4Y4hug-bpJGJX-5ZgCYg-8EVJbz-27TSr82
         */
            mRecipeImages.add(R.drawable.nutella_pie);
            mRecipeImages.add(R.drawable.brownies);
            mRecipeImages.add(R.drawable.yellowcake);
            mRecipeImages.add(R.drawable.cheesecake);

            loadRecipeImage(holder,position);

    }

    @Override
    public int getItemCount() {
        if (mRecipes.isEmpty()) {
            return 0;
        } else {
            return mRecipes.size();
        }
    }

    public void setRecipes(List<Recipe> recipeData) {
        mRecipes = recipeData;
        notifyDataSetChanged();
    }

    public MainAdapter(RecipeClickHandler clickHandler) {
        //mRecipes = recipeData;
        mClickHandler = clickHandler;

    }
    private void loadRecipeImage(ViewHolder holder, int position){
        if (!mRecipe.getImage().equals("")) {
            //if there is an image for the mRecipe then load it
            Picasso.get()
                    .load(mRecipe.getImage())
                    .placeholder(R.drawable.ic_landscape_grey_24dp)
                    .error(R.drawable.content_error)
                    .resize(100,110)
                    .centerCrop()
                    .into(holder.recipeImage);
            return;
        } else {
            //either load a temporary image or an error image
            while (position <= mRecipeImages.size()){
                Picasso.get()
                        .load(mRecipeImages.get(position))
                        .placeholder(R.drawable.ic_landscape_grey_24dp)
                        .error(R.drawable.content_error)
                        .resize(100,110)
                        .centerCrop()
                        .into(holder.recipeImage);
                return;
            }
            holder.recipeImage.setImageResource(R.drawable.no_content);

        }
    }

}
