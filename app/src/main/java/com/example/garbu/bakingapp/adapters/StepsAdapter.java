package com.example.garbu.bakingapp.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.garbu.bakingapp.R;
import com.example.garbu.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by garbu on 7/17/2018.
 * This adapter displays the list of steps for the selected recipe.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private ArrayList<Step> mSteps = new ArrayList<>();

    private final StepClickHandler mClickHandler;

    public interface StepClickHandler {
        void onStepClick(ArrayList<Step> steps, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.short_descr_tv)
        public TextView shortDesc;
        @BindView(R.id.step_layout)
        public LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int stepPosition = getAdapterPosition();
            mClickHandler.onStepClick(mSteps, stepPosition);
        }
    }

    public StepsAdapter(ArrayList<Step> steps, StepClickHandler clickHandler) {
        mSteps = steps;
        mClickHandler = clickHandler;

    }

    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.step_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepsAdapter.ViewHolder holder, final int position) {
        Step step = mSteps.get(position);
        holder.shortDesc.setText(step.getShortDescription());

    }

    @Override
    public int getItemCount() {
        if (mSteps.isEmpty()) {
            return 0;
        } else {
            return mSteps.size();
        }
    }
}
