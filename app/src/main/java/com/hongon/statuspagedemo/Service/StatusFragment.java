package com.hongon.statuspagedemo.Service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hongon.statuspagedemo.CardAdapter;
import com.hongon.statuspagedemo.CardBean;
import com.hongon.statuspagedemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/12/27.
 */

public class StatusFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialCard();
        List<CardBean> mdata = new ArrayList<>();
        mdata.add(IDCard);
        mdata.add(BatteryCard);
        mdata.add(PVCard);
        mdata.add(UtilityCard);

        rec = getView().findViewById(R.id.fragment_recyclerView);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));//不知道行不行
        rec.setAdapter(new CardAdapter(mdata));
    }

    // -- other member
    //--
    RecyclerView rec;//主RecycleView;
    public RecyclerView getRec() {
        return rec;
    }
    // -- 定义一个数据源
    public CardBean BatteryCard ;
    public CardBean PVCard;
    public CardBean UtilityCard;
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
