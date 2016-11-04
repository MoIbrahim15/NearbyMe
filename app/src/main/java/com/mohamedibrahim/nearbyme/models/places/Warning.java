package com.mohamedibrahim.nearbyme.models.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohamed Ibrahim on 11/4/2016.
 **/

public class Warning {
    @SerializedName("text")
    @Expose
    private String text;

    public String getText() {
        return text;
    }
}
