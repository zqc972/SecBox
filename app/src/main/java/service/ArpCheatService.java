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
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isRunning",arpAttacker.isRunning());
                    String transmit_flag = "0";
                    try {
                        transmit_flag = ShellUtils.run(new String[]{"cat","/proc/sys/net/ipv4/ip_forward"},"/");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    transmit_flag = transmit_flag.replace("\n","");

                    int flag = Integer.parseInt(transmit_flag);
                    if(flag == 0)
                        bundle.putBoolean("transmit_flag", false);
                    else
                        bundle.putBoolean("transmit_flag", true);
                    bundle.putString("hostA",arpAttacker.getHostA());
                    bundle.putString("hostB",arpAttacker.getHostB());
                    Message message = new Message();
                    message.what = result_code;
                    message.setData(bundle);
                    mClient.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void start(Bundle configData) {
        String hostA = configData.getString("hostA");
        String hostB = configData.getString("hostB");
        String networkInterface = configData.getString("networkInterface");
        String toolPath = "/data/data/com.secbox.zhaoqc.secbox/files/arpspoof";
        String pkillPath = "/data/data/com.secbox.zhaoqc.secbox/files/pkill";
        if(arpAttacker == null)
            arpAttacker = new ArpAttacker(getApplicationContext());
        arpAttacker.setToolPath(toolPath);
        arpAttacker.setPkillPath(pkillPath);
        arpAttacker.setHostA(hostA);
        arpAttacker.setHostB(hostB);
        arpAttacker.setNetworkInterface(networkInterface);
        arpAttacker.start();
    }

    public void stop() {
        if(arpAttacker != null)
            arpAttacker.stop();
    }

    public void enableTransmit(boolean flag) {
        if(arpAttacker != null)
            arpAttacker.enableTransmit(flag);
    }

}