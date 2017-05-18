package com.demo.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.demo.cworker.Bean.SearchBean;
import com.demo.cworker.Bean.SearchResponseBean;
import com.demo.cworker.Present.SearchResultPresenter;
import com.demo.cworker.R;
import com.demo.cworker.View.SearchResultView;
import com.gzfgeh.GRecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.demo.cworker.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class SearchResultActivity extends BaseActivity implements SearchResultView {

    @Inject
    SearchResultPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
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
            presenter.getSearchResult(this, bean);
        }
    }


    @Override
    public void onFailure() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void getData(SearchResponseBean bean) {

    }
}
