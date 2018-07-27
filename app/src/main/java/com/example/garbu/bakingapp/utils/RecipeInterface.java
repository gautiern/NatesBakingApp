package com.example.garbu.bakingapp.utils;

import com.example.garbu.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by garbu on 7/9/2018.
 */

public interface RecipeInterface {

        @GET("baking.json")
        Call<ArrayList<Recipe>> getRecipes();


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
