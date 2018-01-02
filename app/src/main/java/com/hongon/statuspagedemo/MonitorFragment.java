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
        ButtonView Bb_Solar = getView().findViewById(R.id.CircleA);
        Bb_Solar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示dialog
                ShowDialog();
            }
        });
    }

    //
    private void ShowDialog()
    {
        String[] item ={"A","B","C"};
        AlertDialog.Builder listDialog = new AlertDialog.Builder(getContext());
        listDialog.setTitle("光伏实时信息");
        listDialog.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        listDialog.show();
    }
}
