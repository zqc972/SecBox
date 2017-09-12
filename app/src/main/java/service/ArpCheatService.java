package service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by zhaoqc on 17-8-27.
 */

public class ArpCheatService extends Service {

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
                    start();
                    break;
                default:
                    Log.i("ArpCheatService", "received a message from client");

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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ArpCheatService", "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("ArpCheatService","onDestroy");
    }

    private void start() {

    }

    private void stop() {

    }
}