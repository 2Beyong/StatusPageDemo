package com.hongon.statuspagedemo;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hongon.statuspagedemo.DataModel.Datagram;
import com.hongon.statuspagedemo.Service.DataQueryService;
import com.hongon.statuspagedemo.Service.MonitorFragment;
import com.hongon.statuspagedemo.Service.StatusFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final String  tag ="MainActivity";
    // BindService
    private DataQueryService dataQueryService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            dataQueryService = ((DataQueryService.myBinder)iBinder).getService();
            ((DataQueryService.myBinder)iBinder).setMainActivity(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName ) {
            dataQueryService = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialFragment();
        initalTab();

        Log.d(tag,"Created");
    }

    @Override
    protected void onDestroy() {
        // 尝试关闭Service

        super.onDestroy();
    }
    // -- Fragment Setting
    public StatusFragment statusFragment;
    public MonitorFragment monitorFragment;
    private void  initialFragment()
    {
        statusFragment = new StatusFragment();
        monitorFragment = new MonitorFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.FragmentViewpage,statusFragment);
        ft.commit();
    }
    private void initalTab()
    {
        TabItem tabA = findViewById(R.id.tabitem_1);
        TabItem tabB = findViewById(R.id.tabitem_2);
        TabLayout tb = findViewById(R.id.tablayout);
        tb.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                if(i==0)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.FragmentViewpage,monitorFragment);
                    ft.commit();
                }
                else if(i==1)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.FragmentViewpage,statusFragment);
                    ft.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    //---
    private void BindQueryService()
    {
        // 尝试连接Service
        Intent StartIntent = new Intent(this, DataQueryService.class);
        bindService(StartIntent,mConnection,BIND_AUTO_CREATE);
        startService(StartIntent);
    }
    private void UnBindQueryService()
    {
        Intent StopIntent = new Intent(this, DataQueryService.class);
        unbindService(mConnection);
        stopService(StopIntent);
    }






    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Test","Main Activity is Restart.");

    }

}