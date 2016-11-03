package com.mohamedibrahim.nearbyme.models.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Group {

    @SerializedName("items")
    @Expose
    private List<Item> items = new ArrayList<Item>();

    /**
     *
     * @return
     * The items
     */
    public List<Item> getItems() {
        return items;
    }
}