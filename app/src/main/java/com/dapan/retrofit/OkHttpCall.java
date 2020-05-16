package com.dapan.retrofit;

import android.util.Log;

import okhttp3.Request;

/**
 * Created by per4j
 * on 2020/5/16
 */
public class OkHttpCall<T> implements Call<T> {

    final ServiceMethod serviceMethod;
    final Object[] args;
    public OkHttpCall(ServiceMethod serviceMethod, Object[] args) {
        this.serviceMethod = serviceMethod;
        this.args = args;
    }

    @Override
    public void enqueue(Callback<T> callback) {
        Log.e("TAG", "入队，准备访问网络");

        Request request = serviceMethod.toRequest(args);
        Log.e("TAG", "request: " + request.toString());
    }
}
