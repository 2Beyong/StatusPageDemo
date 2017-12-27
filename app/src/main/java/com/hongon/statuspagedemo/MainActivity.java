package com.hongon.statuspagedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
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
        initial();
        List<CardBean> mdata = new ArrayList<>();
        mdata.add(IDCard);
        mdata.add(BatteryCard);
        mdata.add(PVCard);
        mdata.add(UtilityCard);
        //mdata.add(UtilityCard);

        rec = findViewById(R.id.activity_main_recycleView);
        rec.setLayoutManager(new LinearLayoutManager(this));
        rec.setAdapter(new CardAdapter(mdata));


        // 尝试连接Service
        Intent StartIntent = new Intent(this, DataQueryService.class);
        bindService(StartIntent,mConnection,BIND_AUTO_CREATE);
        startService(StartIntent);
        Log.d(tag,"Created");
    }

    @Override
    protected void onDestroy() {
        // 尝试关闭Service
        Intent StopIntent = new Intent(this, DataQueryService.class);
        unbindService(mConnection);
        stopService(StopIntent);
        super.onDestroy();
    }

    Datagram datagram;
    //---

    private void initial()
    {
        initialCard();
    }
    //--
    RecyclerView rec;//主RecycleView;

    public RecyclerView getRec() {
        return rec;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Test","Main Activity is Restart.");



    }
    // -- 定义一个数据源
    CardBean BatteryCard ;
    CardBean PVCard;
    CardBean UtilityCard;
    public CardBean IDCard;
    void initialCard()
    {

        BatteryCard = new CardBean(getString(R.string.batteryStatus));
        PVCard = new CardBean(getString(R.string.PVStatus));

        BatteryCard.getContent().add(BatteryCard.new CardItemBean(getString(R.string.batteryAmpere),""));
        //

        IDCard =new CardBean();
        IDCard.setTitle("逆变器身份信息");
        IDCard.getContent().add(IDCard.new CardItemBean("版本","0"));
        IDCard.getContent().add(IDCard.new CardItemBean("逆变器型号","0"));
        IDCard.getContent().add(IDCard.new CardItemBean("安全码","0"));
        IDCard.getContent().add(IDCard.new CardItemBean("序列号","0"));
        IDCard.getContent().add(IDCard.new CardItemBean("额定PV电压","0"));
        IDCard.getContent().add(IDCard.new CardItemBean("固件版本","0"));


        UtilityCard = new CardBean(getString(R.string.UtilityStatus));

    }
}