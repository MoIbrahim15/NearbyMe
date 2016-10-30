package com.mohamedibrahim.nearbyme.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.mohamedibrahim.nearbyme.R;
import com.mohamedibrahim.nearbyme.fragments.HomeFragment;

import butterknife.ButterKnife;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

public class HomeActivity extends ParentActivity {

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        fm = getFragmentManager();
        showHomeFragment();

    }

    /**
     * Using beginTransaction to replace fragment and change Actionbar title
     *
     * @param fragment to Show
     */

    private void addFragment(@NonNull Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = fm.beginTransaction();
            Fragment existFragment = fm.findFragmentById(R.id.container);
            if (existFragment != null) {
                if (existFragment.getClass() != fragment.getClass()) {
                    replaceFragment(ft, fragment);
                }
            } else {
                replaceFragment(ft, fragment);
            }
            ft.commit();
        } else {
            throw new NullPointerException("Fragment parameter can not be null");
        }
    }

    private void replaceFragment(FragmentTransaction ft, Fragment fragment) {
        if (!(fragment instanceof HomeFragment)) {
            ft.addToBackStack(null);
        } else {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        ft.replace(R.id.container, fragment);
    }

    @Override
    public void showHomeFragment() {
        addFragment(HomeFragment.newInstance(this, R.string.app_name));
    }
}
