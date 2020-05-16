package com.dapan.retrofit;

import android.util.Log;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by per4j
 * on 2020/5/16
 * 封装为 okhttp3 的 Request 对象
 */
public class RequestBuilder {
    private String httpMethod;
    private HttpUrl httpUrl;
    private HttpUrl.Builder httpBuilder;
    public RequestBuilder(String baseUrl, String relativeUrl, String httpMethod) {
        this.httpUrl = HttpUrl.parse(baseUrl + relativeUrl);
        this.httpMethod = httpMethod;
    }

    public void addQueryName(String key, String value) {
        httpBuilder = httpUrl.newBuilder();
        httpBuilder.addQueryParameter(key, value);
    }

    public Request build() {
        Request request = new Request.Builder().url(httpBuilder.build()).build();

        return request;
    }
}
