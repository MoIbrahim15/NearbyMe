package com.mohamedibrahim.nearbyme.models.places;

/**
 * Created by Mohamed Ibrahim on 11/3/2016.
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("crossStreet")
    @Expose
    private String crossStreet;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("distance")
    @Expose
    private String distance;

    /**
     * @return The address
     */
    public String getAddress() {
        return address;
    }


    /**
     * @return The crossStreet
     */
    public String getCrossStreet() {
        return crossStreet;
    }

    /**
     * @return The lat
     */
    public Double getLat() {
        return lat;
    }

    /**
     * @return The lng
     */
    public Double getLng() {
        return lng;
    }

    /**
     * @return The distance
     */
    public String getDistance() {
        return distance;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCrossStreet(String crossStreet) {
        this.crossStreet = crossStreet;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}