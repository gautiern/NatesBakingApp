package com.example.garbu.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.garbu.bakingapp.fragments.StepDetailFragment;
import com.example.garbu.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

//This activity launches when you select a step and displays a video or image (if any ) and the full text of the step.
//This activity only launches on smaller devices.  For tablets, all fragments are called in the RecipeDetailActivity.class.

public class StepDetailActivity extends AppCompatActivity {
    @BindView(R.id.prev_step)
    Button prevStepButton;
    @BindView(R.id.next_step)
    Button nextStepButton;
    private ArrayList<Step> mSteps;
    private int mStepPosition;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        mSteps = new ArrayList<>();
        mContext = getApplicationContext();

        //get details passed from parent activity
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(getString(R.string.steps_list_key));
            mStepPosition = savedInstanceState.getInt(getString(R.string.step_position));
        } else {

            if (bundle != null) {
                mSteps = bundle.getParcelableArrayList(getString(R.string.steps_list_key));
                mStepPosition = bundle.getInt(getString(R.string.step_position));
            }
        }

        this.setTitle(mSteps.get(mStepPosition).getShortDescription());
        //set navigation buttons active/inactive
        setupButtons();
        if (savedInstanceState == null) {
            //start new details fragment
            FragmentTransaction stepsFT = getSupportFragmentManager().beginTransaction();
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mSteps.get(mStepPosition), mContext);
            stepsFT.add(R.id.step_details, stepDetailFragment);
            stepsFT.commit();

        }

    }

    public void prevStep(View view) {
        mStepPosition--;
        setupButtons();
        displayRecipeDetails();

    }

    public void nextStep(View view) {
        mStepPosition++;
        setupButtons();
        displayRecipeDetails();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the list of steps and the current position
        outState.putParcelableArrayList(getString(R.string.steps_list_key), mSteps);
        outState.putInt(getString(R.string.step_position), mStepPosition);

    }

    private void displayRecipeDetails() {
        //display details of recipe selected
        this.setTitle(mSteps.get(mStepPosition).getShortDescription());
        FragmentTransaction stepsFT = getSupportFragmentManager().beginTransaction();
        StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mSteps.get(mStepPosition), mContext);
        stepsFT.replace(R.id.step_details, stepDetailFragment);
        stepsFT.commit();

    }

    private void setupButtons() {

        if (mStepPosition == 0) {
            prevStepButton.setEnabled(false);
        } else if (mStepPosition == mSteps.size() - 1) {
            nextStepButton.setEnabled(false);
        } else {
            nextStepButton.setEnabled(true);
            prevStepButton.setEnabled(true);
        }


    }


}