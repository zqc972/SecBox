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


//    WelcomeActivity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        scrollView = (ScrollView) findViewById(R.id.scrollview);
        textView = (TextView) findViewById(R.id.boot_info);

        //PrepareTask task = new PrepareTask();
        //task.execute();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("WelcomeActivity",0);
        isFirstIn = pref.getBoolean("isFirstIn",true);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isFirstIn",false);
        editor.commit();

        mPermissionList.clear();
        for(int i = 0;i < Permissions.length;i++ ) {
            Log.i("WelcomeActivity","Check permission:" + Permissions[i]);
            if(ContextCompat.checkSelfPermission(getApplicationContext(),Permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(Permissions[i]);
            } else {
                textView.setText(textView.getText().toString() + "Check permission: " + Permissions[i] + " [ok]\n");
            }
        }
        if(mPermissionList.isEmpty()) {
            textView.setText(textView.getText().toString() + "All permission granted\n");
            startActivity();
        } else {
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(this,permissions,1);
        }

//        activity = this;
    }



    private void startActivity() {
        textView.setText(textView.getText().toString() + "Preparing database...");
//        if(!FileUtils.isFileExist(getApplicationContext(),"/data/data/com.secbox.zhaoqc.secbox/databases/info.db"))
            //FileUtils.copyFileFromAssets(getApplicationContext(),"info.db","/data/data/com.secbox.zhaoqc.secbox/databases/info.db");
        Connector.getDatabase();
        textView.setText(textView.getText().toString() + "[ok]\n");
        textView.setText(textView.getText().toString() + "Preparing Tools...");

        if(FileUtils.copyAllFilesFromAssets(getApplicationContext(),"", "/data/data/com.secbox.zhaoqc.secbox/files"))
            textView.setText(textView.getText().toString() + "[ok]\n");
        else
            textView.setText(textView.getText().toString() + "[failed]\n");

        textView.setText(textView.getText().toString() + "Make tools executable...");
        if(FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/arpspoof")
                && FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/httpd")
                && FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/pkill")
                && FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/tcpdump")
                && FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/arpspoof"))
                textView.setText(textView.getText().toString() + "[ok]");
            else
                textView.setText(textView.getText().toString() + "[failed]");

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        //finish();
    }

//    private class PrepareTask extends AsyncTask<Void,Integer,Void> {
//        private String info = "";
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            info = info + "SecBox is preparing...\n";
//            publishProgress(1);
//
//            info = info + "Prepare DataBase...";
//            //Connector.getDatabase();
//            //if(!FileUtils.isFileExist(getApplicationContext(),"/data/data/com.secbox.zhaoqc.secbox/databases/info.db"))
//            FileUtils.copyFileFromAssets(getApplicationContext(),"info.db","/data/data/com.secbox.zhaoqc.secbox/databases/info.db");
//            info = info + " [ok]\n";
//            publishProgress(10);
//
//            //chech permission
//            info = info + "Check permission...";
//            for (int i = 0; i < Permissions.length ; i++ ) {
//                if(ContextCompat.checkSelfPermission(getApplicationContext(), Permissions[i]) != PackageManager.PERMISSION_GRANTED)
//                {
//                    ActivityCompat.requestPermissions(activity, new String[]{Permissions[i]},1);
//                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Permissions[i]) != PackageManager.PERMISSION_GRANTED)
//                        info = info + "";
//                }
//            }
//            info = info + " [ok]\n";
//            publishProgress(20);
//
//            //Check permission
//            //if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//            for (int i = 0; i < Permissions.length ; i++ ) {
//                info = info + ("Check permission " + Permissions[i]);
//                if(ContextCompat.checkSelfPermission(getApplicationContext(), Permissions[i]) != PackageManager.PERMISSION_GRANTED)
//                {
//                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity,Permissions[i])) {
//                        shouldShowRequestPermissionRationale(Permissions[i]);
//                    } else {
//                        ActivityCompat.requestPermissions(activity, new String[]{Permissions[i]},1);
//                    }
//
//                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Permissions[i]) != PackageManager.PERMISSION_GRANTED)
//                        info = info + " [failed]\n";
//                } else {
//                    info = info + " [ok]\n";
//                }
//            }
//            publishProgress(30);
//
//            //extract assets
//            info = info + "Extract assets files";
//            if(FileUtils.copyAllFilesFromAssets(getApplicationContext(),"", getFilesDir().getAbsolutePath()))
//                info = info + " [ok]\n";
//            else
//                info = info + " [failed]\n";
//            publishProgress(40);
//
//            //make tools executable
//            info = info + "Make tools executable";
//            if(FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/arpspoof")
//                    && FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/httpd")
//                    && FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/pkill")
//                    && FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/tcpdump")
//                    && FileUtils.makeFileExecutable("/data/data/com.secbox.zhaoqc.secbox/files/arpspoof"))
//                info = info + " [ok]\n";
//            else
//                info = info + " [failed]\n";
//            publishProgress(50);
//
//            info = info + "All things prepared\n";
//            publishProgress(99);
//
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            textView.setText(info);
//            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            startActivity();
//        }
//    }

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
                            textView.setText(textView.getText().toString() + "Check permission: " + permissions[i] + " [failed]\n");
                        }
                    } else {
                        textView.setText(textView.getText().toString() + "Check permission: " + permissions[i] + " [ok]\n");
                    }
                }
                //delayEntryPage();
                startActivity();
                break;
            default:
                break;
        }

    }
}
