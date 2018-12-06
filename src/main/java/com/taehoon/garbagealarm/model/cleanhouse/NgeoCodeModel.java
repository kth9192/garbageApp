package com.taehoon.garbagealarm.model.cleanhouse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kth919 on 2017-11-04.
 */

public class NgeoCodeModel {

    @SerializedName("total")
    private int count;

    @SerializedName("userquery")
    private String userQuery;

    @SerializedName("items")
    private ArrayList<ItemModel> items;

    public int getCount() {
        return count;
    }

    public String getUserQuery() {
        return userQuery;
    }

    public ArrayList<ItemModel> getItems() {
        return items;
    }
}
