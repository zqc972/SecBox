package module;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.NetworkUtils;

/**
 * Created by zhaoqc on 17-9-10.
 */

public class HostScanner2 {

    private Context context = null;

    private int ipAddress;
    private int netmask;

    private static int current_ip;
    private static int hosts_size;
    private static int scanned_count;

    private ExecutorService cachedThreadPool = null;

    public HostScanner2(Context context,int ipAddress,int netmask) {
        this.context = context;
        this.ipAddress = ipAddress;
        this.netmask = netmask;
    }

    public void start() {
        current_ip = NetworkUtils.getFirstIp(ipAddress,netmask);
        hosts_size = NetworkUtils.countHost(netmask);
        scanned_count = 0;

        cachedThreadPool = Executors.newFixedThreadPool(50);
        for (int i = 0;i < hosts_size;i++ ){
            ScanThread thread = new ScanThread();
            thread.setTargetIP(getNextIP());
            cachedThreadPool.execute(thread);
        }
    }

    public void stop() {
        if(cachedThreadPool != null)
            cachedThreadPool.shutdown();
    }

    //扫描监听回调
    public interface OnProgressListener {
        void onProgressChanged(int progress,int current_ip);
    }
    public interface OnScannedListener{
        void onScanned(int ip);
    }

    private OnProgressListener onProgressListener;
    private OnScannedListener onScannedListener;

    public void setOnProgressListener(OnProgressListener listener) {
        this.onProgressListener = listener;
    }

    public void setOnScannedListener(OnScannedListener listener) {
        this.onScannedListener = listener;
    }

    //扫描线程
    private class ScanThread implements Runnable{
        private int target = 0;

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                Process p = Runtime.getRuntime().exec("ping -c 1 -w 3 " + NetworkUtils.IpToString(target));
                if(p.waitFor() == 0) {
                    Log.i("ScanThread","found host " + NetworkUtils.IpToString(target));
                } else {
                    Log.i("ScanThread","not found host " + NetworkUtils.IpToString(target));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void setTargetIP(int ip) {
            this.target = ip;
        }
    }

    protected int getNextIP() {
        current_ip = NetworkUtils.getNextIp(current_ip);
        return current_ip;
    }
}
