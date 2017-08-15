package com.xunao.diaodiao.Fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.SearchResponseBean;
import com.xunao.diaodiao.Present.SearchPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.SearchView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class ReleaseFragment extends BaseFragment implements SearchView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    private static final String ARG_PARAM1 = "param1";
    @Inject
    SearchPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    private String mParam1;

    public static ReleaseFragment newInstance(String param1) {
        ReleaseFragment fragment = new ReleaseFragment();
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
        View view = inflater.inflate(R.layout.fragment_release, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        hideToolbarBack(toolBar, titleText, "发布");

        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void getData(SearchResponseBean searchResponseBean) {
    }

    @Override
    public void getHistoryList(List<String> list) {
    }

    @Override
    public void fail(String msg) {

    }

    @Override
    public void getHotWord(List<String> list) {

    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore() {
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
