package com.iflytek.mscv5plusdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iflytek.mscv5plusdemo.buttombar.ButtomBarActivity;
import com.iflytek.mscv5plusdemo.httpdemo.NetActivity;
import com.iflytek.mscv5plusdemo.multilevelmenu.MulmenuActivity;
import com.iflytek.mscv5plusdemo.recyclerviewdemo.MyRecyclerAdapter;
import com.iflytek.mscv5plusdemo.titlebar.TitleBarActivity;

import java.util.ArrayList;

/**
 * Created by jianglei on 2017/2/24.
 */

public class MainTActivity extends Activity {
    private RecyclerView recyclerView ;
    private ArrayList mDatas;
    private MyRecyclerAdapter recycleAdapter;
    private Button recycler_add;
    private  Button recycler_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        recyclerView = (RecyclerView)findViewById(R.id.recylerview);
        recycler_delete = (Button)findViewById(R.id.recycler_delete);
        recycler_add = (Button)findViewById(R.id.recycler_add);
        initData();
        recycleAdapter= new MyRecyclerAdapter(MainTActivity.this , mDatas );
        recycler_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleAdapter.removeData(1);
            }
        });
        recycler_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleAdapter.addData(1);
            }
        });
        recycleAdapter.setOnItemClickLitener(new MyRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainTActivity.this, mDatas.get(position) + " click",
                        Toast.LENGTH_SHORT).show();
                intoItem(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainTActivity.this, mDatas.get(position) + " longclick",
                        Toast.LENGTH_SHORT).show();
                recycleAdapter.removeData(position);
            }
        });
       /* recycler_add.setVisibility(View.GONE);
        recycler_delete.setVisibility(View.GONE);*/
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recyclerView.setAdapter(recycleAdapter);
        /*recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));*/
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
    private void intoItem(int position){
        String item = (String) mDatas.get(position);
        Intent intent = null;
        switch (item) {
            case "多级筛选":
                intent = new Intent(this, MulmenuActivity.class);
                startActivity(intent);
                break;
            case "底部导航":
                intent = new Intent(this, ButtomBarActivity.class);
                startActivity(intent);
                break;
            case "标题栏":
                intent = new Intent(this, TitleBarActivity.class);
                startActivity(intent);
                break;
            case "网络请求":
                intent = new Intent(this, NetActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    private void initData() {
        mDatas = new ArrayList<String>();
        for ( int i=0; i < 42; i++) {
            switch (i) {
                case 0:
                    mDatas.add("多级筛选");
                    break;
                case 1:
                    mDatas.add("底部导航");
                    break;
                case 2:
                    mDatas.add("标题栏");
                    break;
                case 3:
                    mDatas.add("网络请求");
                    break;
                default:
                    mDatas.add("this"+i);
                    break;
            }
        }
    }

   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recycler_add:
                recycleAdapter.addData(1);
                break;
            case R.id.recycler_delete:
                recycleAdapter.removeData(1);
                break;
            default:
                break;
        }
    }*/
}
