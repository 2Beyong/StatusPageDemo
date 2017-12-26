package com.hongon.statuspagedemo.DataModel;

import android.preference.PreferenceActivity;
import android.util.Log;

import java.util.ArrayList;

/**
 * 本类规定了goodwe逆变器通信协议中的数据报文部分的格式与解析
 * 根据Inverter.VS.EzFamily_PROTOCOL_V4.2一问撰写
 * Created by Admin on 2017/12/26.
 */

public class Datagram {
    // Header包含了Source与Destination
    private byte[] Header ;
    private byte ControlCode;
    private byte FunctionCode;
    private byte Datalength;
    private byte[] data;
    private byte[] checksum = new byte[2];
    // 头部选项
    public  static class HeadOption {
        static final byte[] QueryHeader = new byte[]{(byte)0xaa,(byte)0x55,(byte)0xb0,(byte)0x7F};
        static final byte[] RespondHeader = new byte[]{(byte)0xaa,(byte)0x55,(byte)0x7f,(byte)0xb0};
    }

    // 控制代码选项
    public  class ControlCodeOption
    {
        static final byte  Register = 0x00;
        static final byte  Read = 0x01;
        static final byte  Write = 0x02;
        static final byte  Excute = 0x03;
    }

    // 功能代码选项
    public  class FunctionCodeOption
    {
        // byte只能表示 -127 到 128
        // 所以使用超过0x7F的值就会报错！
        static final byte  QueryReadDataList = 0x00;
        //static final byte  ResponseReadDataList =0x80;
        static final byte  QueryRunningInfo = 0x01;
        //static final byte  ResponseRunningInfo = 0x81;
        static final byte  QueryIDInfo = 0x02;
        //static final byte  ResponseIDInfo = 0x82;

    }
    // 构造函数
    public Datagram()
    {
        Header = HeadOption.QueryHeader.clone();
        ControlCode =ControlCodeOption.Read;
        FunctionCode =FunctionCodeOption.QueryIDInfo;
        Log.d("Datagram_header = ",Header.toString());
        Log.d("Datagram_controlCode =", (""+ControlCode));

    }


}
