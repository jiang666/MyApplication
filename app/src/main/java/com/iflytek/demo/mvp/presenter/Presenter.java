package com.iflytek.demo.mvp.presenter;

/**
 * Created by jianglei on 2017/9/5.
 * 连接view
 */

public interface Presenter<V> {
    void attachView(V view);
    void detachView();
}
