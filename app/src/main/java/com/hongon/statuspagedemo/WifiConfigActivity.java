package com.hongon.statuspagedemo;

import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hongon.statuspagedemo.R;
import com.hongon.statuspagedemo.Service.NetStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 配置逆变器的wifi功能
 *
 * 首先在该活动创建时检测本机的wifi是否打开，网关是否是10.10.100.253
 * 将结果打印在日志上作为提示。
 * 点击确定键后发送Wifi名称与密码，选择的默认加密方式为wpk，所以如果是极路由器是其他的加密方式会有问题。
 * 目前使用开放的http服务器进行路由器的配置。实际上可以选用UDP发送命令的方式，未来可以改进。
 * 当接收到返回200 ok 的报文，说明逆变器收到命令，开始进行复位，无论成功与否，都会关闭既有的solarWifi连接。
 * 因此此时可以提示用户跳转，或者提供转到wifi设置的页面。
 * Created by Admin on 2018/1/3.
 */

public class WifiConfigActivity extends AppCompatActivity {
    final String Tag = "WifiConfigActivity";

    //---

    TextView logView;
    //---
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_config_layout);
        Toolbar toolbar =  findViewById(R.id.wifi_config_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wifi配置界面");
        ActionBar T = getSupportActionBar();
        T.setDisplayHomeAsUpEnabled(true);
        //
        initEditText();
        //
        initNetStatus();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            // 实现按钮的返回功能
            case android.R.id.home:
                Toast.makeText(this,"homeAsUp Pressed.",Toast.LENGTH_SHORT).show();
                this.finish();
                return true;
            case R.id.home:
                Toast.makeText(this,"home Pressed.",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    //
    private void SendWifiConfig()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String ssid = ((EditText)findViewById(R.id.SSIDEdit)).getText().toString();
                    String passWord = ((EditText)findViewById(R.id.passwordEdit)).getText().toString();


                    OkHttpClient client = new OkHttpClient();

                    //
                    Request First =new Request.Builder()
                            .url("http://10.10.100.253/index.html")
                            .addHeader("Authorization", "Basic YWRtaW46YWRtaW4=")
                            .build();
                    Response response = client.newCall(First).execute();
                    Log.e(Tag,"get First head : "+response.toString());
                    if(response.isSuccessful())
                    {
                        Log.e(Tag,"First Respond Get ");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                logView.append("Wifi配置请求已成功发送\n");
                            }
                        });
                    }

                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                    RequestBody body = RequestBody.create(mediaType, "sta_ssid="+ssid+
                            "&sta_sec_mode=WPA-PSK&sta_sec_auth=AES&sta_psk="+passWord+"&module_cmd=finish");
                    Request request = new Request.Builder()
                            .url("http://10.10.100.253/finish_success.html")
                            .post(body)
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("content-length", ""+body.contentLength())
                            .addHeader("Authorization", "Basic YWRtaW46YWRtaW4=")
                            .addHeader("cache-control", "no-cache")
                            .build();

                    response = client.newCall(request).execute();

                    Log.e(Tag,"get Response head : "+response.toString());
                    if(response.isSuccessful())
                    {
                        String str =response.body().string();
                        Log.e(Tag,"get Response: "+str);
                        //下面这个函数要在
                        //可以写一个接口放在里面处理UI更新！
                        //Toast.makeText(WifiConfigActivity.this,"设置成功，请重启应用!",Toast.LENGTH_LONG).show();
                    }
                    Log.e(Tag,"result."+response.isSuccessful());
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        }).start();
        Log.e(Tag,"SendPost.");

    }



    //
    private void initEditText()
    {
        logView = findViewById(R.id.LogTextView);
        EditText ssidText = findViewById(R.id.SSIDEdit);
        EditText passWordText = findViewById(R.id.passwordEdit);
        ImageButton showWifiButton = findViewById(R.id.btn_ShowWifiList);
        ImageButton passVisualButton = findViewById(R.id.btn_passwordVisual);

        Button clearBtn = findViewById(R.id.buttonClear);
        Button submitBtn = findViewById(R.id.buttonSure);
        // 添加点击监听

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清除 两个Edit上的内容
                ssidText.getText().clear();
                passWordText.getText().clear();
            }
        });
        passVisualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"visualButton Pressed. "+passWordText.getInputType(),Toast.LENGTH_SHORT).show();
                passWordText.requestFocus();
                int location = passWordText.getSelectionEnd();
                if(passWordText.getTransformationMethod().getClass().equals(HideReturnsTransformationMethod.class))
                {
                    //改为密文显示

                    passWordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //改图标
                    passVisualButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_black_24dp,null));

                }
                else {

                    //改为明文显示

                    passWordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    //改图标
                    passVisualButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_black_24dp, null));
                }
                passWordText.setSelection(location);
            }
        });

        // 设置Dialog
        showWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    //Toast.makeText(WifiConfigActivity.this,"Button Pressed. ",Toast.LENGTH_SHORT).show();
                    // item 选项
                    List<String> itemSSID =netStatus.getWifiList();
                    Toast.makeText(WifiConfigActivity.this,"Button Pressed. "+itemSSID.size(),Toast.LENGTH_SHORT).show();
                    String items[] = new String[itemSSID.size()];
                    for (int i = 0; i < itemSSID.size(); ++i) {
                        items[i] = itemSSID.get(i);
                    }
                    //

                    AlertDialog.Builder builder = new AlertDialog.Builder(WifiConfigActivity.this);
                    builder.setTitle("WIFI列表");
                    builder.setIcon(R.drawable.ic_network_check_black_24dp);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ssidText.setText(items[i]);
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create().show();
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        // submit
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO SOMETHING
                SendWifiConfig();
            }
        });
    }

    //
    private NetStatus netStatus ;

    private void initNetStatus(){
        netStatus=NetStatus.getInstance(this);

        if(!netStatus.getAPaddress().equals("10.10.100.253")){
            logView.append("未连接至Solar Wifi。请检查网络.\n");
        }
    }
}
