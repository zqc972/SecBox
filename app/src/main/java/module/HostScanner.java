package module;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import utils.NetworkUtils;

/**
 * Created by zhaoqc on 17-7-22.
 */

public class HostScanner {

    private Context mContext = null;
    private int thread_num = 50;
    private ArrayList<ScanThread> threads_queue = new ArrayList<ScanThread>();

    private int ipAddress;
    private int netmask;

    private static int current_ip;
    private static int hosts_size;
    private static int scanned_count;

    public HostScanner(Context context,int ipAddress,int netmask) {
        this.mContext = context;
        this.ipAddress = ipAddress;
        this.netmask = netmask;
    }

    public void setThread_num(int thread_num) {
        this.thread_num = thread_num;
    }

    public void scan() {
        current_ip = NetworkUtils.getFirstIp(ipAddress,netmask);
        hosts_size = NetworkUtils.countHost(netmask);
        scanned_count = 0;

        if(threads_queue != null) {
            for (int i = 0; i < threads_queue.size();i++)
                threads_queue.get(i).interrupt();
            threads_queue.clear();
        }

        for (int i = 1; i <= thread_num; i++) {
            ScanThread thread = new ScanThread(i);
            thread.start();
            threads_queue.add(thread);
        }
        Log.i("HostScanner"," ============= hosts_size:" + hosts_size);
    }

    public void stop() {
        for (int i = 0; i < threads_queue.size() ; i++ ) {
            threads_queue.get(i).interrupt();
        }
    }

    private class ScanThread extends Thread implements Runnable{
        public int thread_id = 0;

        public ScanThread(int id) {
            thread_id = id;
        }

        @Override
        public void run() {
            Log.i("HostScanner","Thread id: " + thread_id + " start");
            while (scanned_count < hosts_size) {
//                if(NetworkUtils.isNetworkAvailable(mContext) == false)
//                    break;
                scanned_count ++;
                int target = current_ip;
                current_ip = NetworkUtils.getNextIp(current_ip);
                Log.i("ScanThread","Thread id:" + thread_id + " is ping " + NetworkUtils.IpToString(target) + " Now");

                try {
                    Process p = Runtime.getRuntime().exec("ping -c 1 -w 3 " + NetworkUtils.IpToString(target));
                    if(p.waitFor() == 0) {
                        Log.i("ScanThread","found host " + NetworkUtils.IpToString(target));
                        onScannedListener.onScanned(target);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                onProgressListener.onProgressChanged(
                        (int) ((float)scanned_count/hosts_size*100),
                        current_ip);
            }
            Log.i("HostScanner","Thread id: " + thread_id + " run over");
        }
    }

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

}
