package com.seafire.cworker.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.iosdialog.IOSDialog;
import com.gzfgeh.swipeheader.SwipeRefreshLayout;
import com.seafire.cworker.Bean.FreindBean;
import com.seafire.cworker.Bean.FriendProBean;
import com.seafire.cworker.Bean.Item;
import com.seafire.cworker.Present.FriendPresenter;
import com.seafire.cworker.R;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.View.FriendView;
import com.seafire.cworker.adapters.ItemClickListener;
import com.seafire.cworker.adapters.Section;
import com.seafire.cworker.adapters.SectionedExpandableLayoutHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends BaseFragment implements ItemClickListener, FriendView, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.type_image)
    ImageView typeImage;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.cancle_action)
    ImageView cancleAction;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.result_recycler_view)
    RecyclerView resultRecyclerView;

    @Inject
    FriendPresenter presenter;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    private SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;
    private List<FreindBean> freindBeanList = new ArrayList<>();

    private RecyclerArrayAdapter<FriendProBean> adapter;
    private List<FriendProBean> resultData = new ArrayList<>();

    public static FriendFragment newInstance() {
        FriendFragment fragment = new FriendFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(getContext(),
                recyclerView, this);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == EditorInfo.IME_ACTION_SEARCH) || (keyEvent != null) && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    doSearchAction(textView.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)){
                    resultData.clear();
                    adapter.clear();
                    return;
                }
                if (freindBeanList.size() != 0) {
                    resultData.clear();
                    adapter.clear();
                    for (FreindBean freindBean: freindBeanList){
                        for (FreindBean.UsersBean bean: freindBean.getUsers()){
                            if (bean.getName().contains(editText.getText())){
                                resultData.add(changeBean(bean, freindBean.getName()));
                            }
                        }
                    }
                    adapter.addAll(resultData);
                    back.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    resultRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        back.setOnClickListener(v -> {
            resultRecyclerView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
            closeSoftKeyboard(editText, getContext());
        });

        adapter = new RecyclerArrayAdapter<FriendProBean>(getContext(), R.layout.layout_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, FriendProBean usersBean) {
                baseViewHolder.setImageUrl(R.id.head_icon, usersBean.getFace());
                baseViewHolder.setText(R.id.text_item, usersBean.getName()+(usersBean.getType() != 0?" -- 管理员":""));
            }
        };

        adapter.setOnItemClickListener((view1, i) -> {
            showDialog(resultData.get(i));
        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        resultRecyclerView.setLayoutManager(manager);
        resultRecyclerView.setAdapter(adapter);

        swipe.setOnRefreshListener(this);
        onRefresh();
        return view;
    }

    private void doSearchAction(String s) {
        closeSoftKeyboard(editText, getContext());
    }

    @Override
    public void getData(List<FreindBean> list) {
        swipe.setRefreshing(false);
        sectionedExpandableLayoutHelper.removeAllData();
        freindBeanList.clear();
        freindBeanList.addAll(list);
        for (FreindBean freindBean : list) {
            ArrayList<FreindBean.UsersBean> arrayList = new ArrayList<>();
            arrayList.addAll(freindBean.getUsers());
            sectionedExpandableLayoutHelper.addSection(freindBean.getName(), arrayList);
        }
    }

    @Override
    public void onRefresh() {
        presenter.getFriends(getContext());
    }

    @Override
    public void itemClicked(FreindBean.UsersBean item, String projectName) {
        showDialog(changeBean(item, projectName));
    }

    private void showDialog(FriendProBean bean){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.friend_dialog, null);
        TextView sex = (TextView) view.findViewById(R.id.sex);
        TextView project = (TextView) view.findViewById(R.id.project);
        TextView phone = (TextView) view.findViewById(R.id.phone);

        String type = "";
        if (bean.getType() != 0){
            type = " -- 管理员";
        }
        sex.setText("性别:"+(bean.getSex()==0?"女":"男"));
        project.setText("所属项目:"+bean.getProName());
        phone.setText("手机:"+bean.getMobile());
        new IOSDialog(getContext()).builder()
                .setTitle(bean.getName()+type)
                .setContentView(view)
                .setPositiveButton("确定", null)
                .show();
    }

    @Override
    public void onFailure() {
        swipe.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


    private FriendProBean changeBean(FreindBean.UsersBean bean, String proName){
        FriendProBean friendProBean = new FriendProBean();
        friendProBean.setAddress(bean.getAddress());
        friendProBean.setAppid(bean.getAppid());
        friendProBean.setDateline(bean.getDateline());
        friendProBean.setDeviceRemark(bean.getDeviceRemark());
        friendProBean.setDeviceToken(bean.getDeviceToken());
        friendProBean.setDisabled(bean.getDisabled());
        friendProBean.setEmail(bean.getEmail());
        friendProBean.setExperience(bean.getExperience());
        friendProBean.setFace(bean.getFace());
        friendProBean.setGold(bean.getGold());
        friendProBean.setId(bean.getId());
        friendProBean.setLastLoginTime(bean.getLastLoginTime());
        friendProBean.setLastPasswordModifiedTime(bean.getLastPasswordModifiedTime());
        friendProBean.setLocked(bean.getLocked());
        friendProBean.setMobile(bean.getMobile());
        friendProBean.setName(bean.getName());
        friendProBean.setPassword(bean.getPassword());
        friendProBean.setProject(bean.getProject());
        friendProBean.setSex(bean.getSex());
        friendProBean.setToken(bean.getToken());
        friendProBean.setType(bean.getType());
        friendProBean.setVIP(bean.getVIP());
        friendProBean.setVipDateline(bean.getVipDateline());
        friendProBean.setProName(proName);
        return friendProBean;
    }
}
