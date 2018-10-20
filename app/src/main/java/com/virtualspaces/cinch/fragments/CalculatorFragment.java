package com.virtualspaces.cinch.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;

import com.virtualspaces.cinch.R;
import com.virtualspaces.cinch.adapters.RecyclerViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalculatorFragment extends Fragment {


    public CalculatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.calc_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        // 1 indicates that adapter is for calculator summary fragment
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(1, getActivity());
        recyclerView.setAdapter(adapter);

        AlphaAnimation alpha = new AlphaAnimation(0f,1f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(400);
        alpha.setFillAfter(true);
        alpha.start();
        recyclerView.setLayoutAnimation(new LayoutAnimationController(alpha));
        return view;
    }

}
