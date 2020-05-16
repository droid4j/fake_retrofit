package com.dapan.app;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by per4j
 * on 2020/5/16
 */
public interface ServiceApi {

    @GET("wxarticle/list/405/1/json")
    public Call<BaseResult<JsonObject>> searchArticle(@Query("k") String key);

    @POST("user/login")
    @FormUrlEncoded
    public Call<BaseResult<String>> login(@Field("username") String userName, @Field("password") String password);
}
