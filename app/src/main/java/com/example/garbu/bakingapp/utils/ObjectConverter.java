package com.example.garbu.bakingapp.utils;

import com.example.garbu.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Conversion methods for Recipe object and json
 */

public class ObjectConverter {

    public static Recipe getRecipeFromJson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Recipe>() {
        }.getType();

        return gson.fromJson(json, type);
    }

    public static String getStringFromRecipe(Recipe recipe) {
        Gson gson = new Gson();
        Type type = new TypeToken<Recipe>() {
        }.getType();

        return gson.toJson(recipe, type);
    }
}
