package ui;


import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.secbox.zhaoqc.secbox.R;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import db.Port;
import service.ScanPortsService;


public class ScanPortsFragment extends BaseFragment {

    private ImageButton mScanButton;
    private ImageButton mConfigThreads;
    private ImageButton mEnableNotice;
    private ImageButton mAddHost;

    private RecyclerView recyclerView_of_ports;
    private ArrayList<Port> port_list = new ArrayList<>();

    private LinearLayout mProgressLayout;
    private ProgressBar mProgress;
    private TextView mScanInfo;

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

    public ScanPortsFragment() {
        // Required empty public constructor
    }

    public static ScanPortsFragment newInstance() {
        ScanPortsFragment fragment = new ScanPortsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(getContext(), ScanPortsService.class);
        getContext().bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navigationItemId = R.id.nav_scan_tools;
        position = 1;
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scan_ports, container, false);
        mScanButton = (ImageButton) rootView.findViewById(R.id.scan_button);
        mAddHost = (ImageButton) rootView.findViewById(R.id.add_host);
        mConfigThreads = (ImageButton) rootView.findViewById(R.id.config_threads);
        mEnableNotice = (ImageButton) rootView.findViewById(R.id.enable_notice);

        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });
        mAddHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mConfigThreads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mEnableNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        getContext().unbindService(conn);
        super.onDestroy();
    }

    @Override
    public String getTitle() {
        return "扫描端口";
    }

    @Override
    public void setData(Intent intent) {

    }

    private void scan() {

    }

    private void stop() {

    }

}
