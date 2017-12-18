package com.iflytek.demo.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.iflytek.demo.Iserver;


/**
 * Created by jianglei on 2017/9/21.
 */

public class TestServer extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Mybinder();
    }
    //定义一个办证的方法
    public int BanZheng(int money){

        if (money>1000) {
            //AlertDialogHelper.toastBuilder(getApplicationContext(),"0000000000",3000);
            //Toast.makeText(getApplicationContext(), "我是领导 把证给你办了", Toast.LENGTH_LONG).show();
            return money+20;
        }else {
            return money+50;
            //Toast.makeText(getApplicationContext(), "钱太少了 你懂的....", Toast.LENGTH_LONG).show();
        }
    }
   /* Iserver.Stub mBinder = new Iserver.Stub() {
        @Override
        public int callBinder(int money) throws RemoteException {
           return BanZheng(money);
        }
    };*/
   public class Mybinder extends Iserver.Stub {

        @Override
        public int callBinder(int money) throws RemoteException {
            return BanZheng(money);
        }
    }

}
