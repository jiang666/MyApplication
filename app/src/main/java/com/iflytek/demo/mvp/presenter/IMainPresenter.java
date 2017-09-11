package com.iflytek.demo.mvp.presenter;

import com.iflytek.demo.mvp.model.MainModelBean;

/**
 * Created by jianglei on 2017/9/5.
 * 此接口连接model
 */

public interface IMainPresenter {
    void loadDataSuccess(MainModelBean mainModelBean);
    void loadDataFailure();
}
