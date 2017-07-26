package com.seafire.cworker.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzfgeh.swipeheader.SwipeRefreshLayout;
import com.seafire.cworker.Bean.FreindBean;
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

        swipe.setOnRefreshListener(this);
        onRefresh();
        return view;
    }

    private void doSearchAction(String s) {
        if (!TextUtils.isEmpty(s)) {

        }
    }

    @Override
    public void getData(List<FreindBean> list) {
        swipe.setRefreshing(false);
        for (FreindBean freindBean : list) {
            ArrayList<FreindBean.UsersBean> arrayList = new ArrayList<>();
            arrayList.addAll(freindBean.getUsers());
            sectionedExpandableLayoutHelper.addSection(freindBean.getName(), arrayList);
        }
        sectionedExpandableLayoutHelper.notifyDataSetChanged();

        //checking if adding single item works
//        sectionedExpandableLayoutHelper.addItem("Ice cream", new Item("Tutti frutti",5));
//        sectionedExpandableLayoutHelper.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        presenter.getFriends();
    }

    @Override
    public void itemClicked(FreindBean.UsersBean item) {

    }

    @Override
    public void itemClicked(Section section) {

    }

    @Override
    public void onFailure() {
        swipe.setRefreshing(false);
        ToastUtil.show("请稍后重试");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
