package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.MessagePresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.MessageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class MessageFragment extends BaseFragment implements MessageView, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    @Inject
    MessagePresenter presenter;
    @BindView(R.id.pe_btn)
    RadioButton peBtn;
    @BindView(R.id.pv_btn)
    RadioButton pvBtn;
    @BindView(R.id.consult_content)
    FrameLayout consultContent;
    @BindView(R.id.add)
    ImageView add;
    private String mParam1;

    private static final String SAVE_TYPE = "save_type";
    public static final int PE_FRAGMENT = 1;
    public static final int PV_FRAGMENT = 3;
    private static final String PE_TAG = "pe_tag";
    private static final String PV_TAG = "pv_tag";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment peFragment, ppFragment, pvFragment;

    public static MessageFragment newInstance(String param1) {
        MessageFragment fragment = new MessageFragment();
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
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        fragmentManager = getActivity().getSupportFragmentManager();

        if (savedInstanceState != null) {
            int type = savedInstanceState.getInt(SAVE_TYPE);
            peFragment = fragmentManager.findFragmentByTag(PE_TAG);
            pvFragment = fragmentManager.findFragmentByTag(PV_TAG);
            if (type > 0) {
                loadFragment(type);
            }
        } else {
            fragmentTransaction = fragmentManager.beginTransaction();
            peFragment = fragmentManager.findFragmentByTag(PE_TAG);
            if (peFragment != null) {
                fragmentTransaction.replace(R.id.consult_content, peFragment);
                fragmentTransaction.commit();
            } else {
                loadFragment(PE_FRAGMENT);
            }
        }

        peBtn.setOnClickListener(this);
        pvBtn.setOnClickListener(this);

        if (User.getInstance().getUserInfo() != null){
            add.setVisibility(User.getInstance().getUserInfo().getPerson().getType() != 0 ? View.VISIBLE : View.GONE);
        }else{
            add.setVisibility(View.GONE);
        }
        add.setOnClickListener(v -> {
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pe_btn:
                loadFragment(PE_FRAGMENT);
                break;

            case R.id.pv_btn:
                loadFragment(PV_FRAGMENT);
                break;
        }
    }

    private void loadFragment(int type) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (type == PE_FRAGMENT) {
            if (peFragment == null) {
                peFragment = new FriendFragment();
                fragmentTransaction.add(R.id.consult_content, peFragment, PE_TAG);
            } else {
                fragmentTransaction.show(peFragment);
            }

            if (ppFragment != null) {
                fragmentTransaction.hide(ppFragment);
            }

            if (pvFragment != null) {
                fragmentTransaction.hide(pvFragment);
            }
        } else {
            if (pvFragment == null) {
                pvFragment = new FriendFragment();
                fragmentTransaction.add(R.id.consult_content, pvFragment, PV_TAG);
            } else {
                fragmentTransaction.show(pvFragment);
            }

            if (peFragment != null) {
                fragmentTransaction.hide(peFragment);
            }

            if (ppFragment != null) {
                fragmentTransaction.hide(ppFragment);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
