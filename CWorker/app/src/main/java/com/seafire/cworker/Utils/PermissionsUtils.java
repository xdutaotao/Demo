package com.seafire.cworker.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.seafire.cworker.R;


/**
 * Created by  zhangerjiang
 */
public class PermissionsUtils {

    public final static int RECORD_AUDIO_PERMISSION_CODE = 0;
    public final static int CAMERA_PERMISSION_CODE = 1;
    public final static int WRITE_EXTERNAL_STORAGE_CODE = 2;
    public final static int CALL_PHONE_CODE = 3;
    public final static int READ_EXTERNAL_STORAGE_CODE = 4;
    public final static int WRITE_SETTINGS_CODE = 5;

    /**
     * Activity 中申请权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasPermission(Activity context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限,判断是否会弹权限申请对话框
            boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(context, permission);

            if (shouldShow) {
                // show an explanation why we need this permission
                permissionNotice(context, getPermissionCode(permission));
                // a custom dialog should be created here to tell user give the permission
                ActivityCompat.requestPermissions(context, new String[]{permission},
                        getPermissionCode(permission));
            } else {
                // 申请权限
                ActivityCompat.requestPermissions(context, new String[]{permission},
                        getPermissionCode(permission));
            }

            return false;
        }
        return true;
    }

    /**
     * Fragment  申请权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasPermission(Fragment context, String permission) {
        if (ContextCompat.checkSelfPermission(context.getContext(), permission)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限,判断是否会弹权限申请对话框
            boolean shouldShow = context.shouldShowRequestPermissionRationale(permission);

            if (shouldShow) {
                // show an explanation why we need this permission
                permissionNotice(context.getActivity(), getPermissionCode(permission));
                // a custom dialog should be created here to tell user give the permission
                context.requestPermissions(new String[]{permission},
                        getPermissionCode(permission));
            } else {
                // 申请权限
                context.requestPermissions(new String[]{permission},
                        getPermissionCode(permission));
            }

            return false;
        }
        return true;
    }


    public static int getPermissionCode(String permission) {
        switch (permission) {
            case Manifest.permission.RECORD_AUDIO:
                return 0;
            case Manifest.permission.CAMERA:
                return 1;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return 2;
            case  Manifest.permission.CALL_PHONE:
                return 3;
            case  Manifest.permission.READ_EXTERNAL_STORAGE:
                return 4;
            case  Manifest.permission.WRITE_SETTINGS:
                return 5;
            default:
                return -1;
        }
    }

    public static void permissionNotice(Activity context, int code) {
        switch (code) {
            case RECORD_AUDIO_PERMISSION_CODE:
                ToastUtil.show(String.format(context.getString(R.string.need_permition), context.getString(R.string.need_permition_record_audio)));
                break;
            case CAMERA_PERMISSION_CODE:
                ToastUtil.show(String.format(context.getString(R.string.need_permition), context.getString(R.string.need_permition_camera)));
                break;
            case WRITE_EXTERNAL_STORAGE_CODE:
                ToastUtil.show(String.format(context.getString(R.string.need_permition), context.getString(R.string.need_permition_write_external_storage)));
                break;
            case CALL_PHONE_CODE:
                ToastUtil.show(String.format(context.getString(R.string.need_permition), context.getString(R.string.need_permition_call_phone)));
                break;
            case READ_EXTERNAL_STORAGE_CODE:
                ToastUtil.show(String.format(context.getString(R.string.need_permition), context.getString(R.string.need_permition_write_external_storage)));
                break;
            case WRITE_SETTINGS_CODE:
                ToastUtil.show(String.format(context.getString(R.string.need_permition), context.getString(R.string.need_permition_setting)));
                break;
        }
    }
}
