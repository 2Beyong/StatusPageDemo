package com.hongon.statuspagedemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Admin on 2017/12/21.
 */

public class CardItemAdapter extends RecyclerView.Adapter<CardItemAdapter.Holder> {

    List<String> data;
    class Holder extends RecyclerView.ViewHolder{
        TextView tv ;
        public Holder(View view)
        {
            super(view);
            tv = view.findViewById(R.id.card_item_tv);
        }
    }
    public CardItemAdapter(List<String> data)
    {
        this.data = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.tv.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
