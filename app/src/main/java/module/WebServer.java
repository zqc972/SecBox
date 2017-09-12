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
    private String toolsPath = null;
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

    public void setToolsPath(String toolsPath) {
        this.toolsPath = toolsPath;
    }

    public void start() {
        if(rootDirPath != null && toolsPath != null) {
            try {
                Process p = null;
                if(port < 1024) {
                    //check root permission()
                    p = Runtime.getRuntime().exec("su -c " + toolsPath + " -p 8080 -h " + rootDirPath );
                } else {
                    p = Runtime.getRuntime().exec(toolsPath + " -p 8080 -h " + rootDirPath );//new String[] {toolsPath ,"-p",String.valueOf(port),"-h",rootDirPath});
                }
                Log.i("WebServer",toolsPath + " -p 8080 -h " + rootDirPath);
                if(p.waitFor() == 0) {
                    Log.i("WebServer","result = 0");
                } else {
                    Log.i("WebServer","result = 1");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {

    }
}
