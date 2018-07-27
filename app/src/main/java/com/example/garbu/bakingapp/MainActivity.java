package com.example.garbu.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.garbu.bakingapp.adapters.MainAdapter;
import com.example.garbu.bakingapp.model.Recipe;
import com.example.garbu.bakingapp.utils.ObjectConverter;
import com.example.garbu.bakingapp.utils.RecipeInterface;
import com.example.garbu.bakingapp.widget.BakingAppWidgetProvider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MainAdapter.RecipeClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {
    private RecyclerView recipesRV;
    private MainAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Recipe> mRecipes = new ArrayList<>();
    SharedPreferences mRecipeSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int gridCols = 1;
        //check if tablet layout or landscape, set 2 grid columns
        if (findViewById(R.id.tablet_layout) != null || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridCols = 2;
        }

        recipesRV = findViewById(R.id.recipes_rv);
        mLayoutManager = new GridLayoutManager(this, gridCols);
        recipesRV.setLayoutManager(mLayoutManager);
        mAdapter = new MainAdapter(this);
        recipesRV.setAdapter(mAdapter);
        recipesRV.setHasFixedSize(true);

        //Retrofit code

        RecipeInterface recipeInterface = RecipeInterface.retrofit.create(RecipeInterface.class);
        Call<ArrayList<Recipe>> call = recipeInterface.getRecipes();
        //make an asynchronous API call to get recipe data from web resource
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if (response.isSuccessful()) {
                    mRecipes = response.body();
                    mAdapter.setRecipes(mRecipes);
                } else {
                    int statusCode = response.code();
                    Log.d("retrofit", "error" + statusCode);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "There was an error connecting to get the recipe data.", Toast.LENGTH_LONG).show();
            }
        });
        mRecipeSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mRecipeSharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecipeSharedPref.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onClickRecipe(Recipe recipe) {
        //save selected recipe to Shared Preferences
        String recipeJson = ObjectConverter.getStringFromRecipe(recipe);
        SharedPreferences.Editor editor = mRecipeSharedPref.edit();
        editor.putString(getString(R.string.recipe_key), recipeJson);
        editor.apply();

        //detail intent
        Intent intentToStartDetailActivity = new Intent(this, RecipeDetailActivity.class);
        intentToStartDetailActivity.putExtra(getString(R.string.recipe_key), recipe);
        startActivity(intentToStartDetailActivity);


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        //widget code
        Intent intent = new Intent(this, BakingAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

    }
}
