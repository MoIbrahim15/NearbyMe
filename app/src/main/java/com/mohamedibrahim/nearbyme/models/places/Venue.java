package com.mohamedibrahim.nearbyme.models.places;

/**
 * Created by Mohamed Ibrahim on 11/3/2016.
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Venue {

//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setLocation(Location location) {
//        this.location = location;
//    }
//
//    public void setRating(String rating) {
//        this.rating = rating;
//    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = new ArrayList<Category>();
    @SerializedName("rating")
    @Expose
    private String rating;

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     * The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @return
     * The categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     *
     * @return
     * The rating
     */
    public String getRating() {
        return rating;
    }

}