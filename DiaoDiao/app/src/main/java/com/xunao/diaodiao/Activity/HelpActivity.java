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
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.FindProjReq;
import com.xunao.diaodiao.Bean.FindProjectRes;
import com.xunao.diaodiao.Model.User;
import com.xunao.diaodiao.Present.AddPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ShareUtils;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.AddView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.TYPE_KEY;
import static com.xunao.diaodiao.Common.Constants.latData;
import static com.xunao.diaodiao.Common.Constants.lngData;

public class HelpActivity extends BaseActivity implements AddView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @Inject
    AddPresenter presenter;
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
    private int page = 1;
    private FindProjReq req = new FindProjReq();
    private FindProjectRes.FindProject res;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HelpActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
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


        adapter = new RecyclerArrayAdapter<FindProjectRes.FindProject>(this, R.layout.help_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, FindProjectRes.FindProject homeBean) {
                baseViewHolder.setText(R.id.title, homeBean.getTitle());
                baseViewHolder.setText(R.id.time, homeBean.getIssue_time());
                baseViewHolder.setText(R.id.address, homeBean.getDesc());
                baseViewHolder.setText(R.id.distance, homeBean.getDistance());
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            WebViewDetailActivity.startActivity(this,
                    adapter.getAllData().get(i));
        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        onRefresh();

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                req.setKeyword(editText.getText().toString());
                page = 1;
                req.setPage(1);
                presenter.getMutualList(req, 3);
                return true;
            }else{
                return false;
            }
        });
        req.setKeyword("");

    }

    @Override
    public void onRefresh() {
        req.setLat(latData);
        req.setLng(lngData);
        page = 1;
        req.setPage(page);
        req.setPageSize(10);
        presenter.getMutualList(req, 3);
    }

    @Override
    public void onLoadMore() {
        page++;
        req.setPage(page);
        presenter.getMutualList(req, 3);
    }

    @Override
    public void onFailure() {
        if(adapter.getAllData().size() == 0){
            recyclerView.showEmpty();
        }else {
            adapter.stopMore();
        }
    }

    @Override
    public void getData(FindProjectRes s) {
        if(page == 1){
            adapter.clear();
        }
        adapter.addAll(s.getMutual());
    }
}
