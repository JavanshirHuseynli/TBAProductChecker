package com.agencytba.tba.server;

import com.agencytba.tba.modals.Categories;
import com.agencytba.tba.modals.Companies;
import com.agencytba.tba.modals.Tasks;
import com.agencytba.tba.modals.User;
import com.agencytba.tba.modals.Markets;

public class ServerRequest {

    private String operation;
    private User user;
    private Markets markets;
    private Companies companies;
    private Tasks tasks;
    private Categories categories;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    public void setMarkets(Markets markets) {
        this.markets = markets;
    }

    public void setCompanies(Companies companies) {
        this.companies = companies;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

}
