package com.dapan.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dapan.retrofit.Call;
import com.dapan.retrofit.Callback;
import com.dapan.retrofit.Response;
import com.dapan.retrofit.Retrofit;
import com.google.gson.JsonObject;


/**
 * Created by per4j
 * on 2020/5/16
 */
public class RetrofitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. 构造 Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.wanandroid.com/")
                .build();

        // 2. 调用 create 创建 接口类
        RetrofitServiceApi serviceApi = retrofit.create(RetrofitServiceApi.class);

        // 3. 执行方法，并异步处理
        serviceApi.searchArticle("Java").enqueue(new Callback<BaseResult<JsonObject>>() {
            @Override
            public void onResponse(Call<BaseResult<JsonObject>> call, Response<BaseResult<JsonObject>> response) {
//                if (response.body().errorCode == 0) {
//                    Log.e("TAG", response.body().data.toString());
//                }
                Log.e("TAG", "onResponse");

            }

            @Override
            public void onFailure(Call<BaseResult<JsonObject>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
