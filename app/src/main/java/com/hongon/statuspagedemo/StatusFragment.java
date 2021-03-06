package com.hongon.statuspagedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hongon.statuspagedemo.CardAdapter;
import com.hongon.statuspagedemo.CardBean;
import com.hongon.statuspagedemo.DataModel.Datagram;
import com.hongon.statuspagedemo.DataModel.ResponseID;
import com.hongon.statuspagedemo.DataModel.ResponseRunningData;
import com.hongon.statuspagedemo.R;
import com.hongon.statuspagedemo.Service.DataQueryService;

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
        mdata.add(PVCard);

        mdata.add(LoadCard);
        mdata.add(BatteryCard);

        mdata.add(UtilityCard);
        mdata.add(OtherCard);

        rec = getView().findViewById(R.id.fragment_recyclerView);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));//不知道行不行
        rec.setAdapter(new CardAdapter(mdata));
        rec.setNestedScrollingEnabled(false);

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
    public CardBean LoadCard;
    public CardBean OtherCard;
    void initialCard()
    {
        /* 参考数据
        // PV member
    private String PV1Voltage; // 0.1为单位 0x00
    private String PV2Voltage;  // 0.1为单位 0x01
    private String PV1Current;  // 0.1为单位 0x02
    private String PV2Current;  // 0.1为单位 0x03

    private String TotalPVEnery; // 高位在0x22 低位在 0x25
    public String getPV1Voltage() {
        return PV1Voltage;
    }

    public String getPV2Voltage() {
        return PV2Voltage;
    }

    public String getPV1Current() {
        return PV1Current;
    }

    public String getPV2Current() {
        return PV2Current;
    }

    // Grid member ( only one phase)
    private String PhaseL1Voltage;     // 0.1为单位 0x04
    private String PhaseL1Current;   // 0.1为单位 0x07
    private String PhaseL1Frequency; // 0.01为单位 0x0A

    private String FeedingPower; // 注意它的高位在0x2f,低位在0x0d

    // 综合
    private String WorkMode; // 查表

    private String Temperature; // 0.1为单位 0x0F

    private String ErrorMessage; // 高位在0X10，地位在0x11

    private String TotalFeedEnergytoGrid;// 0x12 , 低0x13

    private String TotalFeedingHours; // 0x14 0x15

    //  电池

    private String VBattery;    //0x21
    private String CBattery;    // 0x23
    private String IBattery;    //0x24

    //  负载
    private String LoadPower;   //0x27
    private String VLoad;       //0x2C
    private String iLoad;       //0x2B
         */
        BatteryCard = new CardBean(getString(R.string.battery_status_title));
        BatteryCard.getContent().add(BatteryCard.new CardItemBean(getString(R.string.battery_voltage),""));
        BatteryCard.getContent().add(BatteryCard.new CardItemBean(getString(R.string.battery_capacity),""));
        BatteryCard.getContent().add(BatteryCard.new CardItemBean(getString(R.string.battery_current),""));

        PVCard = new CardBean(getString(R.string.pv_status_title));
        PVCard.getContent().add(PVCard.new CardItemBean(getString(R.string.pv1_voltage),""));
        PVCard.getContent().add(PVCard.new CardItemBean(getString(R.string.pv2_voltage),""));
        PVCard.getContent().add(PVCard.new CardItemBean(getString(R.string.pv1_current),""));
        PVCard.getContent().add(PVCard.new CardItemBean(getString(R.string.pv2_current),""));
        //

        IDCard =new CardBean();
        IDCard.setTitle(getString(R.string.InverterInfoList));
        IDCard.getContent().add(IDCard.new CardItemBean(getString(R.string.Inverter_Version),"0"));
        IDCard.getContent().add(IDCard.new CardItemBean(getString(R.string.Inverter_Model),"0"));
        //IDCard.getContent().add(IDCard.new CardItemBean("安全码","0"));
        IDCard.getContent().add(IDCard.new CardItemBean(getString(R.string.Serial_Number),"0"));
        IDCard.getContent().add(IDCard.new CardItemBean(getString(R.string.Inverter_PV_Rated_Voltage),"0"));
        IDCard.getContent().add(IDCard.new CardItemBean(getString(R.string.Inverter_ChipVer),"0"));


        UtilityCard = new CardBean(getString(R.string.utility_status_title));
        UtilityCard.getContent().add(UtilityCard.new CardItemBean(getString(R.string.utility_voltage),""));
        UtilityCard.getContent().add(UtilityCard.new CardItemBean(getString(R.string.utility_current),""));
        UtilityCard.getContent().add(UtilityCard.new CardItemBean(getString(R.string.utility_frequency),""));
        UtilityCard.getContent().add(UtilityCard.new CardItemBean(getString(R.string.utility_feeding_power),""));
        //UtilityCard.getContent().add(UtilityCard.new CardItemBean("FeedPower？",""));

        LoadCard =new CardBean(getString(R.string.load_status_title));
        LoadCard.getContent().add(LoadCard.new CardItemBean(getString(R.string.load_power),""));
        LoadCard.getContent().add(LoadCard.new CardItemBean(getString(R.string.load_voltage),""));
        LoadCard.getContent().add(LoadCard.new CardItemBean(getString(R.string.load_current),""));
        OtherCard = new CardBean(getString(R.string.other_status_title));
        OtherCard.getContent().add(OtherCard.new CardItemBean(getString(R.string.other_total_inverter),""));
        OtherCard.getContent().add(OtherCard.new CardItemBean(getString(R.string.other_total_feeding_hour),""));
        OtherCard.getContent().add(OtherCard.new CardItemBean(getString(R.string.other_total_feeding_energy),""));
        OtherCard.getContent().add(OtherCard.new CardItemBean(getString(R.string.other_working_mode),""));
        OtherCard.getContent().add(OtherCard.new CardItemBean(getString(R.string.other_working_temperature),""));
        OtherCard.getContent().add(OtherCard.new CardItemBean(getString(R.string.other_working_inverter_power),""));


    }

    private final String tag ="StatusFragment";
    public DataQueryService.OnIDReceivedListener updateIDCard = new DataQueryService.OnIDReceivedListener() {
        @Override
        public void OnIDReceived(Datagram datagram) {
            Log.d(tag,"updateIDCard");
            ResponseID responseID = new ResponseID(datagram);
            responseID.UpdateCardBean(IDCard);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rec.getAdapter().notifyDataSetChanged();
                }
            });

        }
    };

    public DataQueryService.OnRunningDataReceivedListener updateRunningDataCard = new DataQueryService.OnRunningDataReceivedListener() {
        @Override
        public void OnRunningDataReceived(Datagram datagram) {
            Log.d(tag,"updateIDCard");
            ResponseRunningData d =new ResponseRunningData(datagram);

            // 更新光伏
            PVCard.getContent().get(0).setValue(d.getPV1Voltage());
            PVCard.getContent().get(1).setValue(d.getPV2Voltage());
            PVCard.getContent().get(2).setValue(d.getPV1Current());
            PVCard.getContent().get(3).setValue(d.getPV2Current());
            // 更新负载
            LoadCard.getContent().get(0).setValue(d.getLoadPower());
            LoadCard.getContent().get(1).setValue(d.getVLoad());
            LoadCard.getContent().get(2).setValue(d.getiLoad());
            // 更新电池
            BatteryCard.getContent().get(0).setValue(d.getVBattery());
            BatteryCard.getContent().get(1).setValue(d.getCBattery());
            BatteryCard.getContent().get(2).setValue(d.getIBattery());
            // 更新电网运行状态

            UtilityCard.getContent().get(0).setValue(d.getPhaseL1Voltage());
            UtilityCard.getContent().get(1).setValue(d.getPhaseL1Current());
            UtilityCard.getContent().get(2).setValue(d.getPhaseL1Frequency());
            //UtilityCard.getContent().get(3).setValue(d.getTotalFeedEnergytoGrid());

            // 更新其他
            OtherCard.getContent().get(0).setValue(d.getTotalPVEnery());
            OtherCard.getContent().get(1).setValue(d.getTotalFeedingHours());
            OtherCard.getContent().get(2).setValue(d.getTotalFeedEnergytoGrid());
            OtherCard.getContent().get(3).setValue(d.getWorkMode());
            OtherCard.getContent().get(4).setValue(d.getTemperature());
            OtherCard.getContent().get(5).setValue(d.getTotalPower_str());


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CardAdapter adapter =(CardAdapter) rec.getAdapter();
                    adapter.NotifySubRecyclerView();

                }
            });

        }
    };
}
