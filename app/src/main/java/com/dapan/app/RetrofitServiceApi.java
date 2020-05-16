package com.dapan.app;

import com.dapan.retrofit.GET;
import com.dapan.retrofit.Query;
import com.google.gson.JsonObject;

/**
 * Created by per4j
 * on 2020/5/16
 */
public interface RetrofitServiceApi {

    @GET("wxarticle/list/405/1/json")
    public com.dapan.retrofit.Call<BaseResult<JsonObject>> searchArticle(@Query("k") String key);

}
