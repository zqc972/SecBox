package module;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhaoqc on 17-8-30.
 */

public class WebCrawler {
    private String targetURL = null;
    private OnProgressListener onProgressListener = null;
    private OnFinishedListener onFinishedListener = null;

    public static final int SHOW_RESPONSE=1;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String content = (String) msg.obj;
                    onFinishedListener.onFinished(content);
                    break;
                default:
                    break;
            }
        }
    };

    private void sendRequest(final String targetURL) {
        /*需要新建子线程进行访问*/
        new Thread(){
            public void run(){
                HttpURLConnection httpURLConnection=null;
                try {
                    URL url=new URL(targetURL);
                    httpURLConnection=(HttpURLConnection) url.openConnection();//获取到httpURLConnection的实例
                    httpURLConnection.setRequestMethod("GET");//设置HTTP请求所使用的方法，GET表示希望从服务器那里获取数据，而POST则表示希望提交数据给服务器
                    httpURLConnection.setReadTimeout(6000);//设置读取超时的毫秒数
                    InputStream is=httpURLConnection.getInputStream();//获取到服务器返回的输入流
                    BufferedReader br=new BufferedReader(new InputStreamReader(is));//对获取到的输入流进行读取
                    String s;
                    StringBuilder sb=new StringBuilder();
                    while((s=br.readLine())!=null){
                        sb.append(s);
                    }
                    Message msg=new Message();
                    msg.what=SHOW_RESPONSE;//封装子线程编号
                    msg.obj=sb.toString();//封装获取到的内容
                    handler.sendMessage(msg);//发送信息
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    public void setTargetURL(String targetURL) {
        this.targetURL = targetURL;
    }

    public void start() {
        if(this.targetURL != null)
            sendRequest(this.targetURL);
    }

    public boolean saveAsFile(String filePath) {
        return false;
    }

    public interface OnProgressListener {
        void onProgressChanged(int progress);
    }
    public interface OnFinishedListener {
        void onFinished(String content);
    }

    public void setOnScannedListener(OnProgressListener listener) {
        this.onProgressListener = listener;
    }
    public void setOnFinishedListener(OnFinishedListener listener) {
        this.onFinishedListener = listener;
    }
}
