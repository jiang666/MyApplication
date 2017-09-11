package com.iflytek.demo.mvp.presenter;

import com.iflytek.demo.mvp.model.MainModel;
import com.iflytek.demo.mvp.model.MainModelBean;
import com.iflytek.demo.mvp.view.MainView;

/**
 * Created by jianglei on 2017/9/5.
 * View和Model的桥梁，它从Model层检索数据后，返回给View层
 */

public class MainPresenter implements IMainPresenter,Presenter<MainView>{
    private MainModel mMainModel;
    private MainView mMainView;
    public MainPresenter(MainView view) {
        attachView(view);
        mMainModel = new MainModel(this);
    }

    @Override
    public void attachView(MainView view) {
        this.mMainView = view;
    }

    @Override
    public void detachView() {
        this.mMainView = null;
    }
    public void loadData() {
        mMainView.showProgress();
        mMainModel.loadData();
    }


    @Override
    public void loadDataSuccess(MainModelBean mainModelBean) {
        mMainView.showData(mainModelBean);
        mMainView.hideProgress();
    }

    @Override
    public void loadDataFailure() {
        mMainView.hideProgress();
    }
}
