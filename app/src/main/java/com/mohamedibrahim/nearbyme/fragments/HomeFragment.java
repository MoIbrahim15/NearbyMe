package com.mohamedibrahim.nearbyme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohamedibrahim.nearbyme.R;
import com.mohamedibrahim.nearbyme.listeners.FragmentToActivityListener;

import butterknife.ButterKnife;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

public class HomeFragment extends ParentFragment {


    public static HomeFragment newInstance(FragmentToActivityListener fragmentToActivityListener, int titleRes) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setFragmentToActivityListener(fragmentToActivityListener);
        homeFragment.setTitleRes(titleRes);
        return homeFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, view);
//        String ll = "ll=" + "30.111,30.111" + "&query=asasdsdgfsfs";
//        manager.createRequest("explore?", ll, null);


        return view;
    }
}
