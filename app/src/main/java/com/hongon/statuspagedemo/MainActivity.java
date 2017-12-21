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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
        rec = findViewById(R.id.activity_main_recycleView);
        rec.setLayoutManager(new LinearLayoutManager(this));
        rec.setAdapter(new CardAdapter(mTitle,mdata));
    }
    //---
    private List<List<String>> mdata;
    private List<String> mTitle;
    private void initial()
    {
        mTitle = new ArrayList<>();
        mdata = new ArrayList<>();
        for(int i =0;i<3;++i)
        {
            mTitle.add("第"+i+"张卡片");
        }
        mdata.add(new ArrayList<String>(Arrays.asList("a","b","c")));
        mdata.add(new ArrayList<String>(Arrays.asList("c","d","e")));
        mdata.add(new ArrayList<String>(Arrays.asList("a","b","c","d")));
    }
    //--
    RecyclerView rec;
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Test","Main Activity is Restart.");
        if(!mdata.isEmpty())
        {
            mdata.get(0).add("电池电量");
            rec.getAdapter().notifyDataSetChanged();
        }
    }
}
