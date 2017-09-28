package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.ReleaseSkillReq;
import com.xunao.diaodiao.Present.ReleaseSkillSecondPresenter;
import com.xunao.diaodiao.R;
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

    public static void startActivity(Context context, ReleaseSkillReq req) {
        Intent intent = new Intent(context, ReleaseSkillSecondActivity.class);
        intent.putExtra(INTENT_KEY, req);
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

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setImageUrl(R.id.image, s);
                baseViewHolder.setVisible(R.id.delete, false);
            }
        };
        recyclerView.setAdapter(adapter);


        title.setText(req.getTitle());
        address.setText(req.getAddress());
        addressDetail.setText(req.getAddress());
        projectType.setText(req.getProject_type());
        contact.setText(req.getContact());
        phone.setText(req.getContact_mobile());
        days.setText(req.getTotal_day());
        fee.setText(req.getDaily_wage());
        time.setText(Utils.millToDateString(req.getBuild_time()));
        describe.setText(req.getDescribe());
        adapter.addAll(req.getImages());
        projectFee.setText("￥"+req.getOdd_fee());
        serviceFee.setText("￥"+req.getService_fee());

        allPrice.setText("￥"+req.getTotal_fee());
        pay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay:
                if(req.getImages().get(0).length() < 100){
                    List<String> list = new ArrayList<>();
                    for(String s : req.getImages()){
                        list.add(Utils.Bitmap2StrByBase64(s));
                    }
                    req.setImages(list);
                }

                PayActivity.startActivity(this, req);

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


}
