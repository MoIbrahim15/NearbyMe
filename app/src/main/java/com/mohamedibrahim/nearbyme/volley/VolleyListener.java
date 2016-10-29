package com.mohamedibrahim.nearbyme.volley;

import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mohamedibrahim.nearbyme.R;
import com.mohamedibrahim.nearbyme.listeners.VolleyCallback;
import com.mohamedibrahim.nearbyme.models.general_response.ResponseObject;

import org.json.JSONObject;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

class VolleyListener implements Response.Listener<JSONObject>, Response.ErrorListener {

    private final Class<?> object;
    private VolleyCallback callback;
    private String methodName;
    private boolean showLog;

    VolleyListener(String methodName, VolleyCallback callback, Class<?> object, boolean showLog) {
        this.methodName = methodName;
        this.callback = callback;
        this.object = object;
        this.showLog = showLog;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (showLog) {
            Log.e(methodName.concat(" ERROR"), error.toString());
        }
        callback.onFailure(methodName, getWarningRes(error));
    }

    @Override
    public void onResponse(JSONObject response) {
        if (showLog) {
            Log.d(methodName.concat(" RESPONSE"), response.toString());
        }
        try {
            ResponseObject responseObject = new Gson().fromJson(response.toString(), ResponseObject.class);
            if (responseObject.getMeta().getCode().equalsIgnoreCase("200")) {
                String json;
                json = new Gson().toJson(responseObject.getResponse());
                callback.onSuccess(methodName, object != null ?
                        new Gson().fromJson(json, object) :
                        responseObject.getResponse());
            } else {
                callback.onFailure(methodName, responseObject.getMeta().getErrorDetail());
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailure(methodName, R.string.some_error_occurred);
        }
    }

    private int getWarningRes(VolleyError error) {
        int warningRes;
        if (error instanceof NetworkError) {
            warningRes = R.string.no_connection;
        } else if (error instanceof TimeoutError) {
            warningRes = R.string.time_out_error;
        } else if (error instanceof ServerError) {
            warningRes = R.string.server_error;
        } else {
            warningRes = R.string.some_error_occurred;
        }
        return warningRes;
    }
}
