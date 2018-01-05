package com.hongon.statuspagedemo.DataModel;

import android.util.Log;

import java.util.Locale;

/**
 * Created by Admin on 2017/12/27.
 */

public class ResponseRunningData {
    private final  String tag="ResponseRunningData ";
    // PV member
    private String PV1Voltage; // 0.1为单位 0x00
    private String PV2Voltage;  // 0.1为单位 0x01
    private String PV1Current;  // 0.1为单位 0x02
    private String PV2Current;  // 0.1为单位 0x03

    private String TotalPVEnery; // 高位在0x22 低位在 0x25
    public String getPV1Voltage() {
        return PV1Voltage;
    }

    public String getPV2Voltage() {
        return PV2Voltage;
    }

    public String getPV1Current() {
        return PV1Current;
    }

    public String getPV2Current() {
        return PV2Current;
    }

    // Grid member ( only one phase)
    private String PhaseL1Voltage;     // 0.1为单位 0x04
    private String PhaseL1Current;   // 0.1为单位 0x07
    private String PhaseL1Frequency; // 0.01为单位 0x0A

    public String getPhaseL1Voltage() {
        return PhaseL1Voltage;
    }

    public String getPhaseL1Current() {
        return PhaseL1Current;
    }

    public String getPhaseL1Frequency() {
        return PhaseL1Frequency;
    }

    private String FeedingPower; // 注意它的高位在0x2f,低位在0x0d

    // 综合


    public String getFeedingPower() {
        return FeedingPower;
    }

    public String getTotalPVEnery() {
        return TotalPVEnery;
    }

    private String WorkMode; // 查表

    private String Temperature; // 0.1为单位 0x0F

    private String ErrorMessage; // 高位在0X10，地位在0x11

    private String TotalFeedEnergytoGrid;// 0x12 , 低0x13

    private String TotalFeedingHours; // 0x14
    //

    public String getWorkMode() {
        return WorkMode;
    }

    public String getTemperature() {
        return Temperature;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public String getTotalFeedEnergytoGrid() {
        return TotalFeedEnergytoGrid;
    }

    public String getTotalFeedingHours() {
        return TotalFeedingHours;
    }
    //  电池

    private String VBattery;    //0x21
    private String CBattery;    // 0x23
    private String IBattery;    //0x24

    public String getVBattery() {
        return VBattery;
    }

    public String getCBattery() {
        return CBattery;
    }

    public String getIBattery() {
        return IBattery;
    }

    //  负载
    private String LoadPower;   //0x27
    private String VLoad;       //0x2C
    private String iLoad;       //0x2B

    public String getVLoad() {
        return VLoad;
    }

    public String getiLoad() {
        return iLoad;
    }

    public String getLoadPower() {
        return LoadPower;
    }

    // constructor
    public ResponseRunningData(Datagram d)
    {
        if(isRunningData(d)==false)
            return;

        //
        byte[] data = d.getData();
        PV1Voltage = _2byteToFloat(data,0,0.1f)+"V";
        PV2Voltage = _2byteToFloat(data,2,0.1f)+"V";

        PV1Current = _2byteToFloat(data,4,0.1f)+"A";
        PV2Current = _2byteToFloat(data,6,0.1f)+"A";
        //
        PhaseL1Voltage = _2byteToFloat(data,8,0.1f)+"V";
        Log.d(tag,"PhaseL1Voltage : "+PhaseL1Voltage);
        PhaseL1Current = _2byteToFloat(data,10,0.1f)+"A";
        PhaseL1Frequency =_2byteToFloat_2f(data,12,0.01f)+"Hz";
        Log.d(tag,"PhaseL1Frequence : "+PhaseL1Frequency);

        //
        FeedingPower = _4byteToFloat(data,74,14,1.0f)+"W";
        Log.d(tag,"FeedingPower : "+FeedingPower);

        //
        TotalPVEnery = _4byteToFloat(data,48,54,0.1f)+"KWh";
        Log.e(tag," TotalPVEnery :"+TotalPVEnery);
        // 工作模式需要查表 先略过
        //工作模式只能检测出是不是在发电，有没有故障。
        byte[] t = new byte[2];
        System.arraycopy(data,16,t,0,2);
        WorkMode = d.bytesToHexString(t);
        Log.e(tag,"Work Mode :"+WorkMode);

        Temperature = _2byteToFloat(data,18,0.1f)+"°C";
        Log.d(tag,"Temperature : "+Temperature);

        //
        TotalFeedEnergytoGrid = _4byteToFloat(data,24,26,0.1f)+"KWh";
        Log.d(tag,"ToalFeedEnergytoGrid : "+TotalFeedEnergytoGrid);

        //
        TotalFeedingHours = _4byteToFloat(data,28,30,0.1f)+"h";
        Log.d(tag,"TotalFeedingHours : "+TotalFeedingHours);

        // skip 32 34 36 38 40 42 44
        // 46
        VBattery = _2byteToFloat(data,46,0.1f)+"V";
        Log.d(tag,"VBattery : "+VBattery);

        //
        CBattery = _2byteToFloat(data,50,1f)+"%";
        Log.d(tag,"CBattery : "+CBattery);

        //
        IBattery = _2byteToFloat(data,52,0.1f)+"A";
        Log.d(tag,"IBattery : "+IBattery);

        //
        LoadPower = _2byteToFloat(data,58,1f)+"W";
        Log.d(tag,"LoadPower : "+LoadPower);

        // VLoad
        VLoad = _2byteToFloat(data,68,0.1f)+"V";
        Log.d(tag,"VLoad : "+VLoad);

        // iLoad
        iLoad = _2byteToFloat(data,70,0.1f)+"A";
        Log.d(tag,"iLoad : "+iLoad);


    }

    //  check
    private boolean isRunningData(Datagram d)
    {
        Log.d(tag,"check ");
        //报文符合格式 返回true
        if(d.getControlCode()==Datagram.ControlCodeOption.Read&&d.getFunctionCode()==Datagram.FunctionCodeOption.ResponseRunningInfo) {
            Log.d(tag,"check pass");
            return true;
        }
        Log.d(tag,"check fail. ");
        return false;
    }

    // 2byte to float
    // factor 是系数
    private  String _2byteToFloat(byte[] src, int index  ,float factor)
    {
        byte[] t =new byte[2];
        System.arraycopy(src,index,t,0,2);
        int  x = ((t[0]&0xff)<<8)|(t[1]&0xff);
        //这里强制将所有换成负数
        if(x>32768)
            x=-x;
        float y = x *factor;
        return String.format(Locale.CHINA,"%.1f",y) ;
    }
    private  String _2byteToFloat_2f(byte[] src, int index  ,float factor)
    {
        byte[] t =new byte[2];
        System.arraycopy(src,index,t,0,2);
        int  x = ((t[0]&0xff)<<8)|(t[1]&0xff);

        float y = x *factor;
        return String.format(Locale.CHINA,"%.2f",y) ;
    }

    // factor 是系数
    private  String _4byteToFloat(byte[] src, int indexH ,int indexL  ,float factor)
    {
        byte[] h =new byte[2];
        byte[] l =new byte[2];
        System.arraycopy(src,indexH,h,0,2);
        System.arraycopy(src,indexL,l,0,2);
        int x = l[1]&0x0FF;
        //Log.d(tag,"x : "+ x);
        x=x|(l[0]&0x0FF)<<8;
        //Log.d(tag,"x : "+ x);
        x=x|(h[1]&0x0FF)<<16;
        //Log.d(tag,"x : "+ x);

        x=x|(h[0]&0x0FF)<<24;

        Log.d(tag,"x : "+ x);
        float y = x*factor;
        return  String.format(Locale.CHINA,"%.1f",y) ;
    }

    private  String _4byteToFloat_2f(byte[] src, int indexH ,int indexL  ,float factor)
    {
        byte[] h =new byte[2];
        byte[] l =new byte[2];
        System.arraycopy(src,indexH,h,0,2);
        System.arraycopy(src,indexL,l,0,2);
        int x = l[1]&0x0FF;
        //Log.d(tag,"x : "+ x);
        x=x|(l[0]&0x0FF)<<8;
        //Log.d(tag,"x : "+ x);
        x=x|(h[1]&0x0FF)<<16;
        //Log.d(tag,"x : "+ x);

        x=x|(h[0]&0x0FF)<<24;

        Log.d(tag,"x : "+ x);
        float y = x*factor;
        return  String.format(Locale.CHINA,"%.2f",y) ;
    }
}
