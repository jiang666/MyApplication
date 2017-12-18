package com.iflytek.demo.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.iflytek.demo.R;

/**
 * Created by jianglei on 2017/9/2.
 */

public class CustomActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity);

    }
    @Override
    public void onClick(View v) {

    }
}
