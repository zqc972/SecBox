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

import java.io.DataOutputStream;
import java.io.IOException;

import module.ArpAttacker;
import utils.ShellUtils;

/**
 * Created by zhaoqc on 17-8-27.
 */

public class ArpCheatService extends Service {

    private ArpAttacker arpAttacker = null;

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
                case 3:
                    enableTransmit(true);
                    break;
                case 4:
                    enableTransmit(false);
                    break;
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
        arpAttacker = new ArpAttacker(getApplicationContext());
        Toast.makeText(getApplicationContext(),"ArpCheatService start",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"ArpCheatService stop",Toast.LENGTH_SHORT).show();
    }

    public void getState() {
        new Thread() {
            @Override
            public void run() {
                int result_code = 1;
                try {
                    Process p = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(p.getOutputStream());
                    os.writeBytes("exit\n");
                    if(p.waitFor() == 0) {
                        result_code = 0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    Message message = new Message();
                    message.what = result_code;
                    mClient.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void start(Bundle configData) {

    }

    public void stop() {

    }

    public void enableTransmit(boolean flag) {
        if(arpAttacker != null)
            arpAttacker.enableTransmit(flag);
    }

}