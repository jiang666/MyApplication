package com.iflytek.demo.mvp.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iflytek.demo.R;
import com.iflytek.demo.mvp.model.MainModelBean;
import com.iflytek.demo.mvp.presenter.MainPresenter;

/**
 * Created by jianglei on 2017/9/5.
 * V中实例P，可以调用P中方法。
 * P中有M和V的实例（引用）所以P方法下可以调用M和V中方法。
 * M中有P实例，所以可以调用P中方法，P方法中有V方法调用。
 */

public class MainActivity extends AppCompatActivity implements MainView {
    private TextView text;
    private ProgressBar mProgressBar;
    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mvp_activity);
        initView();
        
    }
    private void initView() {
        text = (TextView) findViewById(R.id.text);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        mMainPresenter = new MainPresenter(this);
        //制造延迟效果
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMainPresenter.loadData();
            }
        }, 2000);
    }
    @Override
    public void showData(MainModelBean mainModelBean) {
        String showData = getResources().getString(R.string.city) + mainModelBean.getCity()
                + getResources().getString(R.string.wd) + mainModelBean.getWd()
                + getResources().getString(R.string.ws) + mainModelBean.getWs()
                + getResources().getString(R.string.time) + mainModelBean.getTime();
        text.setText(showData);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        mMainPresenter.detachView();
        super.onDestroy();
    }
}
