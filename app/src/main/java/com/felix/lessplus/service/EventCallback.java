package com.felix.lessplus.service;

/**
 * Created by felix on 2017/7/4.
 */
public interface EventCallback<T> {
    void onEvent(T t);
}
