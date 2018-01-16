package com.xunao.diaodiao.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xunao.diaodiao.Fragment.HomeFragment;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.FileUtils;
import com.xunao.diaodiao.Utils.PermissionsUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Widget.DownloadDialog.DownloadDialogFactory;
import com.xunao.diaodiao.Widget.GlideImageLoader;
import com.xunao.diaodiao.Widget.PhotoView.PhotoView;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

public class PhotoActivity extends BaseActivity {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.image_view)
    PhotoView imageView;

    String path;
    boolean isNet;
    Bitmap bitmap ;

    public static void startActivity(Context context, String path, boolean isNet) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(INTENT_KEY, path);
        intent.putExtra("isNet", isNet);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        getActivityComponent().inject(this);
        showToolbarBack(toolBar, titleText, "详情");

        path = getIntent().getStringExtra(INTENT_KEY);
        isNet = getIntent().getBooleanExtra("isNet", false);

        imageView.enable();
        if(isNet){
            Glide.with(this)                             //配置上下文
                    .load(path)      //设置图片路径
                    .error(R.drawable.zhanwei)           //设置错误图片
                    .placeholder(R.drawable.zhanwei)     //设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                    .into(imageView);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        bitmap = Glide.with(PhotoActivity.this).load(path).asBitmap().into(500, 500).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }).start();


        }else{
            GlideImageLoader imageLoader = new GlideImageLoader();

            toolBar.post(new Runnable() {
                @Override
                public void run() {
                    imageLoader.displayImage(PhotoActivity.this, path, imageView, imageView.getWidth(), imageView.getHeight());
                }
            });


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collect, menu);
        MenuItem item = menu.findItem(R.id.action_contact);
        item.setTitle("保存");
        item.setVisible(isNet);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                if (!PermissionsUtils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    return true;
                }

                boolean isSuccess = FileUtils.saveImageToGallery(this, bitmap);
                if(isSuccess){
                    ToastUtil.show("保存成功");
                }else{
                    ToastUtil.show("保存失败");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // TODO: 2016/12/26 grantResults[0]
        if (grantResults != null && grantResults.length > 0) {
            switch (requestCode) {
                //权限申请成功
                case PermissionsUtils.WRITE_EXTERNAL_STORAGE_CODE:
                    FileUtils.saveImageToGallery(this, bitmap);
                    break;
            }
        } else {
            PermissionsUtils.permissionNotice(this, requestCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap != null){
            bitmap.recycle();
            bitmap = null;
        }

    }
}
