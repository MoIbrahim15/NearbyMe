package com.mohamedibrahim.nearbyme.managers;

import android.support.annotation.Nullable;

import com.mohamedibrahim.nearbyme.R;
import com.mohamedibrahim.nearbyme.activities.ParentActivity;
import com.mohamedibrahim.nearbyme.listeners.VolleyCallback;
import com.mohamedibrahim.nearbyme.volley.VolleyHandler;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

public class BaseManager {

    private ParentActivity activity;
    private VolleyCallback callback;

    public static BaseManager newInstance(ParentActivity activity, VolleyCallback callback) {
        return new BaseManager(activity, callback);
    }

    private BaseManager(ParentActivity activity, VolleyCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    private String getBaseParams() {
        String params = "client_id=" + activity.getResources().getString(R.string.client_id)
                + "&client_secret=" + activity.getResources().getString(R.string.client_secret)
                + "&v=" + activity.getResources().getString(R.string.api_version) + "&";
        return params;
    }

    public void createRequest(String methodName, String params, @Nullable Class<?> object) {
        String allParams = getBaseParams().concat(params);
        try {
            VolleyHandler.createRequest(activity).setMethodName(methodName)
                    .setParameters(allParams).setCallback(callback).setReturnType(object).start();
        } catch (NullPointerException e) {
            activity.showSnackbar(e.getMessage());
        }
    }
}
