package com.hongon.statuspagedemo;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
        List<CardBean> mdata = new ArrayList<>();
        mdata.add(BatteryCard);
        mdata.add(PVCard);
        //mdata.add(UtilityCard);

        rec = findViewById(R.id.activity_main_recycleView);
        rec.setLayoutManager(new LinearLayoutManager(this));
        rec.setAdapter(new CardAdapter(mdata));

        // 尝试添加一个Datagram
        datagram= new Datagram();
    }
    Datagram datagram;
    //---
    private List<List<String>> mdata;
    private List<String> mTitle;
    private void initial()
    {
        initialCard();


    }
    //--
    RecyclerView rec;
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Test","Main Activity is Restart.");

            BatteryCard.getContent().add(BatteryCard.new CardItemBean("新家的一个属性",""));
            BatteryCard.getContent().get(0).setName("更改后的item");
            rec.getAdapter().notifyDataSetChanged();

    }
    // -- 定义一个数据源
    CardBean BatteryCard ;
    CardBean PVCard;
    CardBean UtilityCard;
    void initialCard()
    {

        BatteryCard = new CardBean(getString(R.string.batteryStatus));
        PVCard = new CardBean(getString(R.string.PVStatus));

        BatteryCard.getContent().add(BatteryCard.new CardItemBean(getString(R.string.batteryAmpere),""));
    }
}