package com.agencytba.tba.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categories {

    @SerializedName("category_name")
    @Expose
    private String category_name;
    @SerializedName("subcategories")
    @Expose
    private String subcategories;
    @SerializedName("done")
    @Expose
    private boolean done;
    @SerializedName("internet_no")
    @Expose
    private boolean internet_no;

    public String getSubcategories() {
        return subcategories;
    }

    public String getCategory_name() {
        return category_name;
    }

    public boolean getDone() {
        return done;
    }

    public boolean getInternet_no() {
        return internet_no;
    }

    public void setDone(boolean done) { this.done = done; }

    public void setInternet_no(boolean internet_no) { this.internet_no = internet_no; }

    public Categories(String category_name, String subcategories, boolean done, boolean internet_no) {
        this.category_name = category_name;
        this.subcategories = subcategories;
        this.done = done;
        this.internet_no = internet_no;
    }
}

