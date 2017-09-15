package module;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaoqc on 17-7-22.
 */

public class PortScanner {

    private ArrayList<String> hostList = new ArrayList<String>();
    private ArrayList<Integer> portList = new ArrayList<>();

    private ScheduledExecutorService mThreadPool = null;

    public void start(boolean keep_scan) {
        if(mThreadPool == null) {   //如果没有设置线程数量的情况下，默认30个线程
            mThreadPool = Executors.newScheduledThreadPool(30);
        }
        for(int i = 0;i < hostList.size();i++) {
            for(int j = 0;j < portList.size();j++ ) {
                ScanThread thread = new ScanThread();
                thread.host = hostList.get(i);
                thread.port = portList.get(j);
                if(keep_scan)
                    mThreadPool.scheduleAtFixedRate(thread,3,3, TimeUnit.SECONDS);
                else
                    mThreadPool.schedule(thread,0,TimeUnit.SECONDS);
            }
        }
    }

    public void stop() {
        mThreadPool.shutdown();
    }

    public boolean isRunning() {
        return mThreadPool.isShutdown();
    }

    public void setThreads(int threads_num) {
        mThreadPool = Executors.newScheduledThreadPool(threads_num);
    }

    public void addHost(String host) {
        for(int i = 0; i < hostList.size();i++) {
            if(hostList.get(i).equals(host))
                return;
        }
        this.hostList.add(host);
    }

    public void setHostList(ArrayList<String> list) {
        if(list != null)
            this.hostList = list;
    }

    public void removeHost(String host) {
        for(int i = 0; i < hostList.size(); i++) {
            if(hostList.get(i).equals(host)) {
                hostList.remove(i);
                break;
            }
        }
    }

    public void addPort(int port){
        for(int i = 0;i < portList.size();i++) {
            if(portList.get(i) == port)
                return;
        }
        portList.add(port);
    }

    public void setPortList(ArrayList<Integer> list) {
        if(list != null)
            this.portList = list;
    }

    public void removePort(int port){
        for(int i = 0;i < portList.size(); i ++) {
            if(portList.get(i) == port) {
                portList.remove(i);
                break;
            }
        }
    }

    private class ScanThread extends Thread{
        int port;
        String host;

        @Override
        public void run() {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host,port),3000);
                Log.i("ScanThread","scanned success: " + host + ":" + port);
                if(onScannedListener != null)
                    onScannedListener.onScanned(host,port);
            } catch (IOException e) {
                //e.printStackTrace();
                Log.i("ScanThread","scanned failed:" + host + ":" + port);
            }
        }
    }

    public interface OnProgressListener{
        void onProgressChanged(int progress,int current_ip,int port);
    };
    public interface OnScannedListener{
        void onScanned(String host,int port);
    }

    private OnProgressListener onProgressListener = null;
    private OnScannedListener onScannedListener = null;

    public void setOnProgressListener(OnProgressListener listener) {
        this.onProgressListener = listener;
    }

    public void setOnScannedListener(OnScannedListener listener) {
        this.onScannedListener = listener;
    }
}

