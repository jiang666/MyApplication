package com.iflytek.demo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.iflytek.demo.bluetooth.BluetoothActivity;
import com.iflytek.demo.buttombar.ButtomBarActivity;
import com.iflytek.demo.evenbus.MessageEven;
import com.iflytek.demo.face.ConStant;
import com.iflytek.demo.face.EnterActivity;
import com.iflytek.demo.face.FACEActivity;
import com.iflytek.demo.face.FileUtil;
import com.iflytek.demo.face.LocalSDK;
import com.iflytek.demo.face.RecognitionActivity;
import com.iflytek.demo.filesave.FiileActivity;
import com.iflytek.demo.httpdemo.NetActivity;
import com.iflytek.demo.jni.JNIActivity;
import com.iflytek.demo.mvp.view.MainActivity;
import com.iflytek.demo.pdf.PDFactivity;
import com.iflytek.demo.photo.PhotoActivity;
import com.iflytek.demo.recorder.RecorderActivity;
import com.iflytek.demo.recyclerviewdemo.EndLessOnScrollListener;
import com.iflytek.demo.recyclerviewdemo.FullyGridLayoutManager;
import com.iflytek.demo.recyclerviewdemo.MyRecyclerAdapter;
import com.iflytek.demo.titlebar.TitleBarActivity;
import com.iflytek.demo.util.AlertDialogHelper;
import com.iflytek.demo.view.CustomActivity;
import com.iflytek.demo.wifi.WIFIActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * 云从人脸识别解压模型
 */

public class MainTActivity extends AppCompatActivity {
    private RecyclerView recyclerView ;
    private ArrayList mDatas;
    private MyRecyclerAdapter recycleAdapter;
    private Button recycler_add;
    private  Button recycler_delete;
    private SwipeRefreshLayout mRefreshLayout;
    private ProgressBar pb;
    private LocalSDK mFaceSDK = null;
    private boolean m_bInit = false;

    private static final String PREV_VERSION_CODE = "prev_version_code";
    protected static final int COPY_FAIL = 0;
    protected static final int DIALOG_CANCEL = 1;
    String[] models;
    private ProgressDialog dialog = null;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == COPY_FAIL) {
                hideProgress();
                new AlertDialog.Builder(MainTActivity.this)
                        .setMessage("模型解压异常,请手工导入!")
                        .setNegativeButton("确定",
                                new AlertDialog.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        arg0.dismiss();
                                        finish();
                                    }
                                }).show();
            }
            if (msg.what == DIALOG_CANCEL) {
                hideProgress();
                initSDk();
            }

            super.handleMessage(msg);
        }

    };
    private AlertDialogHelper alertdialog;

    private void hideProgress() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();

    }

    protected void initSDk() {
        mFaceSDK = LocalSDK.getInstance(this);

        // 创建句柄，句柄只需要创建一次，程序启动创建，退出时再销毁
        int iRet = mFaceSDK.CreateHandles(ConStant.sLicence,
                ConStant.faceMinSize, ConStant.faceMaxSize, ConStant.sModelDir);
        if (iRet != 0) {
            // 创建句柄失败，请根据错误码检测原因
            makeToast("创建句柄失败，错误码: " + iRet);
            m_bInit = false;
        } else {
            m_bInit = true;
            Intent intent = new Intent(this, EnterActivity.class);
            startActivity(intent);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        pb = (ProgressBar)findViewById(R.id.mProgressBar);
        setProgressBarIndeterminateVisibility(true);
        recyclerView = (RecyclerView)findViewById(R.id.recylerview);
        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.layout_swipe_refresh);
        recycler_delete = (Button)findViewById(R.id.recycler_delete);
        recycler_add = (Button)findViewById(R.id.recycler_add);
        initData();
        recycleAdapter= new MyRecyclerAdapter(MainTActivity.this , mDatas );
        recycler_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    //String deviceId = tm.getDeviceId();
                    //Log.e("-----------",deviceId);
                }catch (Exception e){
                    Log.e("-----------",e.toString());
                }

                Log.e("-----------",getSDAvailableSize()+";"+getSDTotalSize());
                Log.e("============", Build.DISPLAY+";"+ Build.ID+";"+ Build.BOOTLOADER+";"+ Build.VERSION.CODENAME+
                        ";"+ Build.DEVICE+";"+ Build.HARDWARE+";"+ Build.RADIO+";"+ Build.VERSION.INCREMENTAL+";"+ Build.VERSION.SDK);
                //recycleAdapter.removeData(1);
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
            public void onItemClick(View view, final int position) {
                alertdialog = AlertDialogHelper.askBuilder(MainTActivity.this,mDatas.get(position) + " click","111111111111");
                /*Toast.makeText(MainTActivity.this, mDatas.get(position) + " click",
                        Toast.LENGTH_SHORT).show();*/
                alertdialog.setOnButtonClickListener(new AlertDialogHelper.OnButtonClickListener() {
                    @Override
                    public void onClickNo() {

                    }

                    @Override
                    public void onClickYes() {
                        intoItem(position);
                    }

                    @Override
                    public void onClickNext() {

                    }

                    @Override
                    public void onAnimationOver() {

                    }
                });
            }

            @Override
            public void onItemLongClick(View view, int position) {
                /*Toast.makeText(MainTActivity.this, mDatas.get(position) + " longclick",
                        Toast.LENGTH_SHORT).show();
                recycleAdapter.removeData(position);*/
            }
        });
       /* recycler_add.setVisibility(View.GONE);
        recycler_delete.setVisibility(View.GONE);*/
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        FullyGridLayoutManager layoutManager = new FullyGridLayoutManager(this,4);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        //设置布局管理器
        layoutManager.setOrientation(FullyGridLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        /**
         * 监听addOnScrollListener这个方法，新建我们的EndLessOnScrollListener
         * 在onLoadMore方法中去完成上拉加载的操作
         * */
        /*recyclerView.setOnScrollListener(new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreData();
            }
        });*/
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

        /*recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { //当前状态为停止滑动

                    if (!recyclerView.canScrollVertically(1)) { // 到达底部
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mDatas.add( "嘿，我是“上拉加载”生出来的");
                                //数据重新加载完成后，提示数据发生改变，并且设置现在不在刷新
                                recycleAdapter.notifyDataSetChanged();
                                mRefreshLayout.setRefreshing(false);
                                Toast.makeText(getContext(), "到达底部", Toast.LENGTH_SHORT).show();
                            }
                        },5000);

                    } else if (!recyclerView.canScrollVertically(-1)) { // 到达顶部
                        Toast.makeText(getContext(), "到达顶部", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });*/

    }

    @Override
    protected void onResume() {
        try {
            models = this.getAssets().list(ConStant.ASSERT_MODULE_DIR);
            copyModules(103, 103);
            //PreferencesUtils.putInt(this, PREV_VERSION_CODE, versionCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.onResume();
    }
    // 预加载CWModels数据
    public void copyModules(int versionCode, int prevVersionCode) {

        // 检测模型是否存在以及程序verisonCode是否不一样
        if (!checkModels(models, ConStant.sModelDir)
                || versionCode != prevVersionCode) {
            dialog = ProgressDialog.show(MainTActivity.this, "",
                    "正在努力加载数据，请稍等...", true);
            // 从assets复制模型文件到指定路劲
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileUtil.mkDir(ConStant.sModelDir);
                    for (int i = 0; i < models.length; i++) {
                        try {
                            assetsDataToSD(ConStant.ASSERT_MODULE_DIR,
                                    models[i], ConStant.sModelDir);//
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (!checkModels(models, ConStant.sModelDir)) {
                        myHandler.sendEmptyMessage(COPY_FAIL);
                    }else{
                        myHandler.sendEmptyMessageDelayed(DIALOG_CANCEL,500);
                    }


                }
            }).start();
        } else {
            myHandler.sendEmptyMessageDelayed(DIALOG_CANCEL, 500);
        }
    }
    // 拷贝assets下的文件的方法
    private void assetsDataToSD(String assertDir, String fileName,
                                String modelDir) throws IOException {

        String outputPath = modelDir + File.separator + fileName;
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(outputPath);

        String assertFilePath = assertDir + File.separator + fileName;
        myInput = this.getAssets().open(assertFilePath);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }

        myOutput.flush();
        myInput.close();
        myOutput.close();
    }
    /**
     * 检查模型是否缺失
     *
     * @param models
     * @param modelDir
     * @return false 模型缺少 true 不缺失
     */
    public boolean checkModels(String[] models, String modelDir) {

        for (String str : models) {
            File file = new File(modelDir + File.separator + str);
            if (!file.exists() || file.length() == 0)
                return false;
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁句柄
        mFaceSDK.DestoryHandles();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN) //默认方式, 在发送线程执行
    public void onMessageEvent(MessageEven event) {
        Log.e("===============",event.getMsg()+"");
        //pb.setProgress((int)event.getMsg());
    }
   /* @Subscribe
    public void onMessageEvent(MessageEven event){

        Toast.makeText(getApplication(), event.message, Toast.LENGTH_SHORT).show();
    }*/


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
                //intent = new Intent(this, MulmenuActivity.class);
                //startActivity(intent);
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
            case "BLUETOOTH":
                intent = new Intent(this, BluetoothActivity.class);
                startActivity(intent);
                break;
            case "录制视频":
                intent = new Intent(this, RecorderActivity.class);
                startActivity(intent);
                break;
            case "TEST":
                intent = new Intent(this, TestActivity.class);
                startActivity(intent);
                break;
            case "PDF":
                intent = new Intent(this, PDFactivity.class);
                startActivity(intent);
                break;
            case "人脸识别":
                intent = new Intent(this, FACEActivity.class);
                startActivity(intent);
                break;
            case "录入":
                intent = new Intent(this, EnterActivity.class);
                startActivity(intent);
                break;
            case "签到":
                intent = new Intent(this, RecognitionActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    private void initData() {
        mDatas = new ArrayList<String>();
        for ( int i=0; i < 100; i++) {
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
                case 10:
                    mDatas.add("BLUETOOTH");
                    break;
                case 11:
                    mDatas.add("录制视频");
                    break;
                case 12:
                    mDatas.add("TEST");
                    break;
                case 13:
                    mDatas.add("PDF");
                    break;
                case 14:
                    mDatas.add("人脸识别");
                    break;
                case 15:
                    mDatas.add("录入");
                    break;
                case 16:
                    mDatas.add("签到");
                    break;
                default:
                    mDatas.add("this"+i);
                    break;
            }
        }
    }
    private final String getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(this, blockSize * totalBlocks);
    }

    private final String getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        int blockSize = stat.getBlockSize();
        int availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(this, blockSize * availableBlocks) ;
    }
    public void	makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
