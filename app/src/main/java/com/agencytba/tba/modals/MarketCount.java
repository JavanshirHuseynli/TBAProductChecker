package com.agencytba.tba.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketCount {
    @SerializedName("employee_id")
    @Expose
    private String employee_id;
    @SerializedName("market_name")
    @Expose
    private String market_name;
    @SerializedName("market_count")
    @Expose
    private int market_count;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    public String getEmployee_id() {
        return employee_id;
    }

    public String getMarket_name() {
        return market_name;
    }

    public int getMarket_count() {
        return market_count;
    }

    public String getCompany() {
        return company;
    }

    public String getCreated_at() {
        return created_at;
    }

    public MarketCount(String employee_id, String market_name, int market_count, String company, String created_at) {
        this.employee_id = employee_id;
        this.market_name = market_name;
        this.market_count = market_count;
        this.company = company;
        this.created_at = created_at;
    }
}
