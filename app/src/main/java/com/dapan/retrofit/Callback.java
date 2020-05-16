package com.dapan.retrofit;

/**
 * Created by per4j
 * on 2020/5/16
 */
public interface Callback<T> {

    void onResponse(Call<T> call, Response<T> response);

    void onFailure(Call<T> call, Throwable t);
}
