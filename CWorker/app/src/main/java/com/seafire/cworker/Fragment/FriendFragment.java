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

import com.seafire.cworker.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends BaseFragment {


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
    Unbinder unbinder;

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
        //presenter.attachView(this);

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

}
