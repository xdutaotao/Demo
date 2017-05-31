package com.yankon.smart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.yankon.smart.MainActivity;
import com.yankon.smart.R;
import com.yankon.smart.utils.Global;

/**
 * Created by guzhenfu on 2015/8/17.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_splash, null);
        setContentView(view);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f, 1.0f);
        alphaAnimation.setDuration(3000);
        view.startAnimation(alphaAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!Global.kiiInited) {
                    startActivity(new Intent(SplashActivity.this, ChooseSiteActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 3000);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
