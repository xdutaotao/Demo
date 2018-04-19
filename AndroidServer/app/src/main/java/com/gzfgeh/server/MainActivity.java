package com.gzfgeh.server;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProxyD p = new ProxyD("127.0.0.1", 8080);
        try {
            p.start();
        } catch (IOException e) {

        }
    }


}
