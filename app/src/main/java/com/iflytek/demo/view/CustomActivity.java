package com.iflytek.demo.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iflytek.demo.R;
import com.nineoldandroids.view.ViewHelper;

import java.util.Random;

/**
 * Created by jianglei on 2017/9/2.
 */

public class CustomActivity extends Activity implements View.OnClickListener{
    private ImageView iv_header;
    private Draglayout dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_activity);
        iv_header = (ImageView) findViewById(R.id.iv_header);
        iv_header.setOnClickListener(this);

        final ListView lv_left = (ListView) findViewById(R.id.lv_left);
        lv_left.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView)view).setTextColor(Color.WHITE);
                return view;
            }
        });

        ListView lv_main = (ListView) findViewById(R.id.lv_main);
        lv_main.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.NAMES));


        dl = (Draglayout) findViewById(R.id.dl);

        MyLinearLayout ll_main = (MyLinearLayout) findViewById(R.id.ll_main);
        ll_main.setDragLayout(dl);


        dl.setDragUpdateListener(new Draglayout.OnDragUpdateListener() {
            @Override
            public void onOpen() {
//				Utils.showToast(getApplicationContext(), "onOpen");
                Random random = new Random();
                lv_left.smoothScrollToPosition(random.nextInt(50));
            }

            @Override
            public void onDraging(float percent) {
                System.out.println("onDraging: " + percent);
//				Utils.showToast(getApplicationContext(), "onDraging:" + percent);
                // 0.0 -> 1.0
                // 1.0 -> 0.0
                ViewHelper.setAlpha(iv_header, 1 - percent);

            }

            @Override
            public void onClose() {
//				Utils.showToast(getApplicationContext(), "onClose");
//				iv_header.setTranslationX(translationX)
                ObjectAnimator animator = ObjectAnimator.ofFloat(iv_header, "translationX", 15f);

                animator.setInterpolator(new CycleInterpolator(4));
                animator.setDuration(500);
                animator.start();
            }
        });

    }

    @Override
    public void onClick(View v) {
        dl.open();
    }

}
