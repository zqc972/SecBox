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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.secbox.zhaoqc.secbox.R;

import service.ArpCheatService;


public class ArpCheatFragment extends BaseFragment {

    private ToggleButton button;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener1;
    private CheckBox mTransmitter;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener2;
    private TextView hostA;
    private TextView hostB;
    private TextView state;

    private View loadingView;
    private View errorView;
    private View workView;

    private Messenger mService;
    private Messenger messenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msgFromService) {
            switch (msgFromService.what) {
                case 0:
                    //获取root权限成功，加载UI
                    loadingView.setVisibility(View.GONE);
                    errorView.setVisibility(View.GONE);
                    workView.setVisibility(View.VISIBLE);
                    if(msgFromService.getData().getBoolean("isRunning")) {
                        button.setOnCheckedChangeListener(null);
                        button.setChecked(true);
                        button.setOnCheckedChangeListener(onCheckedChangeListener1);
                        state.setText("Running");
                    } else {
                        state.setText("stopped");
                    }
                    if(msgFromService.getData().getBoolean("transmit_flag")) {
                        mTransmitter.setOnCheckedChangeListener(null);
                        mTransmitter.setChecked(true);
                        mTransmitter.setOnCheckedChangeListener(onCheckedChangeListener2);
                    }
                    hostA.setText(msgFromService.getData().getString("hostA"));
                    hostB.setText(msgFromService.getData().getString("hostB"));
                    break;
                case 1:
                    //获取root失败，切换提示信息
                    errorView.setVisibility(View.VISIBLE);
                    loadingView.setVisibility(View.GONE);
                    workView.setVisibility(View.GONE);
                    break;
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

        View rootView = inflater.inflate(R.layout.fragment_arp_cheat, container, false);
        loadingView = rootView.findViewById(R.id.loading_view);
        errorView = rootView.findViewById(R.id.error_view);
        workView = rootView.findViewById(R.id.work_view);

        TextView info_of_loading_view = (TextView) loadingView.findViewById(R.id.info);
        info_of_loading_view.setText("申请ROOT权限中...");
        TextView info_of_error_view = (TextView) errorView.findViewById(R.id.info);
        info_of_error_view.setText("申请ROOT权限失败");

        button = (ToggleButton)workView.findViewById(R.id.start);
        mTransmitter = (CheckBox) workView.findViewById(R.id.transmitter);
        hostA = (TextView) workView.findViewById(R.id.hostA);
        hostB = (TextView) workView.findViewById(R.id.hostB);
        state = (TextView) workView.findViewById(R.id.state);

        onCheckedChangeListener1 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    start();
                else
                    stop();
            }
        };
        onCheckedChangeListener2 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    Message message = new Message();
                    if(isChecked) {
                        message.what = 3;
                    } else {
                        message.what = 4;
                    }
                    mService.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };

        button.setOnCheckedChangeListener(onCheckedChangeListener1);
        mTransmitter.setOnCheckedChangeListener(onCheckedChangeListener2);

        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        workView.setVisibility(View.GONE);

        return rootView;
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

    private void start() {
        //类似于加锁的功能，不让Service自动退出
        Intent intent = new Intent(getContext(),ArpCheatService.class);
        getContext().startService(intent);

        try {
            Bundle bundle = new Bundle();
            bundle.putString("hostA",hostA.getText().toString());
            bundle.putString("hostB",hostB.getText().toString());
            bundle.putString("networkInterface","eth0"); //
            Message message = new Message();
            message.what = 1;
            message.setData(bundle);
            mService.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        //类似于去锁的功能，让Service自动退出
        Intent intent = new Intent(getContext(),ArpCheatService.class);
        getContext().stopService(intent);

        try {
            Message message = new Message();
            message.what = 2;
            mService.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
