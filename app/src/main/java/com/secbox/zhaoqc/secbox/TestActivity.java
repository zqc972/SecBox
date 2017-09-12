package com.secbox.zhaoqc.secbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import module.HostScanner;
import module.HostScanner2;
import utils.NetworkUtils;

public class TestActivity extends AppCompatActivity {

    private Button start;
    private Button stop;
    private HostScanner2 scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        start = (Button)findViewById(R.id.start);
        stop = (Button)findViewById(R.id.stop);

        scanner = new HostScanner2(getApplicationContext(), NetworkUtils.getIp(getApplicationContext()),NetworkUtils.getNetmask(getApplicationContext()));

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanner.start();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanner.stop();
            }
        });
    }
}
