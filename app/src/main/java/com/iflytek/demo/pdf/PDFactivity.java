package com.iflytek.demo.pdf;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.iflytek.demo.R;

/**
 * Created by jianglei on 2017/12/11.
 */

public class PDFactivity extends AppCompatActivity{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.pdf_activity);
        setContentView(R.layout.activity_twofaceverfy);
        /*PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
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
                .spacing(10) // in dp
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {

                    }
                })
                //.pageFitPolicy(FitPolicy.BOTH)
                .load();*/

    }
}
