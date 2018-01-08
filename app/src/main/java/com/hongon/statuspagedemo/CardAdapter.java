package com.hongon.statuspagedemo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017/12/21.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.Holder> {

    List<CardBean> data;
    List<CardItemAdapter> adapters;
    public  CardAdapter(List<CardBean> data)
    {
        this.data = data;
        adapters = new ArrayList<>();
        for(CardBean i:data){
            adapters.add(new CardItemAdapter(i.getContent()));
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_menu,parent,false);
        Holder holder =  new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.title.setText(data.get(position).getTitle());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.context));
        holder.recyclerView.setAdapter(adapters.get(position));
        //关闭动画
        holder.recyclerView.setItemAnimator(null);

        //设置分割线
        CardItemDecoration decoration = new CardItemDecoration();
        decoration.setSize(2);
        decoration.setColor(0xFFDDDDDD);
        holder.recyclerView.addItemDecoration(decoration);
        //为Holder增加监听事件。
        // 动画效果
        //动画效果
        final Animation rotateAnimation = new RotateAnimation(
                0,90,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        final Animation anti_rotateAnimation = new RotateAnimation(
                90,0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardItemAdapter adapter = (CardItemAdapter) holder.recyclerView.getAdapter();
                if (adapter.getVisible() == false) {
                    adapter.showAllitems();
                    anti_rotateAnimation.setDuration(300);
                    anti_rotateAnimation.setFillAfter(true);
                    holder.imageButton.startAnimation(anti_rotateAnimation);
                } else {
                     adapter.hideAllitems();
                    rotateAnimation.setDuration(300);
                    rotateAnimation.setFillAfter(true);
                    holder.imageButton.startAnimation(rotateAnimation);
                    //holder.imageButton.setImageResource(R.drawable.ic_keyboard_arrow_left_white_24dp);
                }
            }
        });


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
        ImageButton imageButton;
        public Holder(View view)
        {
            super(view);
            context =view.getContext();
            title =view.findViewById(R.id.card_menu_tv);
            recyclerView = view.findViewById(R.id.card_menu_recyclerView);
            imageButton =view.findViewById(R.id.show_hide_button);

        }
    }

    public void NotifySubRecyclerView(){
        for(CardItemAdapter adapter:adapters){
            adapter.notifyDataSetChanged();
        }
    }



}
