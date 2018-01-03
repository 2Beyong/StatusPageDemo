package com.hongon.statuspagedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.hongon.statuspagedemo.Service.DataQueryService;

public class MainActivity extends AppCompatActivity {
    final String  tag ="MainActivity";
    // BindService
    private DataQueryService dataQueryService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            dataQueryService = ((DataQueryService.myBinder)iBinder).getService();


            //  add Listener;
            dataQueryService.setOnIDReceivedListener(statusFragment.updateIDCard);
            dataQueryService.setOnRunningDataReceivedListener(statusFragment.updateRunningDataCard);

            //
            NestedScrollView nv = findViewById(R.id.nestedScollView);

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
        initialToolbar();
        initialFragment();
        initalTab();
        BindQueryService();
        Log.d(tag,"Created");
    }

    @Override
    protected void onStop() {
        Log.e(tag,"OnStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 尝试关闭Service
        Log.e(tag,"OnDestroy");
        UnBindQueryService();
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
        ft.add(R.id.FragmentViewpage,monitorFragment);
        ft.add(R.id.FragmentViewpage,statusFragment);
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
                    ft.hide(statusFragment).show(monitorFragment);
                    ft.commit();
                }
                else if(i==1)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.hide(monitorFragment).show(statusFragment);
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
        tb.getTabAt(1).select();
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



    private void initialToolbar()
    {
        Toolbar toolbar =findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("监控页面");

    }
    //



    //初始化菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_toolbar,menu);
        return  true;
    }
    //处理菜单选中项


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_wifiConfig:
                // do something
                CallWifiConfigActivity();
                break;
            default:break;
        }
        return  true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Test","Main Activity is Restart.");

    }

    // --- 召唤Wifi配置界面 ---
    private  void CallWifiConfigActivity()
    {
        Intent intent = new Intent(MainActivity.this, WifiConfigActivity.class);
        startActivity(intent);

    }
}