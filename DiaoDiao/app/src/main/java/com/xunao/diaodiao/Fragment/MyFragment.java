package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xunao.diaodiao.Activity.AboutActivity;
import com.xunao.diaodiao.Activity.CheckPhoneActivity;
import com.xunao.diaodiao.Activity.CollectHistoryActivity;
import com.xunao.diaodiao.Activity.HelpActivity;
import com.xunao.diaodiao.Activity.LoginActivity;
import com.xunao.diaodiao.Activity.PersonalActivity;
import com.xunao.diaodiao.Activity.SelectMemoryActivity;
import com.xunao.diaodiao.Activity.SettingActivity;
import com.xunao.diaodiao.Activity.SuggestActivity;
import com.xunao.diaodiao.Activity.VIPActivity;
import com.xunao.diaodiao.Bean.CollectBean;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Model.UserInfo;
import com.xunao.diaodiao.Present.MyPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.JsonUtils;
import com.xunao.diaodiao.Utils.NetWorkUtils;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.MyView;
import com.xunao.diaodiao.Widget.CustomDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.xunao.diaodiao.Common.Constants.COLLECT_LIST;

public class MyFragment extends BaseFragment implements View.OnClickListener, MyView {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    @Inject
    MyPresenter presenter;

    public static MyFragment newInstance(String param1) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        return view;
    }

    @Override
    public void updateData() {
        super.updateData();
        onResume();
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onFailure() {

    }

    @Override
    public void getData(String data) {
        ToastUtil.show("退出成功");
    }

    @Override
    public void signToday(String bean) {
        ToastUtil.show("签到成功");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

}
