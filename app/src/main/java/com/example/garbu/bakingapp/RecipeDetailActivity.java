package com.example.garbu.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.garbu.bakingapp.fragments.IngredientFragment;
import com.example.garbu.bakingapp.fragments.StepDetailFragment;
import com.example.garbu.bakingapp.fragments.StepsFragment;
import com.example.garbu.bakingapp.model.Recipe;
import com.example.garbu.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity implements StepsFragment.OnStepClickListener{
    private Recipe mRecipe;
    private boolean mTabletView;
    //this activity launches when a recipe is clicked to show the ingredients and steps fragments on a phone and all fragments on a tablet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        //check if tablet layout
        if (findViewById(R.id.tablet_view)!=null){
            mTabletView = true;
        } else {
            mTabletView = false;
        }
        //get details passed from parent activity
        Bundle bundle = getIntent().getExtras();
        //Intent intent = getIntent();
        //Bundle bundle=intent.getExtras();
        //Log.d("Intent passed","**************Intent passed is:" +getIntent().toString());


        if(savedInstanceState !=null){
            mRecipe = savedInstanceState.getParcelable(getString(R.string.recipe_key));
        } else {

            if (bundle != null) {
                mRecipe = bundle.getParcelable(getString(R.string.recipe_key));

            }

            //ingredients fragment
            FragmentTransaction ingredientsFT = getSupportFragmentManager().beginTransaction();
            IngredientFragment ingredientFragment = IngredientFragment.newInstance(mRecipe.getIngredients(),this);
            ingredientsFT.add(R.id.ingredients_fragment,ingredientFragment);
            ingredientsFT.commit();

            //steps fragment
            FragmentTransaction stepsFT = getSupportFragmentManager().beginTransaction();
            StepsFragment stepsFragment = StepsFragment.newInstance(mRecipe.getSteps(),this);
            stepsFT.add(R.id.steps_fragment,stepsFragment);
            stepsFT.commit();
        }
        this.setTitle(mRecipe.getName());

        if (mTabletView & savedInstanceState == null){
            //if tablet view, then load the step details fragment
            FragmentTransaction detailsFT = getSupportFragmentManager().beginTransaction();
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mRecipe.getSteps().get(0),this);
            detailsFT.add(R.id.step_detail_fragment,stepDetailFragment);
            detailsFT.commit();
        }

    }


    @Override
    public void onStepSelected(ArrayList<Step> steps, int position) {
        if(mTabletView) {
            //if tablet view, replace the fragment contents with the step selected
            Step step = steps.get(position);
            FragmentTransaction detailsFT = getSupportFragmentManager().beginTransaction();
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(step, this);
            detailsFT.replace(R.id.step_detail_fragment, stepDetailFragment);
            detailsFT.commit();

        } else {
            //for phone view launch the step details activity passing the selected step
            Intent intentToStartDetailActivity = new Intent(this, StepDetailActivity.class);
            intentToStartDetailActivity.putExtra(getString(R.string.steps_list_key), mRecipe.getSteps());
            intentToStartDetailActivity.putExtra(getString(R.string.step_position), position);
            this.startActivity(intentToStartDetailActivity);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(getString(R.string.recipe_key),mRecipe);
        super.onSaveInstanceState(outState);
    }

}
