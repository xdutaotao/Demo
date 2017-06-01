package com.kii.tutk;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yankon.smart.App;
import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;
import com.yankon.smart.fragments.ProgressDialogFragment;
import com.yankon.smart.fragments.WarningSingleBtnDialogFragment;
import com.yankon.smart.model.Event;
import com.yankon.smart.utils.Global;
import com.yankon.smart.utils.LogUtils;
import com.yankon.smart.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Evan on 15/11/22.
 */
public class TUTKVideoActivity extends BaseActivity implements ProgressDialogFragment.CancleIOTCListener, WarningSingleBtnDialogFragment.WarningSingleDialogInterface {

    public static final String EXTRA_UID = "uid";
    @Bind(R.id.surface)
    TUTKVideoView surface;
    @Bind(R.id.video_bg)
    ImageView videoBg;

    private ProgressDialogFragment dialogFragment;
    String UID = "XNKZ55AMB8LJE1HW111A";
    private boolean isDialogShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_video_info);
        ButterKnife.bind(this);
        surface = (TUTKVideoView) findViewById(R.id.surface);
        UID = getIntent().getStringExtra(EXTRA_UID);
        surface.init(UID);
        showLoadingDialog();
        EventBus.getDefault().register(this);

//        if (Global.lastBuffer != null) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(Global.lastBuffer, 0, Global.lastBuffer.length);
//            videoBg.setImageBitmap(bitmap);
//        } else {
//            videoBg.setImageResource(R.drawable.pro_bg);
//        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if ((dialogFragment == null) && (!isDialogShow)) {
            surface.stop();
            surface.start();
            showLoadingDialog();
            LogUtils.i("TAG", "mSurfaceView------start--------");
        }
    }

    public void onEventMainThread(Event.VideoEvent event) {
        switch (event.getType()) {
            case TUTKVideoView.DISPLAY:
                if (dialogFragment != null) {
                    dialogFragment.dismiss();
                    dialogFragment = null;
                    videoBg.setVisibility(View.GONE);
                }
                break;

            case TUTKVideoView.DISCONNECT:
                showWarningDialog(event.getType());
                LogUtils.i("TAG", "MSG_HANDLE_START_FAIL------start--------");
                break;

            case TUTKVideoView.ERROE:
                Toast.makeText(App.getApp(), getString(R.string.check), Toast.LENGTH_SHORT).show();
                LogUtils.i("TAG", "MSG_HANDLE_ERROR------start--------");
                finish();
                break;
        }
    }

    void showWarningDialog(int type) {
        String title = getString(R.string.check);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack("dialog");
        WarningSingleBtnDialogFragment newFragment = WarningSingleBtnDialogFragment.newInstance(title, type);
        newFragment.setWarningDialogInterface(this);
        newFragment.show(getFragmentManager(), "dialog");
        isDialogShow = true;
    }

    @Override
    public void onWarningDialogDone(int type) {
        isDialogShow = false;
        finish();
    }

    private void showLoadingDialog() {
        dialogFragment = ProgressDialogFragment.newInstance(null, getString(R.string.loading));
        surface.setListenIOTCTime(dialogFragment);
        dialogFragment.setCancleIOTCListener(this);
        dialogFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.i("TAG", "onConfigurationChanged--------------");
    }

    @Override
    protected void onPause() {
        super.onPause();
        surface.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surface.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        surface.onDestory();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCancleIOTCListener() {

    }
}
