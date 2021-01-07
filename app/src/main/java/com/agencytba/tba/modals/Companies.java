package com.agencytba.tba.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Companies {

    @SerializedName("company_name")
    @Expose
    private String company_name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    public String getCompany_name() {
        return company_name;
    }

    public String getImage() { return image; }

    public String getCreated_at() {
        return created_at;
    }

    public Companies(String company_name, String image, String created_at) {
        this.company_name = company_name;
        this.image = image;
        this.created_at = created_at;
    }

}

