package com.hongon.statuspagedemo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Admin on 2017/12/21.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.Holder> {

    List<CardBean> data;
    public  CardAdapter(List<CardBean> data)
    {
        this.data = data;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_menu,parent,false);
        return  new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.title.setText(data.get(position).getTitle());

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.context));
        holder.recyclerView.setAdapter(new CardItemAdapter(data.get(position).getContent()));
        //设置分割线
        CardItemDecoration decoration = new CardItemDecoration();
        decoration.setSize(2);
        decoration.setColor(0xFFDDDDDD);
        holder.recyclerView.addItemDecoration(decoration);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class  Holder extends RecyclerView.ViewHolder
    {
        TextView title;
        RecyclerView recyclerView;
        Context context;
        public Holder(View view)
        {
            super(view);
            context =view.getContext();
            title =view.findViewById(R.id.card_menu_tv);
            recyclerView = view.findViewById(R.id.card_menu_recyclerView);

        }
    }

}
