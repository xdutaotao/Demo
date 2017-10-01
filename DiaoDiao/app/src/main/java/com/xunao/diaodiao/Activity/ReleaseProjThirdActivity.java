package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.ReleaseProjReq;
import com.xunao.diaodiao.Bean.ReleaseProjReqTemp;
import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Present.ReleaseProjThirdPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ReleaseProjThirdView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class ReleaseProjThirdActivity extends BaseActivity implements ReleaseProjThirdView, View.OnClickListener {

    @Inject
    ReleaseProjThirdPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.pay)
    TextView pay;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_detail)
    TextView addressDetail;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.describe)
    TextView describe;
    @BindView(R.id.type_recycler_view)
    RecyclerView typeRecyclerView;
    @BindView(R.id.project_fee)
    TextView projectFee;
    @BindView(R.id.service_fee)
    TextView serviceFee;
    @BindView(R.id.all_price)
    TextView allPrice;

    private ReleaseProjReq req;
    private List<ReleaseProjReqTemp> list;

    private RecyclerArrayAdapter<String> adapter;
    private RecyclerArrayAdapter<ReleaseProjReqTemp> typeAdapter;

    public static void startActivity(Context context, ReleaseProjReq req, List<ReleaseProjReqTemp> temp) {
        Intent intent = new Intent(context, ReleaseProjThirdActivity.class);
        intent.putExtra(INTENT_KEY, req);
        intent.putExtra("TEMP", (Serializable) temp);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_proj_third);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "确认项目信息");

        req = (ReleaseProjReq) getIntent().getSerializableExtra(INTENT_KEY);
        list = (List<ReleaseProjReqTemp>) getIntent().getSerializableExtra("TEMP");
        pay.setOnClickListener(this);

        adapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setImageUrl(R.id.image, s);
                baseViewHolder.setVisible(R.id.delete, false);
            }
        };

        typeAdapter = new RecyclerArrayAdapter<ReleaseProjReqTemp>(this, R.layout.res_proj_type_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, ReleaseProjReqTemp s) {
                baseViewHolder.setText(R.id.name, s.getName());
                baseViewHolder.setVisible(R.id.type_detail, true);
                baseViewHolder.setVisible(R.id.type_temp, false);
                baseViewHolder.setText(R.id.type_detail,
                         s.getAmount() + s.getUnit()+ " x " + s.getUnit_price()  + " = " +s.getTotal_price()+"元");
            }
        };

        recyclerView.setAdapter(adapter);

        typeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        typeRecyclerView.setAdapter(typeAdapter);

        title.setText(req.getTitle());
        address.setText(req.getAddress());
        addressDetail.setText(req.getAddress());
        contact.setText(req.getContact());
        phone.setText(req.getContact_mobile());
        time.setText(Utils.millToDateString(req.getBuild_time()));
        describe.setText(req.getDescribe());
        adapter.addAll(req.getImages());


        for(ReleaseProjReqTemp item: list){
            BigDecimal bigDecimal = new BigDecimal(Float.valueOf(item.getUnit_price()) * Float.valueOf(item.getAmount()));
            bigDecimal.setScale(2, 4);
            item.setTotal_price(String.valueOf(bigDecimal.floatValue()));
        }

        typeAdapter.addAll(list);

        projectFee.setText("￥"+req.getProject_fee());
        serviceFee.setText("￥"+req.getService_cost());
        allPrice.setText("￥"+req.getTotal_price());


    }

    @Override
    public void getData(ReleaseProjRes s) {
        // 1 项目
        PayActivity.startActivity(this, s, 1);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay:
                if(req.getImages().get(0).length() < 100){
                    List<String> list = new ArrayList<>();
                    for(String s : req.getImages()){
                        list.add(Utils.Bitmap2StrByBase64(s));
                    }
                    req.setImages(list);
                }

                presenter.publishProject(this, req);


                break;
        }
    }


}
