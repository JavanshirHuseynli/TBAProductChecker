package com.agencytba.tba.server;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("api/")
    Call<ServerResponse> operation(@Body ServerRequest request);

}