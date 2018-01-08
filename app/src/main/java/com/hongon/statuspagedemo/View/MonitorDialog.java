package com.hongon.statuspagedemo.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hongon.statuspagedemo.CardBean;
import com.hongon.statuspagedemo.CardItemAdapter;
import com.hongon.statuspagedemo.CardItemDecoration;
import com.hongon.statuspagedemo.R;

/**
 * Created by Admin on 2018/1/2.
 */

public class MonitorDialog extends Dialog {
    //

    private CardBean cardBean;
    // 控件
    private TextView textView;
    private RecyclerView rec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //

        Window window = this.getWindow();


        window.setContentView(R.layout.monitordialog_layout);
        Display d = window.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int)(d.getWidth()*0.8);
        window.setAttributes(lp);

        //setContentView(R.layout.card_menu);
        textView = findViewById(R.id.card_menu_tv);
        rec = findViewById(R.id.card_menu_recyclerView);
        init();

    }

    //
    private void init()
    {
        // check
        if(cardBean == null)
        {
            cardBean = new CardBean("空白标题栏");
        }
        textView.setText(cardBean.getTitle());
        rec.setLayoutManager(new LinearLayoutManager(getContext()));
        rec.setAdapter(new CardItemAdapter(cardBean.getContent()));
        CardItemDecoration decoration = new CardItemDecoration();
        decoration.setSize(2);
        decoration.setColor(0xFFDDDDDD);
        rec.addItemDecoration(decoration);
    }
    // constructor
    public MonitorDialog(Context context)
    {
        super(context);
    }
    public MonitorDialog(Context context,  CardBean cb){
        super(context);

        cardBean = cb;
    }
}
