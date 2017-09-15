package service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.yanzhenjie.andserver.Server;

import java.util.ArrayList;

import module.PortScanner;

/**
 * Created by zhaoqc on 17-9-13.
 */

public class ScanPortsService extends Service {

    private Messenger mClient;
    private Messenger messenger = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msgFromClient) {
            switch (msgFromClient.what) {
                case 0:
                    mClient = msgFromClient.replyTo;
                    getState();
                    break;
                case 1:
                    start(msgFromClient.getData());
                    break;
                case 2:
                    stop();
                default:
                    Toast.makeText(getApplicationContext(),"Received a message from client" ,Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msgFromClient);
        }
    });

    private PortScanner portScanner = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        portScanner = new PortScanner();
        Toast.makeText(getApplicationContext(),"ScanPortsService start",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"ScanPortsService stop",Toast.LENGTH_SHORT).show();
    }

    public void getState() {
        try {
            Message message = new Message();
            message.what = 0;
            mClient.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void start(Bundle configData) {
        //添加目标主机
        ArrayList<String> hostsList = configData.getStringArrayList("hosts");
        portScanner.setHostList(hostsList);
        //设置等待扫描的端口

        //设置扫描的线程

        portScanner.setOnScannedListener(new PortScanner.OnScannedListener() {
            @Override
            public void onScanned(String host, int port) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("host",host);
                    bundle.putInt("port",port);
                    Message message = new Message();
                    message.what = 1;
                    message.setData(bundle);
                    mClient.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        //设置是否反复扫描
        portScanner.start(false);
    }

    public void stop() {
        portScanner.stop();
    }
}
