package com.mohamedibrahim.nearbyme.volley;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mohamedibrahim.nearbyme.R;
import com.mohamedibrahim.nearbyme.listeners.VolleyCallback;

import org.json.JSONObject;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

class JSONObjectRequest extends JsonObjectRequest {


    private JSONObjectRequest(String url, VolleyListener callback, JSONObject body, int timeout, int maxRetry) {
        super(Method.GET, url, body, callback, callback);
        setRetryPolicy(new DefaultRetryPolicy(timeout, maxRetry,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public static class RequestBuilder {
        private Context context;
        private String methodName;
        private VolleyCallback callback;
        private String parameters;
        private Class<?> object = null;
        private final int DEFAULT_REQUEST_TIME_OUT = 15000;
        private final int DEFAULT_MAX_RETRY = 2;
        private int timeout = -1, maxRetry = -1;
        private String mainUrl;
        private boolean showLog;

        public RequestBuilder(Context context) {
            this.context = context;
            mainUrl = context.getString(R.string.main_url);
            maxRetry = context.getResources().getInteger(R.integer.max_retry);
            timeout = context.getResources().getInteger(R.integer.time_out);
            showLog = context.getResources().getBoolean(R.bool.log);
        }

        public RequestBuilder setMethodName(@NonNull String methodName) {
            this.methodName = methodName;
            return this;
        }

        public RequestBuilder setCallback(@NonNull VolleyCallback callback) {
            this.callback = callback;
            return this;
        }

        public RequestBuilder setParameters(@NonNull String parameters) {
            this.parameters = parameters;
            return this;
        }

        public RequestBuilder setReturnType(@Nullable Class<?> object) {
            this.object = object;
            return this;
        }

        public void start() throws NullPointerException {
            if (mainUrl.isEmpty()) {
                throw new NullPointerException("PLEASE, #R.string.main_url CAN NOT EMPTY OR NULL!");
            }
            if (methodName.isEmpty()) {
                throw new NullPointerException("PLEASE, USE #setMethodName METHOD.#methodName CAN NOT EMPTY OR NULL!");
            }

            if (callback == null) {
                throw new NullPointerException("PLEASE, USE #setCallback METHOD.#callback CAN NOT BE NULL!");
            }

            if (parameters == null) {
                throw new NullPointerException("PLEASE, USE #setParameters METHOD.#parameters CAN NOT BE NULL!");
            }

            if (timeout <= 8000) {
                timeout = DEFAULT_REQUEST_TIME_OUT;
            }

            if (maxRetry <= 0) {
                maxRetry = DEFAULT_MAX_RETRY;
            }
            String url = mainUrl.concat(methodName).concat(parameters);
            if (showLog) {
                Log.d("HTTP REQUEST", url);
            }
            VolleyHandler.addToQueue(context, new JSONObjectRequest(url,
                    new VolleyListener(methodName, callback, object, showLog), null, timeout, maxRetry));
        }
    }
}
