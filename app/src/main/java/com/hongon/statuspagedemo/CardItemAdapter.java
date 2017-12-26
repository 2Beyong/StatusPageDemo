package com.hongon.statuspagedemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Admin on 2017/12/21.
 */

public class CardItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<CardBean.CardItemBean> data;
    class Holder extends RecyclerView.ViewHolder{
        TextView tv ;
        public Holder(View view)
        {
            super(view);
            tv = view.findViewById(R.id.card_item_tv);
        }
    }
    class HolderWithSwitch extends RecyclerView.ViewHolder{
        TextView tv ;
        Switch aSwitch;
        public HolderWithSwitch(View view)
        {
            super(view);
            tv = view.findViewById(R.id.card_item_tv);
            aSwitch = view.findViewById(R.id.card_item_switch);
        }
    }
    public CardItemAdapter(List<CardBean.CardItemBean> data)
    {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType ==1 ) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
            return new Holder(view);
        }
        if(viewType ==0 ) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_with_switch, parent, false);
            return new HolderWithSwitch(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if(viewType ==0) {
            ((HolderWithSwitch)holder).tv.setText(data.get(position).getName());
            ((HolderWithSwitch)holder).aSwitch.setChecked(true);
        }
        if(viewType ==1) {
            ((Holder)holder).tv.setText(data.get(position).getName());

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position%2 == 0)
        return 1;

        return 0;
    }
}
