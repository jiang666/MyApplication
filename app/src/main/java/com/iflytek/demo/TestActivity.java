package com.iflytek.demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.iflytek.demo.server.TestServer;
import com.iflytek.demo.util.AlertDialogHelper;

/**
 * Created by jianglei on 2017/9/18.
 */

public class TestActivity extends AppCompatActivity {
    private Conn conn;
    private Iserver iserve;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mBinder.callBinder(2000);
                try {
                    int a = iserve.callBinder(2000);
                    int b = a +1;
                }catch (RemoteException e){
                    e.printStackTrace();
                }

            }
        });
        PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fromAsset("sample.pdf")
                .defaultPage(0)
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {

                    }
                })
                .enableAnnotationRendering(true)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                    }
                })
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(100) // in dp
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {

                    }
                })
                //.pageFitPolicy(FitPolicy.BOTH)
                .load();

        Intent intent = new Intent(this, TestServer.class);
        conn = new Conn();
        bindService(intent,conn,BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    //监听服务链接
    private class Conn implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AlertDialogHelper.toastBuilder(TestActivity.this,"开启服务",3000);
            //mBinder = (TestServer.Mybinder)service;
            iserve = Iserver.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
