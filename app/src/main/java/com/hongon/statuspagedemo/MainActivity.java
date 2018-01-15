package com.hongon.statuspagedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.hongon.statuspagedemo.Service.DataQueryService;

import java.util.Locale;

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
            dataQueryService.addOnRunningDataReceivedListener(statusFragment.updateRunningDataCard);
            dataQueryService.addOnRunningDataReceivedListener(monitorFragment);
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
        setLanguage();
        setContentView(R.layout.activity_main);
        initialToolbar();
        initialFragment();
        initalTab();
        initNavigation();
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
        if(fm.findFragmentByTag("MonitorFragment")==null){
            monitorFragment = new MonitorFragment();

            ft.add(R.id.FragmentViewpage,monitorFragment,"MonitorFragment");
        }
        else{
            monitorFragment =(MonitorFragment) fm.findFragmentByTag("MonitorFragment");
        }
        if(fm.findFragmentByTag("StatusFragment")==null){
            statusFragment = new StatusFragment();
            ft.add(R.id.FragmentViewpage,statusFragment,"StatusFragment");
        }
        else{
            statusFragment =(StatusFragment) fm.findFragmentByTag("StatusFragment");
        }
        //

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
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            case android.R.id.home:
                // do something

                drawerLayout.openDrawer(Gravity.LEFT);
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

    // navigation 设置
    DrawerLayout drawerLayout;
    private void initNavigation()
    {
        drawerLayout = findViewById(R.id.drawerlayout);
        NavigationView navigationView=findViewById(R.id.navigationView_main_activity);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                TabLayout tb = findViewById(R.id.tablayout);
                switch (id)
                {
                    case R.id.navi_menu_item_Monitor:

                        tb.getTabAt(0).select();
                        break;
                    case R.id.navi_menu_item_DataList:
                         tb = findViewById(R.id.tablayout);
                        tb.getTabAt(1).select();
                        break;
                    case R.id.navi_menu_item_Setting:
                        Toast.makeText(MainActivity.this,"TO DO",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navi_menu_item_WifiConfig:
                        CallWifiConfigActivity();
                        break;
                    case R.id.navi_menu_item_Language:
                        //LanguageSetting();
                        break;
                    default:break;
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
    }

    // language Setting
    protected void selectLanguage(String language) {
        //设置语言类型
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        switch (language) {
            case "en":
                configuration.locale = Locale.ENGLISH;
                break;
            case "zh":
                configuration.locale = Locale.SIMPLIFIED_CHINESE;
                break;
            default:
                configuration.locale = Locale.getDefault();
                break;
        }
        resources.updateConfiguration(configuration, displayMetrics);

        //保存设置语言的类型
        SharedPreferences sharedPreferences =getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString("language_Key",language);

        editor.commit();
    }
    // onCreate时调用
    private void setLanguage(){
        SharedPreferences sharedPreferences =getSharedPreferences("preference", Context.MODE_PRIVATE);
        String str =sharedPreferences.getString("language_Key","cn");
        selectLanguage(str);
    }

}