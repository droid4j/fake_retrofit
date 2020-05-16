package com.dapan.retrofit;

import java.lang.reflect.Method;

/**
 * Created by per4j
 * on 2020/5/16
 */
public class ServiceMethod {

    final Retrofit retrofit;
    final Method method;
    public ServiceMethod(Builder builder) {
        this.retrofit = builder.retrofit;
        this.method = builder.method;
    }

    public static class Builder {

        final Retrofit retrofit;
        final Method method;
        public Builder(Retrofit retrofit, Method method) {
            this.retrofit = retrofit;
            this.method = method;
        }

        public ServiceMethod build() {
            return new ServiceMethod(this);
        }
    }
}
