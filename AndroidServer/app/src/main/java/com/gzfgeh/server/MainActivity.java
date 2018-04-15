package com.gzfgeh.server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.filter.HttpCacheFilter;
import com.yanzhenjie.andserver.website.AssetsWebsite;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button startServerBtn;
    TextView serverLog;
    Server mServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startServerBtn = findViewById(R.id.start_server);
        serverLog = findViewById(R.id.server_log);
        startServerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            mServer = AndServer.serverBuilder()
                    .inetAddress(NetUtils.getLocalIPAddress()) // Bind IP address.
                    .port(8080)
                    .timeout(10, TimeUnit.SECONDS)
                    .website(new AssetsWebsite(getAssets(), "web"))
                    .filter(new HttpCacheFilter())
                    .registerHandler("/", new DataHandler())
                    .listener(mListener)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Server.ServerListener mListener = new Server.ServerListener() {
        @Override
        public void onStarted() {
            Log.d("main", "onStarted");
            startServerBtn.setText("启动成功");
        }

        @Override
        public void onStopped() {
            Log.d("main", "onStopped");
            startServerBtn.setText("启动结束");
        }

        @Override
        public void onError(Exception e) {
            Log.d("main", e.getMessage());
            startServerBtn.setText("启动出错");
        }
    };

}
