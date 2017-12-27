package com.hongon.statuspagedemo.DataModel;

import android.os.Debug;
import android.preference.PreferenceActivity;
import android.util.Log;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.zip.Checksum;

/**
 * 本类规定了goodwe逆变器通信协议中的数据报文部分的格式与解析
 * 根据Inverter.VS.EzFamily_PROTOCOL_V4.2一问撰写
 * Created by Admin on 2017/12/26.
 */

public class Datagram {

    // Header包含了Source与Destination
    private static final String tag ="Datagram";
    private byte[] Header = new byte[2] ;
    private byte Source;
    private byte Destination;
    private byte ControlCode;
    private byte FunctionCode;
    private byte Datalength;
    private byte[] Data;
    private byte[] checksum = new byte[2];
    //

    public byte[] getData() {
        return Data;
    }

    public byte getDatalength() {
        return Datalength;
    }

    public byte getControlCode() {
        return ControlCode;
    }

    public byte getFunctionCode() {
        return FunctionCode;
    }

    public byte[] getChecksum() {
        return checksum;
    }

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
        // 需要强制转换
        static final byte  QueryReadDataList = (byte)0x00;
        static final byte  ResponseReadDataList =(byte)0x80;
        static final byte  QueryRunningInfo = 0x01;
        static final byte  ResponseRunningInfo = (byte)0x81;
        static final byte  QueryIDInfo = 0x02;
        static final byte  ResponseIDInfo = (byte)0x82;

    }
    // 构造函数
    public Datagram()
    {

    }
    //从接受的UDP包裹中解析字符串
    public Datagram(DatagramPacket response)
    {
        byte[] T = response.getData();
        // 解析字符串,放置各部分。
        if(T.length>=9) {
            Header[0] = T[0];
            Header[1] = T[1];
            Source = T[2];
            Destination = T[3];
            ControlCode = T[4];
            FunctionCode= T[5];
            Datalength = T[6];
            Data = new byte[Datalength];
            int offset = 7;
            int i=0;
            for(;i<Datalength;++i)
            {
                Data[i]=T[i+offset];
            }
            checksum[0] =T[i+offset];
            checksum[1] =T[i+offset+1];
            Log.d(tag,"Head "+bytesToHexString(Header));
        }


    }

    //
    public String bytesToHexString(byte[] x)
    {

        if(x==null ||x.length<=0)
        {
            return null;
        }
        StringBuilder sb =new StringBuilder("");
        for(int i =0;i<x.length;++i)
        {
            int v = x[i]&0xFF;
            String s = Integer.toHexString(v);
            if(s.length()<2)
            {
                sb.append(0);
            }
            sb.append(s);
        }
        return sb.toString();
    }
}
