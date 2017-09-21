package module;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

/**
 * Created by zhaoqc on 17-8-30.
 */

public class WebServer {
    private Context mContext;

    private String rootDirPath = null;
    private int port = 80;

    public WebServer(Context context) {
        this.mContext = context;
    }

    public void setRootDirPath(String rootDirPath) {
        this.rootDirPath = rootDirPath;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void start() {
        if(rootDirPath != null) {

        }
    }

    public void stop() {

    }
}
