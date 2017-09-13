package ui;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.secbox.zhaoqc.secbox.R;

import service.ArpCheatService;


public class ArpCheatFragment extends BaseFragment {

    private Messenger mService;
    private Messenger messenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msgFromService) {
            switch (msgFromService.what) {
                default:
                    Toast.makeText(getContext(),"received a message from service ",Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msgFromService);
        }
    });
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            //向Service获取当前状态
            try {
                Message message = new Message();
                message.replyTo = messenger;
                message.what = 0;
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    public ArpCheatFragment() {
        // Required empty public constructor
    }

    public static ArpCheatFragment newInstance() {
        ArpCheatFragment fragment = new ArpCheatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = new Intent(getContext(), ArpCheatService.class);
        getContext().bindService(intent,conn, Context.BIND_AUTO_CREATE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navigationItemId = R.id.nav_cheat_attack;
        position = 0;

        return inflater.inflate(R.layout.fragment_arp_cheat, container, false);
    }

    @Override
    public void onDestroy() {
        getContext().unbindService(conn);
        super.onDestroy();
    }

    @Override
    public String getTitle() {
        return "Arp攻击";
    }
}
