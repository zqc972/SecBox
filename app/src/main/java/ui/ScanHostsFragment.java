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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.secbox.zhaoqc.secbox.R;

import java.util.ArrayList;
import java.util.List;

import service.ScanHostsService;
import utils.NetworkUtils;


public class ScanHostsFragment extends BaseFragment {

    private ArrayList<HostListItem> items = new ArrayList<HostListItem>();
    private HostsListAdapter adapter;

    private TextView mState = null;
    private ProgressBar mProgress = null;
    private LinearLayout mProgressLayout = null;
    private Button mStart = null;

    private Messenger mService;
    private Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msgFromService) {
            switch (msgFromService.what) {
                case 0:
                    refreshProgressUI(msgFromService.getData());
                    Log.i("ScanHostsService","received ok");
                    break;
                case 1:
                    refreshListUI(msgFromService.getData());
                    break;
                default:
            }
            super.handleMessage(msgFromService);
        }
    });
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    public ScanHostsFragment() {
        // Required empty public constructor
    }

    public static ScanHostsFragment newInstance() {
        ScanHostsFragment fragment = new ScanHostsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(getContext(), ScanHostsService.class);
        getContext().bindService(intent,conn, Context.BIND_AUTO_CREATE);

        Log.i("ScanHostsFragment","get ip :" + NetworkUtils.getIp(getContext()));
        Log.i("ScanHostsFragment","ip to String :" + NetworkUtils.IpToString(NetworkUtils.getIp(getContext())));
        Log.i("ScanHostsFragment","dns1 :" + NetworkUtils.getDns1(getContext()));
        Log.i("ScanHostsFragment","dns1 to string :" + NetworkUtils.IpToString(NetworkUtils.getDns1(getContext())));
        Log.i("ScanHostsFragment","dns2 :" + NetworkUtils.getDns2(getContext()));
        Log.i("ScanHostsFragment","dns2 to string :" + NetworkUtils.IpToString(NetworkUtils.getDns2(getContext())));
        Log.i("ScanHostsFragment","gateway :" + NetworkUtils.getGateway(getContext()));
        Log.i("ScanHostsFragment","gateway to string :" + NetworkUtils.IpToString(NetworkUtils.getGateway(getContext())));
        Log.i("ScanHostsFragment","netmask :" + NetworkUtils.getNetmask(getContext()));
        Log.i("ScanHostsFragment","netmask to string :" + NetworkUtils.IpToString(NetworkUtils.getNetmask(getContext())));
        Log.i("ScanHostsFragment","mac :" + NetworkUtils.getMac(getContext()));
        Log.i("ScanHostsFragment","this network has " + NetworkUtils.countHost(NetworkUtils.getNetmask(getContext())) + " hosts");
        Log.i("ScanHostsFragment","the first host of this network is :" + NetworkUtils.IpToString(NetworkUtils.getFirstIp(
                NetworkUtils.getIp(getContext()),
                NetworkUtils.getNetmask(getContext()))
        ));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unbindService(conn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navigationItemId = R.id.nav_scan_tools;
        position = 0;
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scan_hosts, container, false);
        mProgressLayout = (LinearLayout) rootView.findViewById(R.id.progress_layout);
        mProgressLayout.setVisibility(View.GONE);
        mProgress = (ProgressBar) rootView.findViewById(R.id.progress);
        mState = (TextView) rootView.findViewById(R.id.state);
        mStart = (Button) rootView.findViewById(R.id.scan);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Message message = new Message();
                    message.what = 1;
                    message.replyTo = mMessenger;
                    mService.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.hosts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HostsListAdapter();
        recyclerView.setAdapter(adapter);
        loadData();
        return rootView;
    }

    @Override
    public String getTitle() {
        return "扫描主机";
    }

    public void loadData() {
        //本机
        HostListItem item = new HostListItem();
        item.setHost_ip(NetworkUtils.getIp(getContext()));
        item.setHost_mac(NetworkUtils.getMac(getContext()));
        String mac = NetworkUtils.getMac(getContext());
        if(mac != null)
            item.setHost_manufacturer(NetworkUtils.getManufacturerByMac(mac));
        else
            item.setHost_manufacturer("unkown");
        item.setHost_name("本机");
        items.add(item);
        adapter.notifyDataSetChanged();
    }

    private void refreshProgressUI(Bundle bundle) {
        int progress = bundle.getInt("progress");
        int ip = bundle.getInt("ip");
        if(mProgressLayout != null ) {
            if(progress<100)
                mProgressLayout.setVisibility(View.VISIBLE);
            else
                mProgressLayout.setVisibility(View.GONE);
        }
        if(mProgress != null && mState != null) {
            mProgress.setProgress(progress);
            mState.setText("Scanning :" + NetworkUtils.IpToString(ip));
        }

    }

    private void refreshListUI(Bundle bundle) {
        HostListItem item = new HostListItem();
        item.setHost_ip(bundle.getInt("ip"));
        item.setHost_mac(bundle.getString("mac"));
        item.setHost_manufacturer(bundle.getString("manufacturer"));
        items.add(item);
        adapter.notifyDataSetChanged();
    }

    private class HostsListAdapter extends RecyclerView.Adapter<HostsListAdapter.ViewHolder> {

        @Override
        public HostsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_of_scan_hosts,parent,false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.host_name.setText(items.get(position).getHost_name());
            holder.host_ip.setText(NetworkUtils.IpToString(items.get(position).getHost_ip()));
            holder.host_mac.setText(items.get(position).getHost_mac());
            holder.host_manufacturer.setText(items.get(position).getHost_manufacturer());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView host_name;
            private TextView host_ip;
            private TextView host_mac;
            private TextView host_manufacturer;

            public ViewHolder(View itemView) {
                super(itemView);
                host_ip = (TextView) itemView.findViewById(R.id.device_ip);
                host_mac = (TextView) itemView.findViewById(R.id.device_mac);
                host_manufacturer = (TextView) itemView.findViewById(R.id.device_manufacturer);
                host_name = (TextView) itemView.findViewById(R.id.device_name);
            }
        }
    }

    private class HostListItem{
        private String host_name = "未知主机";
        private int host_ip = 0;
        private String host_mac = "未知mac地址";
        private String host_manufacturer = "未知制造厂商";
        public void setHost_name(String host_name) {
            this.host_name = host_name;
        }
        public void setHost_ip(int host_ip) {
            this.host_ip = host_ip;
        }
        public void setHost_mac(String host_mac) {
            this.host_mac = host_mac;
        }
        public void setHost_manufacturer(String host_manufacturer) {
            this.host_manufacturer = host_manufacturer;
        }
        public String getHost_name() {
            return this.host_name;
        }
        public int getHost_ip() {
            return this.host_ip;
        }
        public String getHost_mac() {
            return this.host_mac;
        }
        public String getHost_manufacturer() {
            return this.host_manufacturer;
        }
    }
}
