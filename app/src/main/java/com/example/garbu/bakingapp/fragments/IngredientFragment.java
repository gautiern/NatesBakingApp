package com.example.garbu.bakingapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.garbu.bakingapp.R;
import com.example.garbu.bakingapp.adapters.IngredientAdapter;
import com.example.garbu.bakingapp.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientFragment extends Fragment {

    private static final String TAG = IngredientFragment.class.getSimpleName();
    private ArrayList<Ingredient> mIngredients;
    private RecyclerView mIngredientsRV;
    private IngredientAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;



    public IngredientFragment() {
        // Required empty public constructor
    }
    public static IngredientFragment newInstance(ArrayList<Ingredient> ingredients, Context context){
        IngredientFragment ingredientFragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(context.getString(R.string.ingredient_list_key), ingredients);
        ingredientFragment.setArguments(args);
        return ingredientFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        mIngredientsRV = view.findViewById(R.id.ingredients_rv);
        mIngredients = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mIngredientsRV.setLayoutManager(mLayoutManager);
        mIngredientsRV.setNestedScrollingEnabled(false);
        mIngredients = getArguments().getParcelableArrayList(mContext.getString(R.string.ingredient_list_key));
        mAdapter = new IngredientAdapter(mIngredients);
        mIngredientsRV.setAdapter(mAdapter);

        return view;

    }

}
