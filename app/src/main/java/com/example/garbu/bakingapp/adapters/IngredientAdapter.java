package com.example.garbu.bakingapp.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.garbu.bakingapp.R;
import com.example.garbu.bakingapp.model.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by garbu on 7/16/2018.
 * This adapter displays the ingredients list for the selected recipe
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {


    private ArrayList<Ingredient> mIngredients = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.quantity_tv)
        TextView quantity;
        @BindView(R.id.measure_tv)
        TextView measure;
        @BindView(R.id.ingredient_tv)
        TextView ingredient;
        @BindView(R.id.ingredientLayout)
        LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public IngredientAdapter(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;

    }

    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ingredient_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.ViewHolder holder, int position) {

        Ingredient ingredient = mIngredients.get(position);

        holder.quantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.measure.setText(ingredient.getMeasure());
        holder.ingredient.setText(ingredient.getIngredient());

    }

    @Override
    public int getItemCount() {
        if (mIngredients.isEmpty()) {
            return 0;
        } else {
            return mIngredients.size();
        }
    }
}

