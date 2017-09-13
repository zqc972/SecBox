package ui;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.secbox.zhaoqc.secbox.R;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.website.StorageWebsite;
import com.yanzhenjie.andserver.website.WebSite;

import java.io.IOException;
import java.util.Map;

import module.WebServer;
import service.WebServerService;

public class WebServerFragment extends BaseFragment {

    private ToggleButton mStart = null;
    private TextView mState = null;
    private EditText mPort = null;
    private TextView mRootDir = null;

    private boolean isRunning = false;

    private WebSite webSite = null;
    private Server.Listener listener = null;
    private Server mServer = null;

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

    public WebServerFragment() {
        // Required empty public constructor
    }

    public static WebServerFragment newInstance() {
        WebServerFragment fragment = new WebServerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = new Intent(getContext(), WebServerService.class);
        getContext().bindService(intent,conn,Context.BIND_AUTO_CREATE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_web_server, container, false);
        mStart = (ToggleButton)rootView.findViewById(R.id.start);
        mState = (TextView)rootView.findViewById(R.id.state);
        mPort = (EditText) rootView.findViewById(R.id.server_port);
        mRootDir = (TextView)rootView.findViewById(R.id.root_dir);

        listener = new Server.Listener() {
            @Override
            public void onStarted() {
                mState.setText("Started");
            }

            @Override
            public void onStopped() {
                mState.setText("Stopped");
            }

            @Override
            public void onError(Exception e) {
                mState.setText("Error");
            }
        };

        mRootDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choose web root dir path
            }
        });

        mStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    WebServer webServer = new WebServer(getContext());
                    webServer.setToolsPath("/data/data/com.secbox.zhaoqc.secbox/files/httpd");
                    webServer.setRootDirPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/www");
                    webServer.setPort(80);
                    webServer.start();

                    final int port = Integer.parseInt(mPort.getText().toString());

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(port < 1024) {
                                try {
                                    Process p = Runtime.getRuntime().exec("su");
                                    if(p.waitFor() == 0) {
                                        webSite = new StorageWebsite(mRootDir.getText().toString());
                                        AndServer andServer = new AndServer.Build()
                                                .port(port)
                                                .timeout(10 * 1000)
                                                .listener(listener)
                                                .website(webSite)
                                                .build();
                                        mServer = andServer.createServer();
                                        mServer.start();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                webSite = new StorageWebsite(mRootDir.getText().toString());
                                AndServer andServer = new AndServer.Build()
                                        .port(port)
                                        .timeout(10 * 1000)
                                        .listener(listener)
                                        .website(webSite)
                                        .build();
                                mServer = andServer.createServer();
                                mServer.start();
                            }
                            Log.i("WebServerFragment","WebServer is starting ");
                        }
                    });
                    thread.start();

                } else {
                    if(mServer != null) {
                        mServer.stop();
                    }
                }
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
        return "Web服务器";
    }

}
