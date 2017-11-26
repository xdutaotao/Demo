package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Bean.ApplyDetailRes;
import com.xunao.diaodiao.Bean.GetMoneyReq;
import com.xunao.diaodiao.Bean.OrderSkillDoingRes;
import com.xunao.diaodiao.Present.CustomerDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.CustomerDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class CustomerDetailActivity extends BaseActivity implements CustomerDetailView {

    @Inject
    CustomerDetailPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.rating_star)
    RatingBar ratingStar;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    OrderSkillDoingRes.OddBean bean;
    @BindView(R.id.contact)
    Button contact;
    private String phone;

    private RecyclerArrayAdapter<ApplyDetailRes.EvaluateBean> adapter;

    public static void startActivity(Context context, OrderSkillDoingRes.OddBean homeBean) {
        Intent intent = new Intent(context, CustomerDetailActivity.class);
        intent.putExtra(INTENT_KEY, homeBean);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "家庭用户详情");

        bean = (OrderSkillDoingRes.OddBean) getIntent().getSerializableExtra(INTENT_KEY);

        adapter = new RecyclerArrayAdapter<ApplyDetailRes.EvaluateBean>(this, R.layout.apply_detail_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, ApplyDetailRes.EvaluateBean s) {
                baseViewHolder.setText(R.id.name, s.getName());
                baseViewHolder.setImageUrl(R.id.head_icon, s.getHead_img(), R.drawable.head_icon_boby);
                baseViewHolder.setText(R.id.content, s.getContent());
            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        GetMoneyReq req = new GetMoneyReq();
        req.setId(bean.getMaintenance_id());
        req.setUserid(bean.getPublish_id());
        req.setType(bean.getPublish_type());
        req.setPage(1);
        req.setPageSize(10);
        presenter.maintenanceInfo(this, req);

        address.setOnClickListener(v -> {

        });

        contact.setText("联系ta");
        contact.setOnClickListener(v -> {
            new IOSDialog(this).builder()
                    .setMsg(phone)
                    .setPositiveButton("呼叫", v1 -> {
                        if (!TextUtils.isEmpty(phone)) {
                            Utils.startCallActivity(this, phone);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });
    }

    @Override
    public void getData(ApplyDetailRes res) {
        name.setText(res.getName());
        ratingStar.setRating(Float.valueOf(res.getPoint()));
        ratingStar.setIsIndicator(true);
        time.setText(res.getPoint());
        address.setText(res.getAddress());
        if (res.getEvaluate_Info().size() > 0)
            adapter.addAll(res.getEvaluate_Info());

        phone = res.getContact_mobile();

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
