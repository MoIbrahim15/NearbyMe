package com.mohamedibrahim.nearbyme.models.general_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

public class ResponseObject {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("response")
    @Expose
    private Object response;

    /**
     * @return The meta
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * @return The response
     */
    public Object getResponse() {
        return response;
    }
}
