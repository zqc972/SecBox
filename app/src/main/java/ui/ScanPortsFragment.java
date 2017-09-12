package ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.secbox.zhaoqc.secbox.R;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import db.Port;


public class ScanPortsFragment extends BaseFragment {

    private ImageButton mExpandButton;
    private ImageButton mScanButton;
    private RecyclerView recyclerView_of_ports;
    private ArrayList<Port> port_list = new ArrayList<>();
    private PortListAdapter adapter_of_port_list;
    private TextView mSetupInfo;

    private LinearLayout mProgressLayout;
    private ProgressBar mProgress;
    private TextView mScanInfo;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navigationItemId = R.id.nav_scan_tools;
        position = 1;
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scan_ports, container, false);
        mExpandButton = (ImageButton) rootView.findViewById(R.id.expand_button);
        mScanButton = (ImageButton) rootView.findViewById(R.id.scan_button);
        mExpandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });

        adapter_of_port_list = new PortListAdapter();
        adapter_of_host_list = new HostListAdapter();

        recyclerView_of_ports = (RecyclerView)rootView.findViewById(R.id.list);
        recyclerView_of_ports.setAdapter(adapter_of_port_list);
        recyclerView_of_ports.setLayoutManager(new LinearLayoutManager(getContext()));

        mSetupInfo = (TextView) rootView.findViewById(R.id.setup_info);

        //临时测试
        Port port = new Port();
        port.setDescription("test");
        port.setPort(80);
        port_list.add(port);
        adapter_of_port_list.notifyDataSetChanged();

        //临时测试
        addHost("10.0.0.1");

        return rootView;
    }

    @Override
    public String getTitle() {
        return "扫描端口";
    }

    @Override
    public void setData(Intent intent) {

    }

    private class PortListAdapter extends RecyclerView.Adapter<PortListAdapter.ViewHolder> {

        @Override
        public PortListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_of_scan_ports_a,parent,false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final PortListAdapter.ViewHolder holder, final int position) {
            holder.mPort.setText(String.valueOf(port_list.get(position).getPort()));
            holder.mDescribe.setText(port_list.get(position).getDescription());
        }

        @Override
        public int getItemCount() {
            return port_list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView mPort;
            private TextView mDescribe;

            public ViewHolder(View itemView) {
                super(itemView);
                mPort = (TextView) itemView.findViewById(R.id.port);
                mDescribe = (TextView) itemView.findViewById(R.id.describe);
            }
        }
    }


    private void scan() {
        //

    }


    private Button mAdd = null;
    private Button mOk = null;
    private RecyclerView recyclerView_of_hosts = null;

    private void showPopupWindow() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.popupwindow_scan_ports,null);

        mAdd = (Button) view.findViewById(R.id.add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHostDialog();
            }
        });
        mOk = (Button) view.findViewById(R.id.ok);
        recyclerView_of_hosts = (RecyclerView)view.findViewById(R.id.list);
        recyclerView_of_hosts.setAdapter(adapter_of_host_list);
        recyclerView_of_hosts.setLayoutManager(new LinearLayoutManager(getContext()));

        PopupWindow popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,false);
        // 需要设置一下此参数，点击外边可消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        popupWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(getView().findViewById(R.id.config_layout));
    }

    private void addHost(String target) {
        for(int i = 0;i < host_list.size();i++) {
            if(host_list.get(i).equals(target))
                return;
        }
        host_list.add(target);
        if(adapter_of_host_list == null)
            adapter_of_host_list = new HostListAdapter();
        adapter_of_host_list.notifyDataSetChanged();
        if(host_list.size() > 0)
            mSetupInfo.setText("共" + host_list.size() + "台主机");
        else
            mSetupInfo.setText("未设定");
    }

    private void removeHost(String target) {
        for(int i = 0;i < host_list.size();i++) {
            if(host_list.get(i).equals(target)) {
                host_list.remove(i);
                if(adapter_of_host_list != null)
                    adapter_of_host_list.notifyDataSetChanged();
                if(host_list.size() > 0)
                    mSetupInfo.setText("共" + host_list.size() + "台主机");
                else
                    mSetupInfo.setText("未设定");
                break;
            }
        }
    }


    private void addHostDialog() {
        final EditText editText = new EditText(getContext());
        new AlertDialog.Builder(getActivity())
                .setTitle("please input ip or url")
                .setView(editText)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String target = editText.getText().toString();
                        addHost(target);
                    }
                })
                .setNegativeButton("cancel",null)
                .setCancelable(false)
                .show();
    }

    private void delHost(int position) {
        host_list.remove(position);
        if(adapter_of_host_list != null)
            adapter_of_host_list.notifyDataSetChanged();
        if(host_list.size() > 0)
            mSetupInfo.setText("共" + host_list.size() + "台主机");
        else
            mSetupInfo.setText("未设定");
    }

    private ArrayList<String> host_list = new ArrayList<>();
    private HostListAdapter adapter_of_host_list;
    private class HostListAdapter extends RecyclerView.Adapter<HostListAdapter.ViewHolder> {

        @Override
        public HostListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_of_scan_ports_b,parent,false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final HostListAdapter.ViewHolder holder, final int position) {
            holder.mIp.setText(host_list.get(position));
            holder.mDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delHost(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return host_list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView mIp;
            private Button mDel;

            public ViewHolder(View itemView) {
                super(itemView);
                mIp = (TextView) itemView.findViewById(R.id.ip);
                mDel = (Button) itemView.findViewById(R.id.del);
            }
        }
    }
}
