package com.agencytba.tba.modals;

public class User {
    private String unique_id;
    private String imei_id;
    private String name;
    private String surname;
    private String city;
    private String markets;
    private String company;
    private String market_name;
    private String market_location;
    private String[] company_name;
    private String[] market;
    private String[] status;
    private String[] manager;
    private String[] supervisor;
    private String[] expeditor;
    private String[] manager2;
    private String[] supervisor2;
    private String[] expeditor2;
    private String[] supervisor3;
    private String[] expeditor3;
    private String[] brand_name;
    private String[] brand_type;
    private String[] product_name;
    private String[] total_size;
    private String[] company_size;
    private String[] percentage;
    private String[] field_supervisor;
    private String statuS;
    private String manageR;
    private String supervisoR;
    private String expeditoR;
    private String manageR2;
    private String supervisoR2;
    private String expeditoR2;
    private String supervisoR3;
    private String expeditoR3;
    private String brandName;
    private String brandType;
    private String type;
    private String productName;
    private String totalSize;
    private String companySize;
    private String percentagE;
    private String fieldSupervisor;
    private String cur_date;
    private boolean finished;

    public String getUnique_id() {
        return unique_id;
    }

    public String getImei_id() {
        return imei_id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCity() {
        return city;
    }

    public String getMarkets() {
        return markets;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public void setCur_date(String cur_date) {
        this.cur_date = cur_date;
    }

    public void setCompany(String company) { this.company = company; }

    public void setCompany_name(String[] company_name) { this.company_name = company_name; }

    public void setMarket_name(String market_name) { this.market_name = market_name; }

    public void setMarket(String[] market) { this.market = market; }

    public void setMarket_location(String market_location) { this.market_location = market_location; }

    public void setBrand_name(String[] brand_name) { this.brand_name = brand_name; }

    public void setBrand_type(String[] brand_type) { this.brand_type = brand_type; }

    public void setBrandType(String brandType) { this.brandType = brandType; }

    public void setType(String type) { this.type = type; }

    public void setProduct_name(String[] product_name) { this.product_name = product_name; }

    public void setStatus(String[] status) { this.status = status; }

    public void setManager(String[] manager) { this.manager = manager; }

    public void setSupervisor(String[] supervisor) { this.supervisor = supervisor; }

    public void setExpeditor(String[] expeditor) { this.expeditor = expeditor; }

    public void setManager2(String[] manager2) { this.manager2 = manager2; }

    public void setSupervisor2(String[] supervisor2) { this.supervisor2 = supervisor2; }

    public void setExpeditor2(String[] expeditor2) { this.expeditor2 = expeditor2; }

    public void setSupervisor3(String[] supervisor3) { this.supervisor3 = supervisor3; }

    public void setExpeditor3(String[] expeditor3) { this.expeditor3 = expeditor3; }

    public void setField_supervisor(String[] field_supervisor) { this.field_supervisor = field_supervisor; }

    public void setCompany_size(String[] company_size) { this.company_size = company_size; }

    public void setTotal_size(String[] total_size) { this.total_size = total_size; }

    public void setPercentage(String[] percentage) { this.percentage = percentage; }

    //single data

    public void setBrandName(String brandName) { this.brandName = brandName; }

    public void setProductName(String productName) { this.productName = productName; }

    public void setStatuS(String statuS) { this.statuS = statuS; }

    public void setManageR(String manageR) { this.manageR = manageR; }

    public void setSupervisoR(String supervisoR) { this.supervisoR = supervisoR; }

    public void setExpeditoR(String expeditoR) { this.expeditoR = expeditoR; }

    public void setManageR2(String manageR2) { this.manageR2 = manageR2; }

    public void setSupervisoR2(String supervisoR2) { this.supervisoR2 = supervisoR2; }

    public void setExpeditoR2(String expeditoR2) { this.expeditoR2 = expeditoR2; }

    public void setSupervisoR3(String supervisoR3) { this.supervisoR3 = supervisoR3; }

    public void setExpeditoR3(String expeditoR3) { this.expeditoR3 = expeditoR3; }

    public void setFieldSupervisor(String fieldSupervisor) { this.fieldSupervisor = fieldSupervisor; }

    public void setCompanySize(String companySize) { this.companySize = companySize; }

    public void setTotalSize(String totalSize) { this.totalSize = totalSize; }

    public void setPercentagE(String percentagE) { this.percentagE = percentagE; }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

}
