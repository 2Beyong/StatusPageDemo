package com.hongon.statuspagedemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hongon.statuspagedemo.DataModel.Datagram;
import com.hongon.statuspagedemo.DataModel.ResponseRunningData;
import com.hongon.statuspagedemo.R;
import com.hongon.statuspagedemo.Service.DataQueryService;
import com.hongon.statuspagedemo.View.BigView;
import com.hongon.statuspagedemo.View.ButtonView;
import com.hongon.statuspagedemo.View.MonitorDialog;

/**
 * Created by Admin on 2017/12/27.
 */

public class MonitorFragment extends Fragment implements DataQueryService.OnRunningDataReceivedListener{

    // ---
    BigView bigView ;
    // ---
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monitor,container,false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        bigView =getView().findViewById(R.id.bigView);
        BindDialog();
    }
    //
    // initDialogButton
    private void BindDialog()
    {
        ButtonView Bb_Solar = getView().findViewById(R.id.CircleA);
        Bb_Solar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示dialog
                ShowDialog(((MainActivity)getActivity()).statusFragment.PVCard);
            }
        });

        ButtonView Bb_Load = getView().findViewById(R.id.CircleC);
        Bb_Load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示dialog
                ShowDialog(((MainActivity)getActivity()).statusFragment.LoadCard);
            }
        });

        ButtonView Bb_Utility = getView().findViewById(R.id.CircleB);
        Bb_Utility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示dialog
                ShowDialog(((MainActivity)getActivity()).statusFragment.UtilityCard);
            }
        });

        ButtonView Bb_Battery = getView().findViewById(R.id.CircleD);
        Bb_Battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示dialog
                ShowDialog(((MainActivity)getActivity()).statusFragment.BatteryCard);
            }
        });
    }
    //
    private void ShowDialog(CardBean cb)
    {

        MonitorDialog dialog = new MonitorDialog(getContext(),cb);

        dialog.show();


    }

    final String tag ="MonitorFragment";
    @Override
    public void OnRunningDataReceived(Datagram datagram) {
        Log.e(tag,"OnRunningDataReceived");
        ResponseRunningData data =new ResponseRunningData(datagram);

        // 0 = Disabled
        int a =0; // grid
        int b = 0; // pv
        int c =0;   //  load
        int d =0;   //  battery
        // 光伏只有一个方向。
        // 符号位另说
        if(!data.getPV1Voltage().equals("0.0V")||!data.getPV2Voltage().equals("0.0V"))
        {
            b = BigView.PATH_0_1;
        }
        // 负载只有一个方向。
        if(!data.getLoadPower().equals("0.0W"))
        {
            c=BigView.PATH_1_0;
        }

        if(!data.getPhaseL1Current().equals("0.0A"))
        {
            //有可能也是反的
            if(data.getIBattery().startsWith("-"))
            {
                a=BigView.PATH_0_1;
            }
            else {
                a=BigView.PATH_1_0;
            }
        }
        //通过电流是否有负号即可判断流向
        //为正为充电1-0，为负为放电0-1
        if(!data.getIBattery().equals("0.0A"))
        {
            if(data.getIBattery().startsWith("-"))
            {
                d=BigView.PATH_0_1;
            }
            else {
                d=BigView.PATH_1_0;
            }
        }

        bigView.setFlag(a,b,c,d);
    }
}

