package io.jchat.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import com.seafire.cworker.App;
import com.seafire.cworker.R;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.eventbus.EventBus;
import io.jchat.android.chatting.ChatActivity;
import io.jchat.android.controller.FriendInfoController;
import io.jchat.android.database.FriendEntry;
import io.jchat.android.entity.Event;
import io.jchat.android.chatting.utils.DialogCreator;
import io.jchat.android.chatting.utils.HandleResponseCode;
import io.jchat.android.entity.EventType;
import io.jchat.android.tools.NativeImageLoader;
import io.jchat.android.view.FriendInfoView;

public class FriendInfoActivity extends BaseActivity {

    private FriendInfoView mFriendInfoView;
    private FriendInfoController mFriendInfoController;
    private String mTargetId;
    private long mGroupId;
    private UserInfo mUserInfo;
    private String mTitle;
    private boolean mIsGetAvatar = false;
    private String mTargetAppKey;
    private boolean mIsFromContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        mFriendInfoView = (FriendInfoView) findViewById(R.id.friend_info_view);
        mTargetId = getIntent().getStringExtra(App.TARGET_ID);
        mTargetAppKey = getIntent().getStringExtra(App.TARGET_APP_KEY);
        if (mTargetAppKey == null) {
            mTargetAppKey = JMessageClient.getMyInfo().getAppKey();
        }
        mFriendInfoView.initModule();
        mFriendInfoController = new FriendInfoController(mFriendInfoView, this);
        mFriendInfoView.setListeners(mFriendInfoController);
        mFriendInfoView.setOnChangeListener(mFriendInfoController);
        mIsFromContact = getIntent().getBooleanExtra("fromContact", false);
        if (mIsFromContact) {
            updateUserInfo();
        } else {
            mGroupId = getIntent().getLongExtra(App.GROUP_ID, 0);
            Conversation conv;
            if (mGroupId == 0) {
                conv = JMessageClient.getSingleConversation(mTargetId, mTargetAppKey);
                mUserInfo = (UserInfo) conv.getTargetInfo();
            } else {
                conv = JMessageClient.getGroupConversation(mGroupId);
                GroupInfo groupInfo = (GroupInfo) conv.getTargetInfo();
                mUserInfo = groupInfo.getGroupMemberInfo(mTargetId, mTargetAppKey);
            }

            //先从Conversation里获得UserInfo展示出来
            mFriendInfoView.initInfo(mUserInfo);
            updateUserInfo();
        }
    }

    private void updateUserInfo() {
        //更新一次UserInfo
        final Dialog dialog = DialogCreator.createLoadingDialog(FriendInfoActivity.this,
                FriendInfoActivity.this.getString(R.string.jmui_loading));
        dialog.show();
        JMessageClient.getUserInfo(mTargetId, mTargetAppKey, new GetUserInfoCallback() {
            @Override
            public void gotResult(int status, String desc, final UserInfo userInfo) {
                dialog.dismiss();
                if (status == 0) {
                    mUserInfo = userInfo;
                    mTitle = userInfo.getNotename();
                    if (TextUtils.isEmpty(mTitle)) {
                        mTitle = userInfo.getNickname();
                    }
                    mFriendInfoView.initInfo(userInfo);
                } else {
                    HandleResponseCode.onHandle(FriendInfoActivity.this, status, false);
                }
            }
        });
    }

    /**
     * 如果是从群聊跳转过来，使用startActivity启动聊天界面，如果是单聊跳转过来，setResult然后
     * finish掉此界面
     */
    public void startChatActivity() {
        if (mIsFromContact) {
            Intent intent = new Intent(this, ChatActivity.class);
            String title = mUserInfo.getNotename();
            if (TextUtils.isEmpty(title)) {
                title = mUserInfo.getNickname();
                if (TextUtils.isEmpty(title)) {
                    title = mUserInfo.getUserName();
                }
            }
            intent.putExtra(App.CONV_TITLE, title);
            intent.putExtra(App.TARGET_ID, mUserInfo.getUserName());
            intent.putExtra(App.TARGET_APP_KEY, mUserInfo.getAppKey());
            startActivity(intent);
        } else {
            if (mGroupId != 0) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(App.TARGET_ID, mTargetId);
                intent.putExtra(App.TARGET_APP_KEY, mTargetAppKey);
                intent.setClass(this, ChatActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.putExtra("returnChatActivity", true);
                intent.putExtra(App.CONV_TITLE, mTitle);
                setResult(App.RESULT_CODE_FRIEND_INFO, intent);
            }
        }

        Conversation conv = JMessageClient.getSingleConversation(mTargetId, mTargetAppKey);
        //如果会话为空，使用EventBus通知会话列表添加新会话
        if (conv == null) {
            conv = Conversation.createSingleConversation(mTargetId, mTargetAppKey);
            EventBus.getDefault().post(new Event.Builder()
                    .setType(EventType.createConversation)
                    .setConversation(conv)
                    .build());
        }
        finish();
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public String getUserName() {
        return mUserInfo.getUserName();
    }

    public String getTargetAppKey() {
        return mTargetAppKey;
    }


    //点击头像预览大图
    public void startBrowserAvatar() {
        if (mUserInfo != null && !TextUtils.isEmpty(mUserInfo.getAvatar())) {
            if (mIsGetAvatar) {
                //如果缓存了图片，直接加载
                Bitmap bitmap = NativeImageLoader.getInstance().getBitmapFromMemCache(mUserInfo.getUserName());
                if (bitmap != null) {
                    Intent intent = new Intent();
                    intent.putExtra("browserAvatar", true);
                    intent.putExtra("avatarPath", mUserInfo.getUserName());
                    intent.setClass(this, BrowserViewPagerActivity.class);
                    startActivity(intent);
                }
            } else {
                final Dialog dialog = DialogCreator.createLoadingDialog(this, this.getString(R.string.jmui_loading));
                dialog.show();
                mUserInfo.getBigAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int status, String desc, Bitmap bitmap) {
                        if (status == 0) {
                            mIsGetAvatar = true;
                            //缓存头像
                            NativeImageLoader.getInstance().updateBitmapFromCache(mUserInfo.getUserName(), bitmap);
                            Intent intent = new Intent();
                            intent.putExtra("browserAvatar", true);
                            intent.putExtra("avatarPath", mUserInfo.getUserName());
                            intent.setClass(FriendInfoActivity.this, BrowserViewPagerActivity.class);
                            startActivity(intent);
                        } else {
                            HandleResponseCode.onHandle(FriendInfoActivity.this, status, false);
                        }
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode == App.RESULT_CODE_EDIT_NOTENAME) {
            mTitle = data.getStringExtra(App.NOTENAME);
            mFriendInfoView.setNoteName(mTitle);
            FriendEntry friend = FriendEntry.getFriend(App.getUserEntry(), mTargetId, mTargetAppKey);
            if (null != friend) {
                friend.displayName = mTitle;
                friend.save();
            }
        }
    }

    //将获得的最新的昵称返回到聊天界面
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(App.CONV_TITLE, mTitle);
        setResult(App.RESULT_CODE_FRIEND_INFO, intent);
        finish();
        super.onBackPressed();
    }

    public int getWidth() {
        return mWidth;
    }

    public void delConvAndReturnMainActivity() {
        Conversation conversation = JMessageClient.getSingleConversation(mTargetId, mTargetAppKey);
        EventBus.getDefault().post(new Event.Builder().setType(EventType.deleteConversation)
                .setConversation(conversation)
                .build());
        JMessageClient.deleteSingleConversation(mTargetId, mTargetAppKey);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
