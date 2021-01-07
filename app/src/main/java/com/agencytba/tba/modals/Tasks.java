package com.agencytba.tba.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tasks {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("categories_to_check")
    @Expose
    private String categories_to_check;
    @SerializedName("brand_name")
    @Expose
    private String brand_name;
    @SerializedName("brand_type")
    @Expose
    private String brand_type;
    @SerializedName("product_name")
    @Expose
    private String product_name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("task_count")
    @Expose
    private String task_count;

    public int getId() {
        return id;
    }

    public String getCategories_to_check() {
        return categories_to_check;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public String getBrand_type() { return brand_type; }

    public String getProduct_name() { return product_name; }

    public String getType() {
        return type;
    }

    public String getCompany() { return company; }

    public String getImage() { return image; }

    public String getCreated_at() {
        return created_at;
    }

    public String getTask_count() {
        return task_count;
    }

    public Tasks(int id, String categories_to_check, String brand_name, String brand_type, String product_name, String type, String image, String company, String created_at) {
        this.id = id;
        this.categories_to_check = categories_to_check;
        this.brand_name = brand_name;
        this.brand_type = brand_type;
        this.product_name = product_name;
        this.type = type;
        this.company = company;
        this.image = image;
        this.created_at = created_at;
    }

}

