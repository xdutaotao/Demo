package com.yankon.smart.model;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.winnermicro.smartconfig.ConfigType;
import com.winnermicro.smartconfig.ISmartConfig;
import com.winnermicro.smartconfig.OneShotException;
import com.winnermicro.smartconfig.SmartConfigFactory;
import com.yankon.smart.App;
import com.yankon.smart.R;
import com.yankon.smart.utils.LogUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by sunup_002 on 2015/12/17.
 */
public class NewBuildNetworkThread extends Thread {
    private Context mContext;
    private String mPass;
    private Handler handler;
    private Boolean isThreadDisable = false;//指示监听线程是否终止
    private ISmartConfig smartConfig = null;
    private SmartConfigFactory factory = null;
    private int errorId = 0;
    private UdpHelper udphelper;
    private Thread tReceived;
    private boolean isStart = false;

    public NewBuildNetworkThread(Context context, String pass){
        this.mContext = context;
        this.mPass = pass;
        this.handler = new Handler();

        factory = new SmartConfigFactory();
        //通过修改参数ConfigType，确定使用何种方式进行一键配置，需要和固件侧保持一致。
        smartConfig = factory.createSmartConfig(ConfigType.UDP, context);
    }

    @Override
    public void run() {
        super.run();
        isStart = true;
        isThreadDisable = false;
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        udphelper = new UdpHelper(wifiManager);
        tReceived = new Thread(udphelper);
        tReceived.start();
        new Thread(new UDPReqThread()).start();
    }

    @Override
    public void interrupt() {
        super.interrupt();
        LogUtils.i("NewBuildNetworkThread","STOP");
        stopConfig();
    }

    class UDPReqThread implements Runnable {
        public void run() {
            WifiManager wifiManager = null;
            errorId = 0;
            try {
                wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                if(wifiManager.isWifiEnabled())
                {
                    while(isStart){
                        if(smartConfig.startConfig(mPass) == false)
                        {
                            break;
                        }
                        Thread.sleep(10);
                    }
                }
            }
            catch (OneShotException oe) {
                oe.printStackTrace();
                errorId = oe.getErrorID();
                if(oe.getErrorID() == OneShotException.ERROR_NETWORK_NOT_SUPPORT){
                    //displayToast("不支持该网络!");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally{
                handler.post(confPost);
            }
        }

    }


    class UdpHelper implements Runnable {

        private WifiManager.MulticastLock lock;
        InetAddress mInetAddress;
        public UdpHelper(WifiManager manager) {
            this.lock= manager.createMulticastLock("UDPwifi");
        }
        public void StartListen()  {
            // UDP服务器监听的端口
            Integer port = 65534;
            // 接收的字节大小，客户端发送的数据不能超过这个大小
            byte[] message = new byte[100];
            try {
                // 建立Socket连接
                DatagramSocket datagramSocket = new DatagramSocket(port);
                datagramSocket.setBroadcast(true);
                datagramSocket.setSoTimeout(1000);
                DatagramPacket datagramPacket = new DatagramPacket(message,
                        message.length);
                try {
                    while (!isThreadDisable) {
                        // 准备接收数据
                        Log.d("UDP Demo", "准备接受");
                        this.lock.acquire();
                        try{
                            datagramSocket.receive(datagramPacket);
                            String strMsg="";
                            int count = datagramPacket.getLength();
                            for(int i=0;i<count;i++){
                                strMsg += String.format("%02x", datagramPacket.getData()[i]);
                            }
                            strMsg = strMsg.toUpperCase() + ";" + datagramPacket.getAddress().getHostAddress().toString();
                        }
                        catch(SocketTimeoutException ex){
                            Log.d("UDP Demo", "UDP Receive Timeout.");
                        }
                        this.lock.release();
                    }
                } catch (IOException e) {//IOException
                    e.printStackTrace();
                }
                datagramSocket.close();
            } catch (SocketException e) {
                e.printStackTrace();
            } finally{
                if(!isThreadDisable){
                    handler.post(confPost);
                }
            }
        }
        @Override
        public void run() {
            StartListen();
        }

    }

    private Runnable confPost = new Runnable(){

        @Override
        public void run() {
            isStart=false;
            isThreadDisable = true;
            smartConfig.stopConfig();
            if(errorId == OneShotException.ERROR_NETWORK_NOT_SUPPORT){
                Toast.makeText(App.getApp(), App.getApp().getString(R.string.no_support_net), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void stopConfig(){
        isThreadDisable = true;
        isStart = false;
        smartConfig.stopConfig();
        handler.removeCallbacks(confPost);
    }
}
