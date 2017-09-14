package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.FindProjectPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.FindProjectView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;
import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;
import static com.xunao.diaodiao.Common.Constants.latData;
import static com.xunao.diaodiao.Common.Constants.lngData;

/**
 * create by
 */
public class FindProjectActivity extends BaseActivity implements FindProjectView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @Inject
    FindProjectPresenter presenter;
    @BindView(R.id.type_image)
    ImageView typeImage;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.cancle_action)
    ImageView cancleAction;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;
    @BindView(R.id.back_icon)
    ImageView backIcon;

    private RecyclerArrayAdapter<FindProjectRes.FindProject> adapter;
    private int page;
    private FindProjReq req = new FindProjReq();
    private int type;

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, FindProjectActivity.class);
        intent.putExtra(INTENT_KEY, type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_project);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        type = getIntent().getIntExtra(INTENT_KEY, 0);
        backIcon.setOnClickListener(v -> {
            finish();
        });

        adapter = new RecyclerArrayAdapter<FindProjectRes.FindProject>(this, R.layout.home_vertical_list) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, FindProjectRes.FindProject homeBean) {
                baseViewHolder.setText(R.id.item_content, homeBean.getTitle());
                baseViewHolder.setText(R.id.address, homeBean.getDesc());
                baseViewHolder.setText(R.id.time, Utils.strToDateLong(homeBean.getBuild_time()));
                baseViewHolder.setText(R.id.name, homeBean.getType());
                baseViewHolder.setText(R.id.distance, homeBean.getDistance());
                if (type == 0){
                    baseViewHolder.setText(R.id.price, " ￥ " + homeBean.getPrice());
                }else if(type == 1){
                    baseViewHolder.setText(R.id.price_text, "共三天");
                    baseViewHolder.setText(R.id.price, " ￥ " + homeBean.getPrice() + " / 天");
                }

            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            if (!TextUtils.isEmpty(User.getInstance().getUserId())) {
                ProjectDetailActivity.startActivity(FindProjectActivity.this,
                        adapter.getAllData().get(i).getId(), type);
            } else {
                LoginActivity.startActivity(FindProjectActivity.this);
            }

        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);

        onRefresh();
    }

    @Override
    public void getData(FindProjectRes res) {
        if (page == 1) {
            adapter.clear();
            adapter.addAll(res.getProject());
        } else {
            adapter.addAll(res.getProject());
        }
    }

    @Override
    public void getNoMore(String msg) {
        ToastUtil.show(msg);
        adapter.stopMore();
    }

    @Override
    public void onRefresh() {
        req.setLat(latData);
        req.setLng(lngData);
        page = 1;
        req.setPage(page);
        presenter.getProjectList(req, type);
    }

    @Override
    public void onLoadMore() {
        page++;
        req.setPage(page);
        presenter.getProjectList(req, type);
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
