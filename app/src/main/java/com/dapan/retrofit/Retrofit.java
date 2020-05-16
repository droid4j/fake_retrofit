package com.dapan.retrofit;

import com.dapan.app.ServiceApi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;

/**
 * Created by per4j
 * on 2020/5/16
 */
public class Retrofit {

    private final Map<Method, ServiceMethod> serviceMethodMapCache = new ConcurrentHashMap<>();

    final String baseUrl;
    final okhttp3.Call.Factory callFactory;
    public Retrofit(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.callFactory = builder.callFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> clazz) {

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }
                        ServiceMethod serviceMethod = loadServiceMethod(method);
                        OkHttpCall call = new OkHttpCall(serviceMethod, args);
                        return call;
                    }
                });
    }

    private ServiceMethod loadServiceMethod(Method method) {
        ServiceMethod serviceMethod = serviceMethodMapCache.get(method);
        if (serviceMethod == null) {
            serviceMethod = new ServiceMethod.Builder(this, method).build();
            serviceMethodMapCache.put(method, serviceMethod);
        }
        return serviceMethod;
    }

    public static class Builder {

        private String baseUrl;
        private okhttp3.Call.Factory callFactory;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder client(okhttp3.Call.Factory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        public Retrofit build() {
            if (callFactory == null) { // 创建默认的执行器
                callFactory = new OkHttpClient();
            }
            return new Retrofit(this);
        }
    }
}
