package com.mohamedibrahim.nearbyme.adapters;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 10/31/2016.
 **/

public class PagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragments;

    public PagerAdapter(@NonNull ArrayList<Fragment> fragments, FragmentManager fm) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }
}