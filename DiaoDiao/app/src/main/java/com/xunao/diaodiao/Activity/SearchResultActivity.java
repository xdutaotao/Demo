package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xunao.diaodiao.Bean.HomeResponseBean;
import com.xunao.diaodiao.Bean.SearchBean;
import com.xunao.diaodiao.Bean.SearchResponseBean;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.SearchResultPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.SearchResultView;
import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.LOGIN_AGAIN;

/**
 * create by
 */
public class SearchResultActivity extends BaseActivity implements SearchResultView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {
    private static final String[] titles = {"精华", "猜你喜欢","经典"};
    @Inject
    SearchResultPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;

    private RecyclerArrayAdapter<HomeResponseBean.TopicBean.GroupDataBean> adapter;

    private SearchBean bean;

    public static void startActivity(Context context, SearchBean bean) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra(INTENT_KEY, bean);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        if (getIntent().getSerializableExtra(INTENT_KEY) != null) {
            bean = (SearchBean) getIntent().getSerializableExtra(INTENT_KEY);
            showToolbarBack(toolBar, titleText, titles[bean.getGroupType()-1]);
        }

        adapter = new RecyclerArrayAdapter<HomeResponseBean.TopicBean.GroupDataBean>(this, R.layout.home_vertical_list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, HomeResponseBean.TopicBean.GroupDataBean dataBean) {
            }
        };
        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        onRefresh();
    }

    @Override
    public void getData(SearchResponseBean searchResponseBean) {
        if (bean.getPageNo() == 1){
            adapter.clear();
        }
        adapter.addAll(searchResponseBean.getData());
    }

    @Override
    public void fail(String msg) {
        ToastUtil.show(msg);
        if (adapter.getAllData().size() == 0)
            recyclerView.setVisibility(View.GONE);
        else
            adapter.stopMore();

        if (TextUtils.equals(msg, "请登录!")){
            User.getInstance().clearUser();
            RxBus.getInstance().post(LOGIN_AGAIN);
        }
    }

    @Override
    public void onRefresh() {
        bean.setPageNo(1);
        presenter.getSearchResult(bean);
    }

    @Override
    public void onLoadMore() {
        bean.setPageNo(bean.getPageNo()+1);
        presenter.getSearchResult(bean);
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
