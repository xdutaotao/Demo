package io.jchat.android.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seafire.cworker.Activity.*;
import com.seafire.cworker.R;
import com.seafire.cworker.Utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import io.jchat.android.chatting.utils.DialogCreator;
import io.jchat.android.chatting.utils.HandleResponseCode;

public class CreateGroupNameActivity extends com.seafire.cworker.Activity.BaseActivity {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.pwd)
    EditText pwd;

    private Dialog mDialog;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, CreateGroupNameActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_name);
        ButterKnife.bind(this);

        showToolbarBack(toolBar, titleText, "创建群聊");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:

                String nickName = pwd.getText().toString();
                if (!TextUtils.isEmpty(nickName)){
                    ToastUtil.show("群名称不能为空");
                }else{
                    mDialog = DialogCreator.createLoadingDialog(CreateGroupNameActivity.this, CreateGroupNameActivity.this.getString(R.string.modifying_hint));
                    mDialog.show();
                    UserInfo myUserInfo = JMessageClient.getMyInfo();
                    myUserInfo.setNickname(nickName);
                    JMessageClient.updateMyInfo(UserInfo.Field.nickname, myUserInfo, new BasicCallback() {
                        @Override
                        public void gotResult(final int status, final String desc) {
                            mDialog.dismiss();
                            if (status == 0) {
                                ToastUtil.show(R.string.modify_success_toast);
                                Intent intent = new Intent();
                                intent.putExtra("nickName", nickName);
                                setResult(0, intent);
                                finish();
                            } else {
                                HandleResponseCode.onHandle(CreateGroupNameActivity.this, status, false);
                            }
                        }
                    });
                }


                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
