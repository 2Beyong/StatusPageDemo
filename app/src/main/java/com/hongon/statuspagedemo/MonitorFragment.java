package com.hongon.statuspagedemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hongon.statuspagedemo.R;
import com.hongon.statuspagedemo.View.ButtonView;
import com.hongon.statuspagedemo.View.MonitorDialog;

/**
 * Created by Admin on 2017/12/27.
 */

public class MonitorFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monitor,container,false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        BindDialog();
    }

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
}

