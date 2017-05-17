package com.demo.cworker.Widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.demo.cworker.R;
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
}
