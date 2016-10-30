package com.mohamedibrahim.nearbyme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohamedibrahim.nearbyme.R;
import com.mohamedibrahim.nearbyme.listeners.FragmentToActivityListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

public class HomeFragment extends ParentFragment {

    @BindView(R.id.tabs_home)
    TabLayout tabLayout;

    public static HomeFragment newInstance(FragmentToActivityListener fragmentToActivityListener, int titleRes) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setFragmentToActivityListener(fragmentToActivityListener);
        homeFragment.setTitleRes(titleRes);
        return homeFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        initTabs();

        return view;
    }

    private void initTabs() {
        View tabMap = getActivity().getLayoutInflater().inflate(R.layout.tab_custom_views, null);
        View tabFav = getActivity().getLayoutInflater().inflate(R.layout.tab_custom_views, null);

        tabMap.findViewById(R.id.tab_map).setBackgroundResource(R.drawable.tab_map_selector);
        tabFav.findViewById(R.id.tab_favs).setBackgroundResource(R.drawable.tab_favorite_selector);
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabMap));
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabFav));
    }
}
