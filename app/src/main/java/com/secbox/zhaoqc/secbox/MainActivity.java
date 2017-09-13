package com.secbox.zhaoqc.secbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

import layout.FunctionListFragment;
import layout.MostUseFragment;
import layout.RecentUseFragment;
import utils.NetworkUtils;
import utils.ShellUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MainActivity","result code : " + ShellUtils.run_for_resultCode(new String[] {"/system/bin/cat","/proc/net/arp"},"/"));
                        try {
                            String result = ShellUtils.run(new String[] {"/system/bin/cat","/proc/net/arp"},"/");
                            Log.i("MainActivity","result : " + result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                if(NetworkUtils.isNetworkAvailable(getApplicationContext()))
                    Snackbar.make(view,"Network is available",Snackbar.LENGTH_LONG).show();
                else
                    Snackbar.make(view,"Network is not available",Snackbar.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(),BackgroundTaskActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_test) {
            Intent intent = new Intent(this,TestActivity.class);
            startActivity(intent);
        } else if(id == R.id.nav_recent_used_tools) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, RecentUseFragment.newInstance()).commit();
        } else if(id == R.id.nav_most_use_tools) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, MostUseFragment.newInstance()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, FunctionListFragment.newInstance(id)).commit();
        }

//        if (id == R.id.nav_most_use_tools) {
//            // Handle the camera action
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, FunctionListFragment.newInstance(id)).commit();
//        } else if (id == R.id.nav_recent_used_tools) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, FunctionListFragment.newInstance(id)).commit();
//        } else if (id == R.id.nav_scan_tools) {
//
//        } else if (id == R.id.nav_cheat_attack) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private long exitTime = 0; //“双击两次返回”计时

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Snackbar.make(findViewById(R.id.fab), "再按一次退出", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
