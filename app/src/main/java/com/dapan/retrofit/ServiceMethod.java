package com.dapan.retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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
    public ServiceMethod(Builder builder) {
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
