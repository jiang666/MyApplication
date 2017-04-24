package com.iflytek.mscv5plusdemo.multilevelmenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.mscv5plusdemo.R;

import java.util.ArrayList;

/**
 * Created by jianglei on 2017/3/20.
 */

public class MulmenuActivity extends Activity{
    private TextView tv_title;
    private Toolbar mToolbar;
    private static final String TAG = "MulmenuActivity";
    private ExpandTabView expandTabView;
    private ArrayList<View> mViewArray = new ArrayList<View>();
    private ViewLeft viewLeft;
    private ViewMiddle viewMiddle;
    private ViewRight viewRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mulmenu_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("多级选择框");
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MulmenuActivity.this.finish();
            }
        });
        initView();
        initVaule();
        initListener();

    }

    private void initView() {
        Log.d(TAG,"initView");
        expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view);
        viewLeft = new ViewLeft(this);
        viewMiddle = new ViewMiddle(this);
        viewRight = new ViewRight(this);

    }

    private void initVaule() {
        Log.d(TAG,"initValue");
        mViewArray.add(viewLeft);
        mViewArray.add(viewMiddle);
        mViewArray.add(viewRight);
        ArrayList<String> mTextArray = new ArrayList<String>();
        mTextArray.add("距离");
        mTextArray.add("区域");
        mTextArray.add("距离");
        expandTabView.setValue(mTextArray, mViewArray);//将三个下拉列表设置进去
        expandTabView.setTitle(viewLeft.getShowText(), 0);
        expandTabView.setTitle(viewMiddle.getShowText(), 1);
        expandTabView.setTitle(viewRight.getShowText(), 2);

    }

    private void initListener() {
        Log.d(TAG,"initListener");
        viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                Log.d("ViewLeft", "OnSelectListener, getValue");
                onRefresh(viewLeft, showText);
            }
        });

        viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {

            @Override
            public void getValue(String showText) {
                Log.d("ViewMiddle","OnSelectListener, getValue");
                onRefresh(viewMiddle,showText);

            }
        });

        viewRight.setOnSelectListener(new ViewRight.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                Log.d("ViewRight","OnSelectListener, getValue");
                onRefresh(viewRight, showText);
            }
        });

    }

    private void onRefresh(View view, String showText) {
        Log.d(TAG,"onRefresh,view:"+view+",showText:"+showText);
        expandTabView.onPressBack();
        int position = getPositon(view);
        if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
            expandTabView.setTitle(showText, position);
        }
        Toast.makeText(MulmenuActivity.this, showText, Toast.LENGTH_SHORT).show();

    }

    private int getPositon(View tView) {
        Log.d(TAG,"getPosition");
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onBackPressed() {

        if (!expandTabView.onPressBack()) {
            finish();
        }

    }
}
