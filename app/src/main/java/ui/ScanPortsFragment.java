package ui;


import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.DataSetObserver;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.secbox.zhaoqc.secbox.R;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.litepal.crud.DataSupport;

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

    private LinearLayout mProgressLayout;
    private ProgressBar mProgress;
    private TextView mScanInfo;

    private ExpandableListView expandableListView;
    private BaseExpandableListAdapter adapter;
    private List<String> hostsList = new ArrayList<>();
    private List<List<Integer>> portsList = new ArrayList<>();

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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
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

        //
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.list);
        adapter = new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return hostsList.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return portsList.get(groupPosition).size();
            }

            @Override
            public Object getGroup(int groupPosition) {
                return groupPosition;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return portsList.get(groupPosition).get(childPosition);
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                convertView = View.inflate(getContext(),R.layout.item_of_scan_ports_a,null);
                TextView host = (TextView) convertView.findViewById(R.id.host);
                TextView state = (TextView) convertView.findViewById(R.id.state);
                host.setText(hostsList.get(groupPosition));
                if(portsList.get(groupPosition).size() != 0)
                    state.setText("已扫描到的端口:" + portsList.get(groupPosition).size() + "个");
                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                convertView = View.inflate(getContext(),R.layout.item_of_scan_ports_b,null);
                TextView port = (TextView) convertView.findViewById(R.id.port);
                TextView describe = (TextView) convertView.findViewById(R.id.describe);
                port.setText(String.valueOf(portsList.get(groupPosition).get(childPosition)));
                Port mPort = DataSupport.where("port = ?",String.valueOf(portsList.get(groupPosition).get(childPosition))).findFirst(Port.class);
                if(mPort != null)
                    describe.setText(mPort.getDescription());
                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }
        };
        expandableListView.setAdapter(adapter);
        expandableListView.setGroupIndicator(null);

        addHost("test");
        addPort("test1",80);

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
        //接收来自其他模块的传入数据

        //目前暂时仅为测试数据
    }

    private void scan() {

    }

    private void stop() {

    }


    private void addHost(String host) {
        hostsList.add(host);
        List<Integer> childItems = new ArrayList<>();
        portsList.add(childItems);
        adapter.notifyDataSetChanged();
    }

    private void addPort(String host,int port) {
        int position = -1;
        for(int i = 0;i < hostsList.size();i++)
            if(hostsList.get(i).equals(host))
                position = i;
        if(position != -1) {
            portsList.get(position).add(port);
        } else {
            addHost(host);
            portsList.get(portsList.size()-1).add(port);
        }
        adapter.notifyDataSetChanged();
    }

}
