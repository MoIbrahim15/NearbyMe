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
 * Created by Mohamed Ibrahim on 11/1/2016.
 **/
public class FavoriteFragment extends ParentFragment {


    public static FavoriteFragment newInstance(FragmentToActivityListener fragmentToActivityListener) {
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        favoriteFragment.setFragmentToActivityListener(fragmentToActivityListener);
        return favoriteFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
