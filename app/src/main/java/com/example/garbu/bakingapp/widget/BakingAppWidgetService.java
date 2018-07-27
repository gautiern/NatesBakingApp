package com.example.garbu.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by garbu on 7/24/2018.
 */

public class BakingAppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return (new IngredientListProvider(this.getApplicationContext()));
    }
}
