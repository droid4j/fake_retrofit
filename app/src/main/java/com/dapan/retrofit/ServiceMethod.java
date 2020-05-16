package com.dapan.retrofit;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;

/**
 * Created by per4j
 * on 2020/5/16
 */
public class ServiceMethod {

    final Retrofit retrofit;
    final Method method;
    final String httpMethod;
    final String relativeUrl;
    final ParameterHandler[] parameterHandlers;
    final okhttp3.Call.Factory callFactory;
    public ServiceMethod(Builder builder) {
        this.callFactory = builder.retrofit.callFactory;
        this.retrofit = builder.retrofit;
        this.method = builder.method;
        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.parameterHandlers = builder.parameterHandlers;
    }

    public Request toRequest(Object...args) {
        RequestBuilder requestBuilder = new RequestBuilder(retrofit.baseUrl, relativeUrl, httpMethod);

        @SuppressWarnings("unchecked")
        ParameterHandler<Object>[] handlers = (ParameterHandler<Object>[]) parameterHandlers;
        for (int i = 0; i < parameterHandlers.length; i++) {
            handlers[i].apply(requestBuilder, args[i]);
        }

        return requestBuilder.build();
    }

    public <T> T parseResponse(okhttp3.Response rawResponse) {
        Type type = method.getGenericReturnType();
        Gson gson = new Gson();
        type = (((ParameterizedType) type).getActualTypeArguments())[0];
        Class<T> dataClass = null;
        if (type instanceof Class) {
            dataClass = (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            dataClass = (Class<T>)((ParameterizedType) type).getActualTypeArguments()[0];
        }
        if (dataClass != null) {
            return gson.fromJson(rawResponse.body().charStream(), dataClass);
        }
        return null;
    }

    public static class Builder {

        final Retrofit retrofit;
        final Method method;
        String httpMethod;
        String relativeUrl;
        private final Annotation[] methodAnnotations;
        private final Annotation[][] parameterAnnotations;
        private ParameterHandler<?>[] parameterHandlers;
        public Builder(Retrofit retrofit, Method method) {
            this.retrofit = retrofit;
            this.method = method;
            methodAnnotations = method.getAnnotations();
            parameterAnnotations = method.getParameterAnnotations();
        }

        public ServiceMethod build() {

            for (Annotation annotation : methodAnnotations) {
                parseMethodAnnotation(annotation);
            }

            int count = parameterAnnotations.length;
            parameterHandlers = new ParameterHandler<?>[count];
            for (int i = 0; i < count; i++) {
                Annotation parameterAnnotation = parameterAnnotations[i][0];
                if (parameterAnnotation instanceof Query) {
                    parameterHandlers[i] = new ParameterHandler.Query<>(((Query) parameterAnnotation).value());
                }
            }

            return new ServiceMethod(this);
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof GET) {
                parseHttpAndPath("GET", ((GET) annotation).value());
            } else if (annotation instanceof POST) {
                parseHttpAndPath("POST", ((POST) annotation).value());
            }
        }

        private void parseHttpAndPath(String method, String value) {
            this.httpMethod = method;
            this.relativeUrl = value;
        }
    }
}
