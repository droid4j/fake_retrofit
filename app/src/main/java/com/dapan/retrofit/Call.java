package com.dapan.retrofit;

/**
 * Created by per4j
 * on 2020/5/16
 */
public interface Call<T> {

    void enqueue(Callback<T> callback);
}
