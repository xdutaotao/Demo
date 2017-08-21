package com.seafire.cworker.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seafire.cworker.R;
import com.seafire.cworker.Utils.RxUtils;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.Widget.GlideImageLoader;
import com.gzfgeh.iosdialog.IOSDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

import static com.seafire.cworker.Activity.CollectActivity.REQUEST_SCAN_CODE;
import static com.seafire.cworker.Common.Constants.INTENT_KEY;

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {
    private static final int IMAGE_PICKER = 8888;

    @BindView(R.id.zxingview)
    ZXingView zxingview;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.open_light)
    ImageView openLight;

    private boolean isOpened = false;
    private Subscription subscription;

    public static void startActivityForResult(Activity context){
        Intent intent = new Intent(context, ScanActivity.class);
        context.startActivityForResult(intent, REQUEST_SCAN_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "二维码/条形码");

        zxingview.setDelegate(this);
        openLight.setOnClickListener(v -> {
            if (!isOpened){
                zxingview.openFlashlight();
                isOpened = true;
                openLight.setImageResource(R.drawable.turn_off);
            }else{
                isOpened = false;
                zxingview.closeFlashlight();
                openLight.setImageResource(R.drawable.turn_on);
            }

        });

        zxingview.startCamera();
        zxingview.showScanRect();
        zxingview.startSpot();

        zxingview.postDelayed(new Runnable() {
            @Override
            public void run() {
                zxingview.setVisibility(View.VISIBLE);
            }
        }, 500);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null){
            subscription.unsubscribe();
            subscription = null;
        }
        zxingview.stopCamera();
        zxingview.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);

        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtil.show("打开相机出错");
    }

    private void selectPhoto(){
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setCrop(true);
        imagePicker.setSaveRectangle(true);
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setFocusWidth(600);
        imagePicker.setFocusHeight(600);
        imagePicker.setShowCamera(false);
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                scanPhotoPath(images.get(0).path);
            } else {
                ToastUtil.show("没有数据");
            }
        }
    }

    private void scanPhotoPath(String path){
        subscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(QRCodeDecoder.syncDecodeQRCode(path));
                subscriber.onCompleted();
            }
        }).compose(RxUtils.applyIOToMainThreadSchedulers())
        .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                if (TextUtils.isEmpty(s)){
                    new IOSDialog(ScanActivity.this).builder()
                            .setTitle("扫描结果")
                            .setMsg("没有扫描到有效二维码")
                            .setNegativeButton("确定", null)
                            .show();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra(INTENT_KEY, s);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_photo:
                selectPhoto();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
