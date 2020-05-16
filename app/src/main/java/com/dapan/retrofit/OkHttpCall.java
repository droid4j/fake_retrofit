package com.dapan.retrofit;

import android.util.Log;

import java.io.IOException;

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
    public void enqueue(final Callback<T> callback) {
        Log.e("TAG", "入队，准备访问网络");

        okhttp3.Call call = createRawCall();

        call.enqueue(new okhttp3.Callback() {
                         @Override
                         public void onFailure(okhttp3.Call call, IOException e) {
                             if (callback != null) {
                                 callback.onFailure(OkHttpCall.this, e);
                             }
                         }

                         @Override
                         public void onResponse(okhttp3.Call call, okhttp3.Response rawResponse) throws IOException {
                             if (callback != null) {
                                 Response<T> response = new Response<T>();
                                 response.body = serviceMethod.parseResponse(rawResponse);
                                 callback.onResponse(OkHttpCall.this, response);
                             }
                         }

                     }

        );
    }

    private okhttp3.Call createRawCall() {
        Request request = serviceMethod.toRequest(args);
        Log.e("TAG", "request: " + request.toString());
        okhttp3.Call newCall = serviceMethod.callFactory.newCall(request);
        return newCall;
    }
}
