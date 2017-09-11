package com.iflytek.demo.mvp.view;

import com.iflytek.demo.mvp.model.MainModelBean;

/**
 * Created by jianglei on 2015/9/23.
 * 处理业务需要哪些方法
 */
public interface MainView {
    void showData(MainModelBean mainModelBean);
    void showProgress();
    void hideProgress();
}
