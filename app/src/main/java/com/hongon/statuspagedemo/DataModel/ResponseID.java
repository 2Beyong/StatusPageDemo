package com.hongon.statuspagedemo.DataModel;

import android.util.Log;

import com.hongon.statuspagedemo.CardBean;

import java.lang.reflect.Array;

/**
 * Created by CoCO on 2017/12/27.
 */

public class ResponseID {
    String version;
    String ModelName;
    String SecurityKey;
    String SerialNumber;
    String Nom_pv;
    String InternalVersion;
    String SaftyCountryCode;//这个先不做
    // 内置的一个bind的索引;
    CardBean Card;
    public ResponseID(Datagram d)
    {
        //判断校验是否是本类报文
        if(d.getControlCode()!=Datagram.ControlCodeOption.Read||d.getFunctionCode()!=Datagram.FunctionCodeOption.ResponseIDInfo)
        {
            return;
        }

        // 是合法报文
        byte[] data = d.getData();
        int length = d.getDatalength()&0xff;
        byte[] t =new byte[5];
        System.arraycopy(data,0,t,0,5);
        version = new String(t);
        t = new byte[10];
        System.arraycopy(data,5,t,0,10);
        ModelName = new String(t);

        t = new byte[16];
        System.arraycopy(data,15,t,0,16);
        SecurityKey = new String(t);

        t = new byte[16];
        System.arraycopy(data,31,t,0,16);
        SerialNumber = new String(t);

        t = new byte[4];
        System.arraycopy(data,47,t,0,4);
        Nom_pv = new String(t);

        t = new byte[12];
        System.arraycopy(data,51,t,0,12);
        InternalVersion = new String(t);
        // for debug
        Log.d("RespondId","version = " +version);
        Log.d("RespondId","ModelName = " +ModelName);
        Log.d("RespondId","SecurityKey = " +SecurityKey);
        Log.d("RespondId","SerialNumber = " +SerialNumber);
        Log.d("RespondId","Nom_pv = " +Nom_pv);
        Log.d("RespondId","InternalVersion = " +InternalVersion);

    }
    //
    public void UpdateCardBean(CardBean card)
    {
        //card.setTitle("逆变器身份信息");
        if(card.getContent().size()==0) {
            card.getContent().clear();

            CardBean.CardItemBean x = card.new CardItemBean("version", version);
            card.getContent().add(x);
            x = card.new CardItemBean("ModelName", ModelName);
            card.getContent().add(x);
            x = card.new CardItemBean("SecurityKey", SecurityKey);
            card.getContent().add(x);
            x = card.new CardItemBean("SerialNumber;", SerialNumber);
            card.getContent().add(x);
            x = card.new CardItemBean("Nom_pv;", Nom_pv);
            card.getContent().add(x);
            x = card.new CardItemBean("InternalVersion;", InternalVersion);
            card.getContent().add(x);
        }
        else
        {

            card.getContent().get(0).setValue(version);
            card.getContent().get(1).setValue(ModelName);
            card.getContent().get(2).setValue(SecurityKey);
            card.getContent().get(3).setValue(SerialNumber);
            card.getContent().get(4).setValue(Nom_pv);
            card.getContent().get(5).setValue(InternalVersion);
        }
        //UI更新要到其他地方
    }
}
