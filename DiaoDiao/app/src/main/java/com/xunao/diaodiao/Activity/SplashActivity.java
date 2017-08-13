package com.xunao.diaodiao.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.MainActivity;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxUtils;
import com.xunao.diaodiao.Utils.ShareUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

public class SplashActivity extends AppCompatActivity {

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        subscription = Observable.timer(2, TimeUnit.SECONDS)
                .compose(RxUtils.applyIOToMainThreadSchedulers())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {
                        goToNextActivity();
                        return aLong;
                    }
                }).subscribe();
    }


    private void goToNextActivity(){
        boolean isComeOver = ShareUtils.getValue(Constants.IS_COME_OVER, false);
        if (isComeOver){
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }else{
            startActivity(new Intent(SplashActivity.this, LaunchActivity.class));
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            System.exit(0);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
            subscription = null;
        }
    }

}
