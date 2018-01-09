package com.hongon.statuspagedemo.Service;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by CoCO on 2018/1/9.
 */

public  class NetStatus {

    static final String tag = "NetStatus";
    Context context;

    private  static NetStatus instance = null;

    private NetStatus(Context context){
        this.context = context;
    }

    public static NetStatus getInstance(Context context){
        try{
            if(instance!=null){


            }else {
                synchronized (NetStatus.class){
                    if(instance==null){
                        instance = new NetStatus(context);
                    }
                }
            }
            return  instance;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return  instance;
    }
    public InetAddress InverterHost ;
    // 获取网关地址
    public  String getAPaddress()
    {
        WifiManager wm =(WifiManager)context.getSystemService(WIFI_SERVICE);
        DhcpInfo di =wm.getDhcpInfo();
        long ip = di.gateway;
        StringBuffer sb =new StringBuffer();
        sb.append(String.valueOf((int)(ip&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)(ip>>8&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)(ip>>16&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)(ip>>24&0xff)));
        return sb.toString();
    }
    //需要在新线程下运行
    public  InetAddress SearchInverterHost()
    {

        // 第一步查询网关
        getAPaddress();
        if(getAPaddress().equals("10.10.100.253"))
        {
            // 说明网关地址就是逆变器host
            try {
                InverterHost = InetAddress.getByName("10.10.100.253");
            }
            catch (Exception ex)
            {

            }
            return InverterHost;
        }


        //否则进行广播
        try {

            //
            InetAddress host = InetAddress.getByName("255.255.255.255");

            DatagramSocket ss = new DatagramSocket();
            ss.setSoTimeout(3000);
            // 设置发送条目
            //byte[] mBytes = new byte[]{(byte) 0xaa, (byte) 0x55, (byte) 0xb0, (byte) 0x7F, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x30};

            String HexString = "574946494b49542d3231343032382d52454144";
            byte[] mBytes = hexStringToBytes(HexString);
            DatagramPacket request = new DatagramPacket(mBytes, mBytes.length,host,48899 );

            DatagramPacket response = new DatagramPacket(new byte[1024],1024);
            //发送
            ss.send(request);
            ss.receive(response);
            // 解析response;
            String str = new String(response.getData(),"ASCII");

            String resultStr =str.substring(0,str.indexOf(0x2c));

            //
            Log.d(tag,"IP is ." +resultStr);
            // to InetADDRESS;
            InverterHost =InetAddress.getByName(resultStr);
            return InverterHost;
        }
        catch (SocketTimeoutException ex)
        {
            Log.d(tag,"Time out .");
            Log.d(tag,"Try Again .");
            return SearchInverterHost();

        }
        catch (Exception ex) {
            ex.printStackTrace();
            Log.d(tag,"get Inverter Host Failure");

        }
        return null;
    }

    // hexStringToBytes
    public  byte[] hexStringToBytes(String s)
    {
        if(s==null||s.equals(""))
        {
            return null;
        }

        s=s.toUpperCase();
        int length =s.length()/2;
        char[] hexchars= s.toCharArray();
        byte[] d = new byte[length];
        for(int i=0;i<length;++i)
        {
            int pos = i*2;
            d[i]=(byte)(charToByte(hexchars[pos])<<4|charToByte(hexchars[pos+1]));
        }
        return  d;
    }

    //
    // 获取wifi的项目 需要打开gps功能
    public List<String> getWifiList()
    {

        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //wm.startScan();
        List<ScanResult> WifiList = wm.getScanResults();
        Log.e("getWifiList","WifiListsize:"+WifiList.size());
        List<String> SSIDlist = new ArrayList<>();
        for(int i =0;i<WifiList.size();++i)
        {
            SSIDlist.add(  WifiList.get(i).SSID);

        }
        return SSIDlist;
    }
    private  byte charToByte(char c)
    {
        return (byte)"0123456789ABCDEF".indexOf(c);
    }
}
