package com.seafire.cworker.Fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.seafire.cworker.Bean.FreindBean;
import com.seafire.cworker.Bean.Item;
import com.seafire.cworker.Present.FriendPresenter;
import com.seafire.cworker.R;
import com.seafire.cworker.View.FriendView;
import com.seafire.cworker.adapters.ItemClickListener;
import com.seafire.cworker.adapters.Section;
import com.seafire.cworker.adapters.SectionedExpandableLayoutHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import io.jchat.android.adapter.SearchFriendListAdapter;
import io.jchat.android.chatting.utils.DialogCreator;
import io.jchat.android.chatting.utils.HandleResponseCode;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends BaseFragment implements ItemClickListener, FriendView {


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
        presenter.getFriends();

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

        return view;
    }

    private void doSearchAction(String s){
        if (!TextUtils.isEmpty(s)){

        }
    }

    @Override
    public void itemClicked(Item item) {

    }

    @Override
    public void itemClicked(Section section) {

    }

    @Override
    public void onFailure() {

    }

    @Override
    public void getData(List<FreindBean> list) {
        for(FreindBean freindBean: list){
            ArrayList<FreindBean.UsersBean> arrayList = new ArrayList<>();
            arrayList.addAll(freindBean.getUsers());
            sectionedExpandableLayoutHelper.addSection(freindBean.getName(), arrayList);
        }
        sectionedExpandableLayoutHelper.notifyDataSetChanged();

        //checking if adding single item works
//        sectionedExpandableLayoutHelper.addItem("Ice cream", new Item("Tutti frutti",5));
//        sectionedExpandableLayoutHelper.notifyDataSetChanged();
    }
}
