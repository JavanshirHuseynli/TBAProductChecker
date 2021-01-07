package com.agencytba.tba.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Markets {

    @SerializedName("unique_id")
    @Expose
    private String unique_id;
    @SerializedName("market_name")
    @Expose
    private String market_name;
    @SerializedName("market_address")
    @Expose
    private String market_address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("categories_to_check")
    @Expose
    private String categories_to_check;
    @SerializedName("manager")
    @Expose
    private String manager;
    @SerializedName("manager2")
    @Expose
    private String manager2;
    @SerializedName("supervisor")
    @Expose
    private String supervisor;
    @SerializedName("supervisor2")
    @Expose
    private String supervisor2;
    @SerializedName("supervisor3")
    @Expose
    private String supervisor3;
    @SerializedName("expeditor")
    @Expose
    private String expeditor;
    @SerializedName("expeditor2")
    @Expose
    private String expeditor2;
    @SerializedName("expeditor3")
    @Expose
    private String expeditor3;
    @SerializedName("field_supervisor")
    @Expose
    private String field_supervisor;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("market_type")
    @Expose
    private String market_type;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("done")
    @Expose
    private boolean done;
    @SerializedName("internet_no")
    @Expose
    private boolean internet_no;

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) { this.unique_id = unique_id; }

    public String getMarket_name() {
        return market_name;
    }

    public String getMarket_address() { return market_address; }

    public String getCity() { return city; }

    public String getCategories_to_check() {
        return categories_to_check;
    }

    public String getManager() {
        return manager;
    }

    public String getManager2() {
        return manager2;
    }

    public String getSupervisor() { return  supervisor; }

    public String getSupervisor2() { return  supervisor2; }

    public String getSupervisor3() { return  supervisor3; }

    public String getExpeditor() { return  expeditor; }

    public String getExpeditor2() { return  expeditor2; }

    public String getExpeditor3() { return  expeditor3; }

    public String getField_supervisor() { return  field_supervisor; }

    public String getLocation() { return  location; }

    public void setLocation(String location) { this.location = location; }

    public String getCompany() { return  company; }

    public String getMarket_type() { return  market_type; }

    public String getCreated_at() {
        return created_at;
    }

    public boolean getDone() {
        return done;
    }

    public boolean getInternet_no() {
        return internet_no;
    }

    public void setDone(boolean done) { this.done = done; }

    public void setInternet_no(boolean internet_no) { this.internet_no = internet_no; }

    public Markets(String market_name, String market_address, String city, String categories_to_check, String manager, String manager2, String supervisor, String supervisor2, String supervisor3, String expeditor, String expeditor2, String expeditor3, String field_supervisor, String location, String company, String market_type, String created_at, boolean done, boolean internet_no) {
        this.market_name = market_name;
        this.market_address = market_address;
        this.city = city;
        this.categories_to_check = categories_to_check;
        this.manager = manager;
        this.manager2 = manager2;
        this.supervisor = supervisor;
        this.supervisor2 = supervisor2;
        this.supervisor3 = supervisor3;
        this.expeditor = expeditor;
        this.expeditor2 = expeditor2;
        this.expeditor3 = expeditor3;
        this.field_supervisor = field_supervisor;
        this.location = location;
        this.company = company;
        this.market_type = market_type;
        this.created_at = created_at;
        this.done = done;
        this.internet_no = internet_no;
    }
}

