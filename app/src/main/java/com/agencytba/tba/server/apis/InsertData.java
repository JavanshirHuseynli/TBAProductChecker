package com.agencytba.tba.server.apis;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface InsertData {

    @FormUrlEncoded
    @POST("apitest/insert.php")
    Call<ResponseBody> InsertOSA(
            @Field("operation") String operation,
            @Field("brand_name") String brand_name,
            @Field("brand_type") String brand_type,
            @Field("product_name") String product_name,
            @Field("status") String status,
            @Field("manager") String manager,
            @Field("manager2") String manager2,
            @Field("supervisor") String supervisor,
            @Field("expeditor") String expeditor,
            @Field("supervisor2") String supervisor2,
            @Field("expeditor2") String expeditor2,
            @Field("supervisor3") String supervisor3,
            @Field("expeditor3") String expeditor3,
            @Field("field_supervisor") String field_supervisor,
            @Field("market_name") String market_name,
            @Field("company_name") String company_name,
            @Field("created_at") String created_at
    );

    @FormUrlEncoded
    @POST("apitest/insert.php")
    Call<ResponseBody>InsertNPD(
            @Field("operation") String operation,
            @Field("brand_name") String brand_name,
            @Field("brand_type") String brand_type,
            @Field("product_name") String product_name,
            @Field("status") String status,
            @Field("manager") String manager,
            @Field("manager2") String manager2,
            @Field("supervisor") String supervisor,
            @Field("expeditor") String expeditor,
            @Field("supervisor2") String supervisor2,
            @Field("expeditor2") String expeditor2,
            @Field("supervisor3") String supervisor3,
            @Field("expeditor3") String expeditor3,
            @Field("field_supervisor") String field_supervisor,
            @Field("market_name") String market_name,
            @Field("company_name") String company_name,
            @Field("created_at") String created_at
    );

    @FormUrlEncoded
    @POST("apitest/insert.php")
    Call<ResponseBody>InsertPlanogram(
            @Field("operation") String operation,
            @Field("brand_type") String brand_type,
            @Field("product_name") String product_name,
            @Field("status") String status,
            @Field("market_name") String market_name,
            @Field("company_name") String company_name,
            @Field("created_at") String created_at
    );

    @FormUrlEncoded
    @POST("apitest/insert.php")
    Call<ResponseBody>InsertSOS(
            @Field("operation") String operation,
            @Field("brand_name") String brand_name,
            @Field("brand_type") String brand_type,
            @Field("total_size") String total_size,
            @Field("company_size") String company_size,
            @Field("percentage") String percentage,
            @Field("market_name") String market_name,
            @Field("company_name") String company_name,
            @Field("created_at") String created_at
    );

    @FormUrlEncoded
    @POST("apitest/insert.php")
    Call<ResponseBody>InsertSOD(
            @Field("operation") String operation,
            @Field("brand_name") String brand_name,
            @Field("brand_type") String brand_type,
            @Field("total_size") String total_size,
            @Field("company_size") String company_size,
            @Field("percentage") String percentage,
            @Field("market_name") String market_name,
            @Field("company_name") String company_name,
            @Field("created_at") String created_at
    );

    @FormUrlEncoded
    @POST("apitest/insert.php")
    Call<ResponseBody>InsertMarketCount(
            @Field("operation") String operation,
            @Field("unique_id") String unique_id,
            @Field("market_name") String market_name,
            @Field("company_name") String company_name
    );

}
