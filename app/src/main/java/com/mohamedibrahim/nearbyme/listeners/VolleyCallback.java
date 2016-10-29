package com.mohamedibrahim.nearbyme.listeners;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

public interface VolleyCallback {
    void onSuccess(String methodName, Object object);

    void onFailure(String methodName, String message);

    void onFailure(String methodName, int messageRes);
}
