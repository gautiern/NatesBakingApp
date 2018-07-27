package com.example.garbu.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.garbu.bakingapp.R;
import com.example.garbu.bakingapp.model.Ingredient;
import com.example.garbu.bakingapp.model.Recipe;
import com.example.garbu.bakingapp.utils.ObjectConverter;

import java.util.ArrayList;

/**
 * Created by garbu on 7/24/2018.
 */

public class IngredientListProvider implements RemoteViewsService.RemoteViewsFactory{
    private ArrayList<Ingredient> mIngredients = new ArrayList();
    private Context mContext = null;

    public IngredientListProvider(Context context){
        this.mContext = context;
        mIngredients =getIngredientList();
    }
    private ArrayList<Ingredient> getIngredientList(){
        ArrayList<Ingredient> ingredients = null;

        SharedPreferences recipePreference = PreferenceManager.getDefaultSharedPreferences(mContext);
        String recipeJson = recipePreference.getString(mContext.getString(R.string.recipe_key),"");

        if(!recipeJson.isEmpty()) {
            Recipe recipe = ObjectConverter.getRecipeFromJson(recipeJson);
            ingredients = recipe.getIngredients();
            Log.d("ingredient_widget","ingredient" +ingredients.get(2).getIngredient());
        }
        return ingredients;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mIngredients = getIngredientList();

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredients.isEmpty()) {
            return 0;
        } else {
            return mIngredients.size();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),R.layout.ingredient_layout);
        Ingredient ingredient = mIngredients.get(position);
        //populate views
        remoteViews.setTextViewText(R.id.quantity_tv,String.valueOf(ingredient.getQuantity()));
        remoteViews.setTextViewText(R.id.measure_tv,ingredient.getMeasure());
        remoteViews.setTextViewText(R.id.ingredient_tv,ingredient.getIngredient());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
