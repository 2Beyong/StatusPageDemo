package com.hongon.statuspagedemo.Service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Xml;

import com.hongon.statuspagedemo.DataModel.Datagram;
import com.hongon.statuspagedemo.DataModel.ResponseID;
import com.hongon.statuspagedemo.DataModel.ResponseRunningData;
import com.hongon.statuspagedemo.MainActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 本服务用于查询
 * Created by CoCO on 2017/12/27.
 */

public class DataQueryService extends Service{
    final String  tag ="DataQueryService";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(tag,"创建服务");

    }

    // 定义flag;
    boolean isQuerying =true;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(tag,"启动服务");
        //在另一个线程执行该操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                netStatus.SearchInverterHost();
                UDPTest();
                while (isQuerying == true) {
                    QueryRunningData();
                    try {
                        Thread.sleep(5000);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }
            }
        }).start();
        //执行完后关闭。

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        Log.d(tag,"关闭服务");
        //
        isQuerying =false;

        super.onDestroy();
    }


    private final IBinder binder = new myBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.d(tag,"关闭服务");
        return binder;
    }
    private MainActivity mainActivity;
    public class myBinder extends Binder
    {
        public DataQueryService getService()
        {
            return DataQueryService.this;
        }

        public void setMainActivity(MainActivity m)
        {
            mainActivity = m;
        }
    }
    //--------- UDP
    public void UDPTest() {

        try {
            //要开权限！傻逼
            InetAddress host = netStatus.InverterHost;
            int port = 8899;

            DatagramSocket ss = new DatagramSocket();
            ss.setSoTimeout(3000);
            // 设置发送条目
            byte[] mBytes = new byte[]{(byte) 0xaa, (byte) 0x55, (byte) 0xb0, (byte) 0x7F, (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x02, (byte) 0x31};

            DatagramPacket request = new DatagramPacket(mBytes, mBytes.length,host,port );

            DatagramPacket response = new DatagramPacket(new byte[1024],1024);
            //发送
            ss.send(request);
            ss.receive(response);

            //处理返回消息
            Log.d(tag,"get"+response.getData().toString());
            //Log.d(tag,"getRespondRunningData"+response.getData().toString());
            Datagram datagram = new Datagram(response);

            //调用接口,进行界面更新
            if(onIDReceivedListener!=null)
            onIDReceivedListener.OnIDReceived(datagram);


        }
        catch (SocketTimeoutException ex)
        {
            Log.d(tag,"Time out .");
            Log.d(tag,"Try Again .");
            UDPTest();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            Log.d(tag,"get information Failure");

        }
    }


    public void QueryRunningData() {

        try {
            //要开权限！傻逼
            InetAddress host = netStatus.InverterHost;
            int port = 8899;
            DatagramSocket ss = new DatagramSocket();
            ss.setSoTimeout(3000);
            // 设置发送条目
            byte[] mBytes = new byte[]{(byte) 0xaa, (byte) 0x55, (byte) 0xb0, (byte) 0x7F, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x30};


            DatagramPacket request = new DatagramPacket(mBytes, mBytes.length,host,port );

            DatagramPacket response = new DatagramPacket(new byte[1024],1024);
            //发送
            ss.send(request);
            ss.receive(response);

            //处理返回消息

            Datagram datagram = new Datagram(response);


            if(onRunningDataReceivedListener.size()!=0)
                for(int i =0;i<onRunningDataReceivedListener.size();++i){
                    onRunningDataReceivedListener.get(i).OnRunningDataReceived(datagram);
                }

        }
        catch (SocketTimeoutException ex)
        {
            Log.d(tag,"Time out .");
            Log.d(tag,"Try Again .");
            QueryRunningData();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            Log.d(tag,"get information Failure");

        }
    }

    // 接口

    public interface OnIDReceivedListener
    {
        void OnIDReceived(Datagram datagram);
    }

    private  OnIDReceivedListener onIDReceivedListener;
    public void setOnIDReceivedListener(OnIDReceivedListener onIDReceivedListener)
    {
        this.onIDReceivedListener = onIDReceivedListener;
    }

    public interface OnRunningDataReceivedListener
    {
        void OnRunningDataReceived(Datagram datagram);
    }

    private List<OnRunningDataReceivedListener> onRunningDataReceivedListener = new ArrayList<>();
    public void addOnRunningDataReceivedListener(OnRunningDataReceivedListener onRunningDataReceivedListener)
    {
        this.onRunningDataReceivedListener.add(onRunningDataReceivedListener);
    }

    // 网络信息处理
    NetStatus netStatus = new NetStatus();
    public  class NetStatus {

        static final String tag = "NetStatus";
        public InetAddress InverterHost ;
        // 获取网关地址
        public  String getAPaddress()
        {
            WifiManager wm =(WifiManager)getSystemService(WIFI_SERVICE);
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
        private  byte charToByte(char c)
        {
            return (byte)"0123456789ABCDEF".indexOf(c);
        }
    }
}
