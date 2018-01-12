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
                        Thread.sleep(3000);
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
    NetStatus netStatus = NetStatus.getInstance(this);

}
