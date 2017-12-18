package com.iflytek.demo.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.demo.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by jianglei on 2017/9/13.
 */

public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView text_state;
    private TextView text_msg;
    private ListView listview;
    private BlueToothDeviceAdapter adapter;
    private BluetoothAdapter bTAdatper;
    private final int BUFFER_SIZE = 1024;
    private static final String NAME = "BT_DEMO";
    private static final UUID BT_UUID = UUID.fromString("02001101-0001-1000-8080-00805F9BA9BA");
    private ConnectThread connectThread;
    private ListenerThread listenerThread;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blue_activity);
        initView();
        bTAdatper = BluetoothAdapter.getDefaultAdapter();
        initReceiver();
        listenerThread = new ListenerThread();
        listenerThread.start();
    }

    private void initReceiver() {
            //注册广播
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(mReceiver, filter);
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //避免重复添加已经绑定过的设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    adapter.add(device);
                    adapter.notifyDataSetChanged();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(BluetoothActivity.this, "开始搜索", Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(BluetoothActivity.this, "搜索完毕", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void initView() {
        findViewById(R.id.btn_openBT).setOnClickListener(this);
        findViewById(R.id.btn_search).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        text_state = (TextView) findViewById(R.id.text_state);
        text_msg = (TextView) findViewById(R.id.text_msg);
        listview = (ListView) findViewById(R.id.listView);
        adapter = new BlueToothDeviceAdapter(getApplicationContext(), R.layout.bluetooth_device_list_item);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (bTAdatper.isDiscovering()) {
                    bTAdatper.cancelDiscovery();
                }
                BluetoothDevice device = (BluetoothDevice) adapter.getItem(position);
                //连接设备
                connectDevice(device);
            }
        });
    }
    /**
     * 连接蓝牙设备
     */
    private void connectDevice(BluetoothDevice device) {

            text_state.setText("连接");

            try {
                //创建Socket
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(BT_UUID);
                //启动连接线程
                connectThread = new ConnectThread(socket, true);
                connectThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    /**
     * 连接线程
     */
    private class ConnectThread extends Thread {

        private BluetoothSocket socket;
        private boolean activeConnect;
        InputStream inputStream;
        OutputStream outputStream;

        private ConnectThread(BluetoothSocket socket, boolean connect) {
            this.socket = socket;
            this.activeConnect = connect;
        }

        @Override
        public void run() {
            try {
                //如果是自动连接 则调用连接方法
                if (activeConnect) {
                    socket.connect();
                }
                text_state.post(new Runnable() {
                    @Override
                    public void run() {
                        text_state.setText("连接成功");
                    }
                });

            }catch (IOException e){
                e.printStackTrace();
                text_state.post(new Runnable() {
                    @Override
                    public void run() {
                        text_state.setText("连接失败");
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_openBT:
                openBlueTooth();
                break;
            case R.id.btn_search:
                searchDevices();
                break;
            /*case R.id.btn_send:
                if (connectThread != null) {
                    connectThread.sendMsg("这是蓝牙发送过来的消息");
                }
                break;*/
        }

    }

    private void searchDevices() {
        if (bTAdatper.isDiscovering()) {
            bTAdatper.cancelDiscovery();
        }
        getBoundedDevices();
        bTAdatper.startDiscovery();
    }
    /**
     * 获取已经配对过的设备
     */
    private void getBoundedDevices() {
        //获取已经配对过的设备
        Set<BluetoothDevice> pairedDevices = bTAdatper.getBondedDevices();
        //将其添加到设备列表中
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                adapter.add(device);
            }
        }
    }
    /**
     * 开启蓝牙
     */
    private void openBlueTooth() {
        if (bTAdatper == null) {
            Toast.makeText(this, "当前设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
        }
        if (!bTAdatper.isEnabled()) {
           /* Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(i);*/
            bTAdatper.enable();
        }
        //开启被其它蓝牙设备发现的功能
        if (bTAdatper.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            //设置为一直开启
            i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
           startActivity(i);
        }
    }
        /**
         * 监听线程
         */
        private class ListenerThread extends Thread {

            private BluetoothServerSocket serverSocket;
            private BluetoothSocket socket;

            @Override
            public void run() {
                try {
                    serverSocket = bTAdatper.listenUsingRfcommWithServiceRecord(
                            NAME, BT_UUID);
                    while (true) {
                        //线程阻塞，等待别的设备连接
                        socket = serverSocket.accept();
                        text_state.post(new Runnable() {
                            @Override
                            public void run() {
                                text_state.setText("连接。。。");
                            }
                        });
                        connectThread = new ConnectThread(socket, false);
                        connectThread.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}
