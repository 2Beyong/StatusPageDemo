package com.hongon.statuspagedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hongon.statuspagedemo.R;

/**
 * Created by Admin on 2018/1/3.
 */

public class WifiConfigActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_config_layout);
        Toolbar toolbar =  findViewById(R.id.wifi_config_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wifi配置界面");
        ActionBar T = getSupportActionBar();
        T.setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            // 实现按钮的返回功能
            case android.R.id.home:
                Toast.makeText(this,"homeAsUp Pressed.",Toast.LENGTH_SHORT).show();
                this.finish();
                return true;
            case R.id.home:
                Toast.makeText(this,"home Pressed.",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
