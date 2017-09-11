package com.iflytek.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iflytek.demo.buttombar.ButtomBarActivity;
import com.iflytek.demo.evenbus.MessageEven;
import com.iflytek.demo.filesave.FiileActivity;
import com.iflytek.demo.httpdemo.NetActivity;
import com.iflytek.demo.jni.JNIActivity;
import com.iflytek.demo.multilevelmenu.MulmenuActivity;
import com.iflytek.demo.mvp.view.MainActivity;
import com.iflytek.demo.photo.PhotoActivity;
import com.iflytek.demo.recyclerviewdemo.EndLessOnScrollListener;
import com.iflytek.demo.recyclerviewdemo.MyRecyclerAdapter;
import com.iflytek.demo.titlebar.TitleBarActivity;
import com.iflytek.demo.view.CustomActivity;
import com.iflytek.demo.wifi.WIFIActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by jianglei on 2017/2/24.
 */

public class MainTActivity extends AppCompatActivity {
    private RecyclerView recyclerView ;
    private ArrayList mDatas;
    private MyRecyclerAdapter recycleAdapter;
    private Button recycler_add;
    private  Button recycler_delete;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        recyclerView = (RecyclerView)findViewById(R.id.recylerview);
        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.layout_swipe_refresh);
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

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh() {
                //我在List最前面加入一条数据
                mDatas.add(0, "嘿，我是“下拉刷新”生出来的");
                System.out.print("====================");
                Log.e("000000000000000","------------------");
                //数据重新加载完成后，提示数据发生改变，并且设置现在不在刷新
                recycleAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
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
        /**
         * 监听addOnScrollListener这个方法，新建我们的EndLessOnScrollListener
         * 在onLoadMore方法中去完成上拉加载的操作
         * */
        recyclerView.addOnScrollListener(new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreData();
            }
        });
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recyclerView.setAdapter(recycleAdapter);
        /*recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));*/
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageEvent(MessageEven event){
        Toast.makeText(getApplication(), event.message, Toast.LENGTH_SHORT).show();
    }


    private void loadMoreData(){
        for (int i =0; i < 10; i++){
            mDatas.add("嘿，我是“上拉加载”生出来的"+i);
            recycleAdapter.notifyDataSetChanged();
        }
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
            case "侧滑":
                intent = new Intent(this, CustomActivity.class);
                startActivity(intent);
                break;
            case "保存文件":
                intent = new Intent(this, FiileActivity.class);
                startActivity(intent);
                break;
            case "拍照":
                intent = new Intent(this, PhotoActivity.class);
                startActivity(intent);
                break;
            case "mvp":
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case "jni":
                intent = new Intent(this, JNIActivity.class);
                startActivity(intent);
                break;
            case "WIFI":
                intent = new Intent(this, WIFIActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    private void initData() {
        mDatas = new ArrayList<String>();
        for ( int i=0; i < 20; i++) {
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
                case 4:
                    mDatas.add("侧滑");
                    break;
                case 5:
                    mDatas.add("保存文件");
                    break;
                case 6:
                    mDatas.add("拍照");
                    break;
                case 7:
                    mDatas.add("mvp");
                    break;
                case 8:
                    mDatas.add("WIFI");
                    break;
                case 9:
                    mDatas.add("jni");
                    break;
                default:
                    mDatas.add("this"+i);
                    break;
            }
        }
    }
}
