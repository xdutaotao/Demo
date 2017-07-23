package com.seafire.cworker.Activity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seafire.cworker.R;
import com.seafire.cworker.Utils.FileUtils;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.Widget.camera.CameraSurfaceView;
import com.lzy.imagepicker.bean.ImageItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seafire.cworker.Common.Constants.INTENT_KEY;

public class CameraActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.camera)
    CameraSurfaceView camera;
    @BindView(R.id.left_tv)
    TextView leftTv;
    @BindView(R.id.center_tv)
    TextView centerTv;
    @BindView(R.id.take_photo)
    ImageView takePhoto;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.all_num)
    TextView allNum;

    private int preNum = 0, currentNum = 0;
    private ArrayList<ImageItem> paths = new ArrayList<>();

    public static void startActivityForResult(Activity context, int num){
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra(INTENT_KEY, num);
        context.startActivityForResult(intent, CollectActivity.REQUEST_TAKE_PHOTO_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        preNum = getIntent().getIntExtra(INTENT_KEY, 0);
        takePhoto.setOnClickListener(this);
        rightTv.setOnClickListener(this);
        centerTv.setOnClickListener(this);
        leftTv.setOnClickListener(this);
    }

    //创建jpeg图片回调数据对象
    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            //获取拍照的图像
            String name = System.currentTimeMillis()+".jpg";
            String s = FileUtils.writeFile(data, "cwork",name);
            if (!TextUtils.isEmpty(s)){
                ImageItem item = new ImageItem();
                item.path = s;
                item.name = name;
                item.addTime = System.currentTimeMillis();
                item.mimeType = "jpg";
                item.width = 540;
                item.height = 500;
                item.size = data.length;
                paths.add(item);
                setAfterTakeView();
            }

        }
    };

    private void setAfterTakeView() {
        currentNum++;
        leftTv.setText("重拍");
        centerTv.setText("下一张");
        rightTv.setText("完成"+currentNum+"张");
        centerTv.setVisibility(View.VISIBLE);
        rightTv.setVisibility(View.VISIBLE);
        takePhoto.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.take_photo:
                if (preNum + paths.size() >= 10){
                    ToastUtil.show("超出最大数！");
                    return;
                }
                camera.takePicture(jpeg);
                break;

            case R.id.right_tv:
                Intent intent = new Intent();
                intent.putExtra(INTENT_KEY, paths);
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.center_tv:
                centerTv.setVisibility(View.GONE);
                takePhoto.setVisibility(View.VISIBLE);
                camera.startPreview();
                break;

            case R.id.left_tv:
                if(TextUtils.equals(leftTv.getText().toString(), "取消")){
                    finish();
                }else{
                    leftTv.setText("取消");
                    takePhoto.setVisibility(View.VISIBLE);
                    centerTv.setVisibility(View.GONE);
                    currentNum--;
                    rightTv.setText("完成"+currentNum+"张");
                    paths.remove(paths.size()-1);
                    camera.startPreview();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jpeg = null;
    }
}
