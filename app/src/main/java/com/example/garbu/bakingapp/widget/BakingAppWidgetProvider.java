package com.example.garbu.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.garbu.bakingapp.MainActivity;
import com.example.garbu.bakingapp.R;
import com.example.garbu.bakingapp.RecipeDetailActivity;
import com.example.garbu.bakingapp.model.Recipe;
import com.example.garbu.bakingapp.utils.ObjectConverter;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent launchApp;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);


        //get data from Shared Preferences
        SharedPreferences recipePreference = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeJson = recipePreference.getString(context.getString(R.string.recipe_key), "");
        if (recipeJson != "") {
            Recipe recipe = ObjectConverter.getRecipeFromJson(recipeJson);
            String recipeName = recipe.getName();
            views.setTextViewText(R.id.widget_recipe_name, recipeName);

            //intent for listview of ingredients
            Intent ingredientIntent = new Intent(context, BakingAppWidgetService.class);
            views.setRemoteAdapter(R.id.widget_ingredient_list, ingredientIntent);

            //Create an intent to launch Recipe Detail activity
            launchApp = new Intent(context, RecipeDetailActivity.class);
            launchApp.putExtra(context.getString(R.string.recipe_key), recipe);
        } else {
            views.setTextViewText(R.id.widget_recipe_name, context.getString(R.string.widget_default_text));
            launchApp = new Intent(context, MainActivity.class);
        }
        //Referenced Medium article forTaskStackBuilder info
        // URL: https://medium.com/google-developers/tasks-and-the-back-stack-dbb7c3b0f6d4

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(launchApp);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //click handler to launch pending intent
        views.setOnClickPendingIntent(R.id.recipe_widget, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

