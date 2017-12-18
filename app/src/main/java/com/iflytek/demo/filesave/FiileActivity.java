package com.iflytek.demo.filesave;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.demo.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jianglei on 2017/9/2.
 */

public class FiileActivity extends Activity {
    EditText text;
    Button button;
    Button button2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_activity);
        text = (EditText) findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(text.getText().toString());
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read();
            }
        });
        //获取SharedPreferences对象
        Context ctx = FiileActivity.this;
        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        //存入数据
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("STRING_KEY", "string");
        editor.putInt("INT_KEY", 0);
        editor.putBoolean("BOOLEAN_KEY", true);
        editor.commit();
        //返回STRING_KEY的值
        Log.d("SP", sp.getString("STRING_KEY", "none"));
        //如果NOT_EXIST不存在，则返回值为"none"
        Log.d("SP", sp.getString("NOT_EXIST", "none"));
    }
    public void save(String text)
    {
        try {
            //String string = getCacheDir().toString();
            ///data/user/0/com.example.jianglei.com.iflytek.demo/cache
            //String string = getExternalCacheDir().toString();
            ///storage/emulated/0/Android/data/com.example.jianglei.com.iflytek.demo/cache
            String string = Environment.getExternalStorageDirectory().toString();
            ///storage/emulated/0
            FileOutputStream outStream=this.openFileOutput("b.txt",Context.MODE_WORLD_READABLE);
            outStream.write(text.getBytes());
            outStream.close();
            Toast.makeText(FiileActivity.this,"Saved_"+ string,Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            return;
        }
        catch (IOException e){
            return ;
        }
    }
    /**
    * 从文本中读取json字符串，转化为bean，在从bean中取值
     * 读取的是NetActivity中存储的文件
    * */
    public void read(){
        /*//文件的读取
        String fileName = "b.txt";
        //也可以用String fileName = "mnt/sdcard/Y.txt";
        String res="";
        try{
            //FileInputStream fin = new FileInputStream(fileName);
            FileInputStream fin = openFileInput(fileName);
            //用这个就不行了，必须用FileInputStream
            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        }catch(Exception e){
            e.printStackTrace();
        }*/
        //text.setText(res);
        //String fileName = "a.txt";时，下面
        /*Gson gson = new Gson();
        TestBean bean = (TestBean)gson.fromJson(res, TestBean.class);

        text.setText(bean.getApiKey());*/
    }

}
