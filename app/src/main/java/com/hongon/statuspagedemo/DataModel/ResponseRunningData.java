package com.hongon.statuspagedemo.DataModel;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
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

    private String PVPower;
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

    public String getPVPower(){return PVPower;}
    // Grid member ( only one phase)
    private String PhaseL1Voltage;     // 0.1为单位 0x04
    private String PhaseL1Current;   // 0.1为单位 0x07
    private String PhaseL1Frequency; // 0.01为单位 0x0A
    private String PhaseL1Power;
    public String getPhaseL1Voltage() {
        return PhaseL1Voltage;
    }

    public String getPhaseL1Current() {
        return PhaseL1Current;
    }

    public String getPhaseL1Frequency() {
        return PhaseL1Frequency;
    }

    public String getPhaseL1Power(){return PhaseL1Power;}
    private float FeedingPower; // 注意它的高位在0x2f,低位在0x0d

    // 综合

    public boolean isConntoMeter(){
        return ConntoMeter;
    }
    public String getFeedingPower() {
        return String.format("%.3fkw",FeedingPower/1000);
    }

    public String getTotalPVEnery() {
        return TotalPVEnery;
    }

    private String WorkMode; // 查表
    private String[] workModeTable = new String[]{"等待","正在发电","故障","严重故障"};

    private String Temperature; // 0.1为单位 0x0F

    private String ErrorMessage; // 高位在0X10，地位在0x11

    private String TotalFeedEnergytoGrid;// 0x12 , 低0x13

    private String TotalFeedingHours; // 0x14

    private float TotalPower= 0f;

    private boolean ConntoMeter =false;

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
    public String getTotalPower_str(){
        return String.format(Locale.getDefault(),"%.1fw",TotalPower);
    }
    //  电池

    private String PBattery; // 自己计算
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
    private String LoadPowerKW;
    private String LoadPower;   //0x27
    public String getVLoad() {
        return VLoad;
    }

    private String VLoad;       //0x2C
    private String iLoad;       //0x2B
    public String getPBattery(){return PBattery;}
    public String getiLoad() {
        return iLoad;
    }

    public String getLoadPower() {
        return LoadPower;
    }
    public String getLoadPowerKW(){return LoadPowerKW;}

    // constructor
    public ResponseRunningData(){

    }
    public ResponseRunningData(Datagram d)
    {
        if(!isRunningData(d))
            return;

        //
        byte[] data = d.getData();
        PV1Voltage = _2byteToFloat(data,0,0.1f)+"V";
        PV2Voltage = _2byteToFloat(data,2,0.1f)+"V";

        PV1Current = _2byteToFloat(data,4,0.1f)+"A";
        PV2Current = _2byteToFloat(data,6,0.1f)+"A";

        PVPower = String.format(Locale.getDefault(),"%.3fkw",
                (_2byte_float(data,0,0.1f)*_2byte_float(data,4,0.1f)
        +_2byte_float(data,2,0.1f)*_2byte_float(data,6,0.1f))/1000);
        //
        PhaseL1Voltage = _2byteToFloat(data,8,0.1f)+"V";
        Log.d(tag,"PhaseL1Voltage : "+PhaseL1Voltage);
        PhaseL1Current = _2byteToFloat_sign(data,10,0.1f)+"A";
        PhaseL1Frequency =_2byteToFloat_2f(data,12,0.01f)+"Hz";

        PhaseL1Power = String.format(Locale.getDefault(),"%.3fkw",
                _2byte_float(data,8,0.1f)*_2byte_float_sign(data,10,0.1f)/1000
                        );
        Log.d(tag,"PhaseL1Frequence : "+PhaseL1Frequency);

        // 74应该是电表连接状态

        FeedingPower = _2byte_float_sign(data,14,1.0f);
        Log.d(tag,"FeedingPower : "+FeedingPower);
        ConntoMeter = getIfConntoMeter(data,74);

        //
        TotalPVEnery = _4byteToFloat(data,48,54,0.1f)+"KWh";
        Log.e(tag," TotalPVEnery :"+TotalPVEnery);
        // 工作模式需要查表 先略过
        //工作模式只能检测出是不是在发电，有没有故障。
        byte[] t = new byte[2];
        System.arraycopy(data,16,t,0,2);
        WorkMode = workModeTable[t[1]&0xff];

        Log.e(tag,"Work Mode :"+WorkMode);

        Temperature = _2byteToFloat_sign(data,18,0.1f)+"°C";
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
        IBattery = _2byteToFloat_sign(data,52,0.1f)+"A";
        Log.d(tag,"IBattery : "+IBattery);

        //
        LoadPower = _2byteToFloat(data,58,1f)+"W";
        Log.d(tag,"LoadPower : "+LoadPower);

        // totalPower
        TotalPower =_2byte_float(data,66,1f);
        // VLoad
        VLoad = _2byteToFloat(data,68,0.1f)+"V";
        Log.d(tag,"VLoad : "+VLoad);

        // iLoad
        iLoad = _2byteToFloat(data,70,0.1f)+"A";
        Log.d(tag,"iLoad : "+iLoad);

        // 负载功率 kw
        LoadPowerKW = String.format(Locale.getDefault(),"%.3fkw",_2byte_float(data,58,1f)/1000);
        // 自己计算的电池功率 kw
        PBattery = String.format(Locale.getDefault(),"%.3fkw",(_2byte_float(data,46,0.1f)*_2byte_float_sign(data,52,0.1f))/1000) ;
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
    private    float _2byte_float_sign(byte[] src, int index  ,float factor)
    {
        byte[] t =new byte[2];
        System.arraycopy(src,index,t,0,2);
        int  x = ((t[0]&0xff)<<8)|(t[1]&0xff);
        //这16个bit >1000 0000 0000 0000
        //说明这是一个负数，发的是补码。
        //所以实际值是65536 - x的负数
        if(x>32768)
            x=-(65536-x);

        return   x *factor;
    }
    private    float _2byte_float(byte[] src, int index  ,float factor)
    {
        byte[] t =new byte[2];
        System.arraycopy(src,index,t,0,2);
        int  x = ((t[0]&0xff)<<8)|(t[1]&0xff);

        float y = x *factor;
        return  y;
    }
    private    String _2byteToFloat_sign(byte[] src, int index  ,float factor)
    {
        byte[] t =new byte[2];
        System.arraycopy(src,index,t,0,2);
        int  x = ((t[0]&0xff)<<8)|(t[1]&0xff);
        //这16个bit >1000 0000 0000 0000
        //说明这是一个负数，发的是补码。
        //所以实际值是65536 - x的负数
        if(x>32768)
            x=-(65536-x);
        float y = x *factor;
        return String.format(Locale.CHINA,"%.1f",y) ;
    }
    private    String _2byteToFloat(byte[] src, int index  ,float factor)
    {
        byte[] t =new byte[2];
        System.arraycopy(src,index,t,0,2);
        int  x = ((t[0]&0xff)<<8)|(t[1]&0xff);

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

    //查询电表连接状态
    private  boolean getIfConntoMeter(byte[] data,int index){
        byte[] t =new byte[2];
        System.arraycopy(data,index,t,0,2);
        if(t[0] == 1){
            return  false;
        }
        else{
            return true;
        }
    }
}
