package com.secbox.zhaoqc.secbox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import utils.FileUtils;

public class WelcomeActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private TextView textView;

    private boolean isFirstIn = false;

    private String Permissions [] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private List<String> mPermissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        scrollView = (ScrollView) findViewById(R.id.scrollview);
        textView = (TextView) findViewById(R.id.boot_info);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("WelcomeActivity",0);
        isFirstIn = pref.getBoolean("isFirstIn",true);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isFirstIn",false);
        editor.commit();

        mPermissionList.clear();
        for(int i = 0;i < Permissions.length;i++ ) {
            Log.i("WelcomeActivity","检查权限:" + Permissions[i]);
            if(ContextCompat.checkSelfPermission(getApplicationContext(),Permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(Permissions[i]);
            } else {
                textView.setText(textView.getText().toString() + "检查权限: " + Permissions[i] + " [ok]\n");
            }
        }
        if(mPermissionList.isEmpty()) {
            textView.setText(textView.getText().toString() + "权限检测完毕\n");
            PrepareTask task = new PrepareTask();
            task.start();
        } else {
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(this,permissions,1);
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msgFromThread) {
            switch (msgFromThread.what) {
                case 1:
                    textView.setText(textView.getText() + msgFromThread.obj.toString());
                    break;
                case 2:
                    Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                default:
            }
            super.handleMessage(msgFromThread);
        }
    };

    private class PrepareTask extends Thread {

        @Override
        public void run() {
            Message message = new Message();

            message. what = 1;
            message.obj = "准备数据库中...";
            handler.sendMessage(message);

            Connector.getDatabase();

            message = new Message();
            message.what = 1;
            message.obj = "[ok]\n准备工具中...";
            handler.sendMessage(message);

            message = new Message();
            message.what = 1;
            if(FileUtils.copyAllFilesFromAssets(getApplicationContext(),"", "/data/data/com.secbox.zhaoqc.secbox/files"))
                message.obj = "[ok]\n";
            else
                message.obj = "[failed]\n";
            handler.sendMessage(message);

            message = new Message();
            message.what = 1;
            message.obj = "赋予执行权限...";
            handler.sendMessage(message);

            message = new Message();
            message.what = 1;
            if(FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/arpspoof")
                    && FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/httpd")
                    && FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/pkill")
                    && FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/tcpdump")
                    && FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/arpspoof"))
                message.obj = "[ok]\n";
            else
                message.obj = "[failed]\n";
            handler.sendMessage(message);

            message = new Message();
            message.what = 2;
            handler.sendMessage(message);

        }
    }

    private boolean mShowRequestPermission = true; //用户是否禁止权限

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[]grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(WelcomeActivity.this, permissions[i]);
                        if (showRequestPermission) {//
                            //judgePermission();//重新申请权限
                            ActivityCompat.requestPermissions(this,permissions,1);
                            return;
                        } else {
                            mShowRequestPermission = false;//已经禁止
                            textView.setText(textView.getText().toString() + "检查权限: " + permissions[i] + " [failed]\n");
                        }
                    } else {
                        textView.setText(textView.getText().toString() + "检查权限: " + permissions[i] + " [ok]\n");
                    }
                }
                //delayEntryPage();
                PrepareTask task = new PrepareTask();
                task.start();
                break;
            default:
                break;
        }

    }
}
