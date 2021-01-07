package com.agencytba.tba.server.apis;

import com.agencytba.tba.server.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static ApiClient apiClient;
    private Retrofit retrofit;

    private ApiClient(){
        retrofit=new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }
    public static synchronized ApiClient getInstance(){
        if (apiClient==null){
            apiClient=new ApiClient();
        }
        return apiClient;
    }
    public InsertData getApi(){
        return retrofit.create(InsertData.class);
    }
}


