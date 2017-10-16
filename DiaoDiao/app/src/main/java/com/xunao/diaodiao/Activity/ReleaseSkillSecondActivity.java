package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.OddFeeRes;
import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.Bean.ReleaseSkillReq;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.ReleaseSkillSecondPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ReleaseSkillSecondView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class ReleaseSkillSecondActivity extends BaseActivity implements ReleaseSkillSecondView, View.OnClickListener {

    @Inject
    ReleaseSkillSecondPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_detail)
    TextView addressDetail;
    @BindView(R.id.project_type)
    TextView projectType;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.fee)
    TextView fee;
    @BindView(R.id.days)
    TextView days;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.describe)
    TextView describe;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.project_fee)
    TextView projectFee;
    @BindView(R.id.service_fee)
    TextView serviceFee;
    @BindView(R.id.all_price)
    TextView allPrice;
    @BindView(R.id.pay)
    TextView pay;

    private ReleaseSkillReq req;
    private RecyclerArrayAdapter<String> adapter;
    private boolean flag;

    public static void startActivity(Context context, ReleaseSkillReq req) {
        Intent intent = new Intent(context, ReleaseSkillSecondActivity.class);
        intent.putExtra(INTENT_KEY, req);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, ReleaseSkillReq req, boolean flag) {
        Intent intent = new Intent(context, ReleaseSkillSecondActivity.class);
        intent.putExtra(INTENT_KEY, req);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_skill_second);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "确认零工信息");
        req = (ReleaseSkillReq) getIntent().getSerializableExtra(INTENT_KEY);
        flag = getIntent().getBooleanExtra("flag", false);

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setImageUrl(R.id.image, s);
                baseViewHolder.setVisible(R.id.delete, false);
            }
        };
        recyclerView.setAdapter(adapter);


        title.setText(req.getTitle());
        address.setText(req.getRegion());
        addressDetail.setText(req.getAddress());
        projectType.setText(req.getProject_type_string());
        contact.setText(req.getContact());
        phone.setText(req.getContact_mobile());
        days.setText(req.getTotal_day());
        fee.setText(req.getDaily_wage());
        time.setText(req.getBuild_time_string());
        describe.setText(req.getDescribe());
        adapter.addAll(req.getImages());

        pay.setOnClickListener(this);


        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, Constants.DESTORY))
                .subscribe(s -> {
                    finish();
                });

        if (flag){
            projectFee.setText("￥"+req.getOdd_fee());
            serviceFee.setText("￥"+req.getService_fee());
            allPrice.setText("￥"+req.getTotal_fee());

            pay.setText("发布");

        }else{
            presenter.countOddExpenses(this, req);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay:
                if (!flag){
                    if(req.getImages().get(0).length() < 100){
                        List<String> list = new ArrayList<>();
                        for(String s : req.getImages()){
                            list.add(Utils.Bitmap2StrByBase64(s));
                        }
                        req.setImages(list);
                    }

                    presenter.publishOdd(this, req);
                }else{
                    presenter.updateOdd(this, req);
                }


                break;
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
    public void getData(ReleaseProjRes res) {
        // 零工
        PayActivity.startActivity(this, res, 3);
    }

    @Override
    public void getData(Object res) {
        RxBus.getInstance().post("update_project");
        finish();
    }

    @Override
    public void getData(OddFeeRes res) {
        projectFee.setText("￥"+res.getOdd_fee());
        serviceFee.setText("￥"+res.getService_fee());
        allPrice.setText("￥"+res.getTotal_fee());

        req.setService_fee(res.getService_fee());
        req.setTotal_fee(res.getTotal_fee());
        req.setOdd_fee(res.getOdd_fee());
    }
}
