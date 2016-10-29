package com.mohamedibrahim.nearbyme.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

public class VolleyHandler {

    private static RequestQueue queue;

    private VolleyHandler() {
    }

    private static void initVolleyQueue(Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static JSONObjectRequest.RequestBuilder createRequest(Context context) {
        return new JSONObjectRequest.RequestBuilder(context);
    }

    static void addToQueue(Context context, JSONObjectRequest request) {
        initVolleyQueue(context);
        queue.add(request);
    }
}
