package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.JoinPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.JoinView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;

/**
 * create by
 */
public class JoinActivity extends BaseActivity implements JoinView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @Inject
    JoinPresenter presenter;
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
    @BindView(R.id.point_layout)
    LinearLayout pointLayout;
    @BindView(R.id.point)
    TextView point;

    private RecyclerArrayAdapter<FindProjectRes.FindProject> adapter;
    private int page = 1;
    private FindProjReq req = new FindProjReq();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, JoinActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        backIcon.setOnClickListener(v -> {
            finish();
        });

        back.setOnClickListener(v -> {
            if (TextUtils.isEmpty(User.getInstance().getUserId())) {
                ToastUtil.show("请登录");
                return;
            }

            if (ShareUtils.getValue(TYPE_KEY, 0) == 0) {
                ToastUtil.show("请选择角色");
                return;
            }

            RxBus.getInstance().post("release");
            finish();

        });

        pointLayout.setOnClickListener(v -> {
            if (req.getNearby() == 0) {
                point.setTextColor(getResources().getColor(R.color.colorAccent));
                req.setSort("");
            } else {
                point.setTextColor(getResources().getColor(R.color.nav_gray));
                req.setSort("");
            }

            page = 1;
            req.setPage(page);
            presenter.businesses(JoinActivity.this, req);
        });

        adapter = new RecyclerArrayAdapter<FindProjectRes.FindProject>(this, R.layout.join_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, FindProjectRes.FindProject homeBean) {
                baseViewHolder.setText(R.id.item_content, homeBean.getName());
                RatingBar bar = (RatingBar) baseViewHolder.getConvertView().findViewById(R.id.rating_star);
                bar.setIsIndicator(true);
                bar.setRating(Float.valueOf(homeBean.getPlat_point()));
                baseViewHolder.setText(R.id.time, homeBean.getPlat_point());
                baseViewHolder.setText(R.id.address, homeBean.getAddress());
                baseViewHolder.setText(R.id.days, homeBean.getTel());

                baseViewHolder.setOnClickListener(R.id.request, v -> {
                    new IOSDialog(JoinActivity.this).builder()
                            .setMsg(homeBean.getTel())
                            .setNegativeButton("呼叫", v1 -> {
                                Utils.startCallActivity(JoinActivity.this, homeBean.getTel());
                            })
                            .setPositiveButton("取消", null)
                            .show();
                });
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            //JoinDetailActivity.startActivity(JoinActivity.this, 1, 1);
        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        onRefresh();

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                req.setKeyword(editText.getText().toString());
                page = 1;
                req.setPage(1);
                presenter.businesses(this, req);
                return true;
            } else {
                return false;
            }
        });
        req.setKeyword("");

    }

    @Override
    public void onRefresh() {
        page = 1;
        req.setPage(page);
        req.setPageSize(10);
        presenter.businesses(this, req);
    }

    @Override
    public void onLoadMore() {
        page++;
        req.setPage(page);
        presenter.businesses(this , req);
    }

    @Override
    public void onFailure() {
        if (adapter.getAllData().size() == 0) {
            recyclerView.showEmpty();
        } else {
            adapter.stopMore();
        }
    }

    @Override
    public void getData(FindProjectRes s) {
        if (page == 1) {
            adapter.clear();
        }
        adapter.addAll(s.getInfo());
    }


}
