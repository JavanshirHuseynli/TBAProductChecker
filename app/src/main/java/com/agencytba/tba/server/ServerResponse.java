package com.agencytba.tba.server;

import com.agencytba.tba.modals.Categories;
import com.agencytba.tba.modals.Companies;
import com.agencytba.tba.modals.MarketCount;
import com.agencytba.tba.modals.Tasks;
import com.agencytba.tba.modals.User;
import com.agencytba.tba.modals.Markets;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponse {

    private String result;
    private String message;
    private User user;
    @SerializedName("markets")
    private List<Markets> markets;
    @SerializedName("companies")
    private List<Companies> companies;
    @SerializedName("tasks")
    private List<Tasks> tasks;
    @SerializedName("category")
    private List<Categories> category;
    @SerializedName("market_count")
    private List<MarketCount> market_count;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public List<Markets> getMarkets() {
        return markets;
    }

    public List<Companies> getCompanies() {
        return companies;
    }

    public List<Tasks> getTasks() {
        return tasks;
    }

    public List<Categories> getCategory() {
        return category;
    }

    public List<MarketCount> getMarket_count() { return  market_count; }
}