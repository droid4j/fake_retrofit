package com.dapan.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://www.wanandroid.com/")
                .client(new OkHttpClient());

        ServiceApi serviceApi = builder.build().create(ServiceApi.class);

        serviceApi.searchArticle("Java").enqueue(new Callback<BaseResult<JsonObject>>() {
            @Override
            public void onResponse(Call<BaseResult<JsonObject>> call, Response<BaseResult<JsonObject>> response) {
                if (response.body().errorCode == 0) {
                    Log.e("TAG", response.body().data.toString());
                }

            }

            @Override
            public void onFailure(Call<BaseResult<JsonObject>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        serviceApi.login("test", "123123").enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(Call<BaseResult<String>> call, Response<BaseResult<String>> response) {
                if (response.body().errorCode == 0) {
                    Log.e("TAG", response.body().data.toString());
                } else {
                    Log.e("TAG", response.body().errorMsg + ", " + response.body().errorCode);
                }
            }

            @Override
            public void onFailure(Call<BaseResult<String>> call, Throwable t) {
                t.printStackTrace();
            }
        });



    }
}
