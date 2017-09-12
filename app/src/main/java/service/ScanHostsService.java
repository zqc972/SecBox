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

import module.HostScanner;
import utils.NetworkUtils;

/**
 * Created by zhaoqc on 17-8-3.
 */

public class ScanHostsService extends Service{

    private Messenger mClient;
    private Messenger messenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msgFromClient) {
            mClient = msgFromClient.replyTo;
            switch (msgFromClient.what) {
                case 0:
                    stop();
                    break;
                case 1:
                    scan();
                    break;
                default:
                    Log.i("ScanHostsService","received a message from client");

                    try {
                        Message message = new Message();
                        message.replyTo = messenger;
                        message.what = 1;
                        mClient.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
            }
            super.handleMessage(msgFromClient);
        }
    });

    private HostScanner scanner = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ScanHostsService","onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("ScanHostsService","onDestroy");
    }

    private void scan() {
        int ipaddress = NetworkUtils.getIp(getApplicationContext());
        int netmask = NetworkUtils.getNetmask(getApplicationContext());
        scanner = new HostScanner(getApplicationContext(),ipaddress,netmask);
        scanner.setOnProgressListener(new HostScanner.OnProgressListener() {
            @Override
            public void onProgressChanged(int progress, int current_ip) {
                try {
                    Message message = new Message();
                    message.replyTo = messenger;
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putInt("progress",progress);
                    bundle.putInt("ip",current_ip);
                    message.setData(bundle);
                    mClient.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        scanner.setOnScannedListener(new HostScanner.OnScannedListener() {
            @Override
            public void onScanned(int ip) {
                try {
                    Message message = new Message();
                    message.replyTo = messenger;
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putInt("ip",ip);
                    String mac = NetworkUtils.getMacFromArpTable(NetworkUtils.IpToString(ip));
                    bundle.putString("mac",mac);
                    String manufacturer = NetworkUtils.getManufacturerByMac(mac);
                    bundle.putString("manufacturer",manufacturer);
                    message.setData(bundle);
                    mClient.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        scanner.scan();

    }

    private void stop() {

    }
}
