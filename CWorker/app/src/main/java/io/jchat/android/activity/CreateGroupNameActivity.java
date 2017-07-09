package io.jchat.android.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seafire.cworker.Activity.*;
import com.seafire.cworker.App;
import com.seafire.cworker.R;
import com.seafire.cworker.Utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import io.jchat.android.chatting.ChatActivity;
import io.jchat.android.chatting.utils.DialogCreator;
import io.jchat.android.chatting.utils.HandleResponseCode;
import io.jchat.android.controller.ConversationListController;

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
                if (TextUtils.isEmpty(nickName)){
                    ToastUtil.show("群名称不能为空");
                }else{

                    mDialog = DialogCreator.createLoadingDialog(CreateGroupNameActivity.this,
                            getString(R.string.creating_hint));
                    mDialog.show();
                    JMessageClient.createGroup("", "", new CreateGroupCallback() {

                        @Override
                        public void gotResult(final int status, String msg, final long groupId) {
                            mDialog.dismiss();
                            if (status == 0) {
                                Conversation conv = Conversation.createGroupConversation(groupId);
                                //mController.getAdapter().setToTop(conv);

                                JMessageClient.updateGroupName(groupId, nickName, new BasicCallback() {
                                    @Override
                                    public void gotResult(final int status, final String desc) {
                                        mDialog.dismiss();
                                        if (status == 0) {
                                            Intent intent = new Intent();
                                            //设置跳转标志
                                            intent.putExtra("fromGroup", true);
                                            intent.putExtra(App.MEMBERS_COUNT, 1);
                                            intent.putExtra(App.GROUP_ID, groupId);
                                            intent.setClass(CreateGroupNameActivity.this, ChatActivity.class);
                                            CreateGroupNameActivity.this.startActivity(intent);
                                        } else {

                                            HandleResponseCode.onHandle(CreateGroupNameActivity.this, status, false);
                                        }
                                    }
                                });


                            } else {
                                HandleResponseCode.onHandle(CreateGroupNameActivity.this, status, false);
                                Log.i("CreateGroupController", "status : " + status);
                            }
                        }
                    });

                }


                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
