package com.mohamedibrahim.nearbyme.models.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Ibrahim on 11/3/2016.
 **/

public class Places {

    @SerializedName("groups")
    @Expose
    private List<Group> groups = new ArrayList<Group>();
    @SerializedName("warning")
    @Expose
    private Warning warning;

    /**
     *
     * @return
     * The groups
     */
    public List<Group> getGroups() {
        return groups;
    }

    public Warning getWarning() {
        return warning;
    }
}