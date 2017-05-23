package com.demo.cworker.Widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.cworker.R;
import com.demo.cworker.Utils.LogUtils;
import com.demo.cworker.Utils.Utils;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.gzfgeh.iosdialog.IOSDialog;

/**
 * Description:
 * Created by GUZHENFU on 2017/5/17 13:58.
 */

public class CustomDialog {
    private static class Holder{
        public final static CustomDialog INSTANCE = new CustomDialog();
    }

    public static CustomDialog getInstance(){
        return Holder.INSTANCE;
    }

    public static void show(Activity context, String url){
        Dialog dialog = new Dialog(context, R.style.AlertDialogStyle);
        Window win = dialog.getWindow();
        win.setWindowAnimations(R.style.dialogWindowAnim);
        WindowManager winManager = context.getWindowManager();
        Display display = winManager.getDefaultDisplay();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(display.getWidth()*0.9), (int)(display.getHeight()*0.5));
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_image, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        Glide.with(context).load(url).placeholder(R.drawable.ic_launcher_round).into(imageView);
        dialog.setContentView(view, params);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public static void showReboundDialog(Activity context, int resID, String text){
        Dialog dialog = new Dialog(context, R.style.AlertDialogStyle);
        WindowManager winManager = context.getWindowManager();
        Display display = winManager.getDefaultDisplay();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(display.getWidth()*0.8), (int)(display.getHeight()*0.6));
        View view = LayoutInflater.from(context).inflate(R.layout.rebound_dialog_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.pop_image);
        TextView textView = (TextView) view.findViewById(R.id.pop_tv);
        imageView.setImageResource(resID);
        textView.setText(text);
        SpringSystem springSystem = SpringSystem.create();
        SpringConfig springConfig = SpringConfig.fromOrigamiTensionAndFriction(60, 7);
        Spring spring = springSystem.createSpring();
        spring.setSpringConfig(springConfig);
        spring.addListener(new SimpleSpringListener(){
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value*0.5f);
                view.setScaleX(scale);
                view.setScaleY(scale);
            }
        });
        spring.setEndValue(1);

        dialog.setContentView(view, params);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    public static void showReboundPop(Context context, int resID, String text){
        View view = LayoutInflater.from(context).inflate(R.layout.rebound_dialog_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.pop_image);
        TextView textView = (TextView) view.findViewById(R.id.pop_tv);
        imageView.setImageResource(resID);
        textView.setText(text);
        SpringSystem springSystem = SpringSystem.create();
        SpringConfig springConfig = SpringConfig.fromOrigamiTensionAndFriction(60, 7);
        Spring spring = springSystem.createSpring();
        spring.setSpringConfig(springConfig);
        spring.addListener(new SimpleSpringListener(){
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                float value = (float) spring.getCurrentValue();
                view.setScaleX(value);
                view.setScaleY(value);
            }
        });
        spring.setEndValue(1);

        PopupWindow popupWindow = new PopupWindow(view, Utils.getScreenWidth(context)/2, Utils.getScreenHeight(context)/3, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        int x = Utils.getScreenWidth(context)/4;
        int y = Utils.getScreenHeight(context)/3;
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y);
    }


}
