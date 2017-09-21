package module;

import android.content.Context;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by zhaoqc on 17-7-22.
 */

public class ArpAttacker {

    private Context mContext;

    public ArpAttacker(Context context) {
        this.mContext = context;
    }

    public void start() {

    }

    public void stop() {

    }

    //允许包转发
    public void enableTransmit(final boolean on) {
        new Thread() {
            @Override
            public void run() {
                if(on) {
                    try {
                        Process p = Runtime.getRuntime().exec("su");
                        DataOutputStream os = new DataOutputStream(p.getOutputStream());
                        os.writeBytes("echo 1 > /proc/sys/net/ipv4/ip_forward\n");
                        os.writeBytes("echo 1 > /proc/sys/net/ipv6/ip_forward\n");
                        os.writeBytes("exit\n");
                        os.flush();
                        p.waitFor();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Process p = Runtime.getRuntime().exec("su");
                        DataOutputStream os = new DataOutputStream(p.getOutputStream());
                        os.writeBytes("echo 0 > /proc/sys/net/ipv4/ip_forward\n");
                        os.writeBytes("echo 0 > /proc/sys/net/ipv6/ip_forward\n");
                        os.writeBytes("exit\n");
                        os.flush();
                        p.waitFor();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}
