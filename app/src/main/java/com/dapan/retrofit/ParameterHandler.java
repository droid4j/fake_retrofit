package com.dapan.retrofit;

/**
 * Created by per4j
 * on 2020/5/16
 */
public abstract class ParameterHandler<T> {

    abstract void apply(RequestBuilder requestBuilder, T value);

    public static class Query<T> extends ParameterHandler<T> {
        private String key;
        public Query(String key) {
            this.key = key;
        }

        @Override
        void apply(RequestBuilder requestBuilder, T value) {
            requestBuilder.addQueryName(key, value.toString());
        }
    }
}
