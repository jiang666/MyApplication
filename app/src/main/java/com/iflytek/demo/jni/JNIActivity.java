package com.iflytek.demo.jni;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.iflytek.demo.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jianglei on 2017/9/10.
 */

public class JNIActivity extends AppCompatActivity{
    static{
        System.loadLibrary("Testjni");
    }

    private TimerTask task;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jni_activity);
        TextView tv=(TextView)findViewById(R.id.tv_mainactivity);
        tv.setText(getStrFromJni());
        timer = new Timer();
        task = new TimerTask(){
            @Override
            public void run() {
                final int presure = getIntpro();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (presure > 290) {
                            //当锅炉的压力大于270的时候 把timer取消 task取消
                            timer.cancel();  //把任务取消
                            task.cancel();

                            TextView tv = new TextView(getApplicationContext());
                            tv.setText("刚哥锅炉要爆炸了 快跑啊!!!");
                            tv.setTextSize(30);
                            setContentView(tv);

                            /*//播放一段报警音乐
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ylzs);
                            mediaPlayer.start();//播放音乐*/

                            return;
                        }
                        MyView myView = new MyView(getApplicationContext(), presure);
                        setContentView(myView);
                    }
                });
            }
        };
        timer.schedule(task,100,2000);
    }

    public native String getStrFromJni();
    public native int getIntpro();
}
