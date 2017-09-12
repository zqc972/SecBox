package com.secbox.zhaoqc.secbox;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import db.ModuleFreqCount;
import ui.ArpCheatFragment;
import ui.BaseFragment;
import ui.PasswordGenenratorFragment;
import ui.ScanHostsFragment;
import ui.ScanPortsFragment;
import ui.SnifferFragment;
import ui.WeakPasswordDictionaryFragment;
import ui.WebCloneFragment;
import ui.WebServerFragment;

public class ModuleActivity extends AppCompatActivity {

    private BaseFragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int navigationItemId = getIntent().getIntExtra("navigationItemId",0);
        int position = getIntent().getIntExtra("position",0);
        fragment = getFragmentByPosition(navigationItemId,position);
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            String title = fragment.getTitle();
            if (title != null)
                getSupportActionBar().setTitle(title);
        }
        else
            Toast.makeText(getApplicationContext(),"模块未定义",Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_module, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.run_in_background:
                if(fragment != null)
                    FragmentQueue.fragmentList.add(fragment);
                finish();
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private BaseFragment getFragmentByPosition(int navigationItemId,int position) {
        ModuleFreqCount moduleFreqCount = DataSupport.where("navigationItemId = ? and position = ?",String.valueOf(navigationItemId),String.valueOf(position)).findFirst(ModuleFreqCount.class);
        if(moduleFreqCount == null) {
            moduleFreqCount = new ModuleFreqCount();
            moduleFreqCount.setNavigationItemId(navigationItemId);
            moduleFreqCount.setPosition(position);
            moduleFreqCount.setUsetime();
            moduleFreqCount.save();
            Log.i("ModuleActivity","new");
        } else {
            moduleFreqCount.addCount();
            moduleFreqCount.setUsetime();
            moduleFreqCount.save();
            Log.i("ModuleActivity","add");
        }

        switch (navigationItemId) {
            case R.id.nav_most_use_tools:
                switch (position){
                    default:
                        return null;
                }
            case R.id.nav_recent_used_tools:
                switch (position) {
                    default:
                        return null;
                }
            case R.id.nav_scan_tools:
                switch (position){
                    case 0:
                        return ScanHostsFragment.newInstance();
                    case 1:
                        return ScanPortsFragment.newInstance();
                    case 2:
                        return WebCloneFragment.newInstance();
                    default:
                        return null;
                }
            case R.id.nav_cheat_attack:
                switch (position) {
                    case 0:
                        return ArpCheatFragment.newInstance();
                    default:
                        return null;
                }
            case R.id.nav_sniffer:
                switch (position) {
                    case 0:
                        return SnifferFragment.newInstance();
                    default:
                        return null;
                }
            case R.id.nav_password_tools:
                switch (position) {
                    case 0:
                        return WeakPasswordDictionaryFragment.newInstance();
                    case 1:
                        return PasswordGenenratorFragment.newInstance();
                    default:
                        return null;
                }
            case R.id.nav_other_tools:
                switch (position) {
                    case 0:
                        return WebServerFragment.newInstance();
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

}
