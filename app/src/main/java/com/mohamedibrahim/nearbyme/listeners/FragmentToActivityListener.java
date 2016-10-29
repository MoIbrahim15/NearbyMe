package com.mohamedibrahim.nearbyme.listeners;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

public interface FragmentToActivityListener {

    void showSnackbar(int messageRes);

    void showSnackbar(String message);

    void hideSnackbar();

    void showHomeFragment();

    void changeTitle(int titleRes);

    void setBackToolbar(int titleRes);
}
