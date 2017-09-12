package module;

import android.content.Context;

import java.io.IOException;

/**
 * Created by zhaoqc on 17-8-30.
 */

public class SnifferModule {
    private Context context = null;
    private int targetIP = 0;
    private int sourceIP = 0;
    private String savePath = null;
    private String toolsPath = null;

    public SnifferModule(Context context) {
        this.context = context;
    }

    public void setTargetIP(int ipAddress) {
        this.targetIP = ipAddress;
    }

    public void setSourceIP(int ipAddress) {
        this.sourceIP = ipAddress;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void setToolsPath(String toolsPath) {
        this.toolsPath = toolsPath;
    }

    public void start() {

    }
}
