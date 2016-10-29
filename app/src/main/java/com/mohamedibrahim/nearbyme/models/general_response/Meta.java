package com.mohamedibrahim.nearbyme.models.general_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

public class Meta {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("errorDetail")
    @Expose
    private String errorDetail;

    /**
     * @return The code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return The errorDetail
     */
    public String getErrorDetail() {
        return errorDetail;
    }
}
