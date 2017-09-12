package module;

import android.content.Context;
import android.util.Log;

/**
 * Created by zhaoqc on 17-7-22.
 */

public class PortScanner {

    private Context mContext;

    public PortScanner(Context context) {
        this.mContext = context;
    }

    public void scan() {
        ScanThread thread = new ScanThread();
        thread.start();
    }

    public void stop() {

    }

    private class ScanThread extends Thread implements Runnable{

        @Override
        public void run() {
            Log.i("PortScanner","start scan port:");
        }
    }
}
