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

    List<String> Title;
    List<List<String>> data;
    public  CardAdapter(List<String> title ,List<List<String>> data)
    {
        this.data = data;
        this.Title = title;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_menu,parent,false);
        return  new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.title.setText(Title.get(position));

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.context));
        holder.recyclerView.setAdapter(new CardItemAdapter(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return Title.size();
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
