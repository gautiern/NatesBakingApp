package com.example.garbu.bakingapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.garbu.bakingapp.R;
import com.example.garbu.bakingapp.adapters.StepsAdapter;
import com.example.garbu.bakingapp.model.Step;

import java.util.ArrayList;

/**
 * This fragment is for the list of steps.
 */
public class StepsFragment extends Fragment implements StepsAdapter.StepClickHandler {

    private static final String TAG = StepsFragment.class.getSimpleName();
    private ArrayList<Step> mSteps;
    private RecyclerView mStepsRV;
    private StepsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;

    OnStepClickListener mCallback;

    @Override
    public void onStepClick(ArrayList<Step> steps, int position) {
        mCallback.onStepSelected(steps, position);
    }

    public interface OnStepClickListener {
        void onStepSelected(ArrayList<Step> steps, int position);
    }

    //verify interface is implemented
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    public StepsFragment() {
        // Required empty public constructor
    }

    public static StepsFragment newInstance(ArrayList<Step> steps, Context context) {
        StepsFragment stepsFragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(context.getString(R.string.steps_list_key), steps);
        stepsFragment.setArguments(args);
        return stepsFragment;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity().getApplicationContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        mStepsRV = view.findViewById(R.id.steps_rv);
        mSteps = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mStepsRV.setLayoutManager(mLayoutManager);
        mStepsRV.setNestedScrollingEnabled(false);
        mSteps = getArguments().getParcelableArrayList(mContext.getString(R.string.steps_list_key));
        mAdapter = new StepsAdapter(mSteps, this);
        mStepsRV.setAdapter(mAdapter);
        return view;
    }

}
