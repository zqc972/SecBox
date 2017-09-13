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

/**
 * Created by zhaoqc on 17-8-27.
 */

public class ArpCheatService extends Service {

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(),"ArpCheatService start",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"ArpCheatService stop",Toast.LENGTH_SHORT).show();
    }

    public void getState() {

    }

    public void start(Bundle configData) {

    }

    public void stop() {

    }

}