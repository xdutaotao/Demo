package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.GetPercentRes;
import com.xunao.diaodiao.Bean.ReleaseHelpReq;
import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.ReleaseSkillSureInfoPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.ReleaseSkillSureInfoView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class ReleaseSkillSureInfoActivity extends BaseActivity implements ReleaseSkillSureInfoView {

    @Inject
    ReleaseSkillSureInfoPresenter presenter;
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
    @BindView(R.id.region)
    TextView region;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.contact_phone)
    TextView contactPhone;
    @BindView(R.id.door_time)
    TextView doorTime;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.fee)
    TextView fee;
    @BindView(R.id.service_fee)
    TextView serviceFee;
    @BindView(R.id.all_fee)
    TextView allFee;

    private ReleaseHelpReq req;
    private RecyclerArrayAdapter<String> adapter;


    public static void startActivity(Context context, ReleaseHelpReq req) {
        Intent intent = new Intent(context, ReleaseSkillSureInfoActivity.class);
        intent.putExtra(INTENT_KEY, req);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_skill_sure_info);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "发布维修信息");


        req = (ReleaseHelpReq) getIntent().getSerializableExtra(INTENT_KEY);
        title.setText(req.getTitle());
        region.setText(req.getRegion());
        address.setText(req.getAddress());
        contact.setText(req.getContact());
        contactPhone.setText(req.getContact_mobile());
        doorTime.setText(req.getBuildTimeString());
        type.setText(req.getTypeString());
        content.setText(req.getDescribe());


        adapter = new RecyclerArrayAdapter<String>(this, R.layout.single_image_delete) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setImageUrl(R.id.image, s);
                baseViewHolder.setVisible(R.id.delete, false);
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            if(adapter.getAllData().size() > 0)
                PhotoActivity.startActivity(this, adapter.getAllData().get(i),
                        adapter.getAllData().get(i).contains("http"));

        });
        recyclerView.setAdapter(adapter);
        adapter.addAll(req.getImages());
        fee.setText(req.getDoor_fee());
        presenter.countMaintenanceExpenses(this, req.getDoor_fee());

        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, Constants.DESTORY))
                .subscribe(s -> {
                    finish();
                });

        pay.setOnClickListener(v -> {

            if(req.getImages() != null && (req.getImages().size()>0) &&  (req.getImages().get(0).length() < 100)){
                List<String> list = new ArrayList<>();
                for(String s : req.getImages()){
                    list.add(Utils.Bitmap2StrByBase64(s));
                }
                req.setImages(list);
            }

            presenter.publishMaintenance(this, req);
        });

        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, Constants.DESTORY))
                .subscribe(s -> {
                    finish();
                });
        
        if (getIntent().getBooleanExtra("flag", false)){
            pay.setText("发布");
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
    public void getPercent(GetPercentRes res) {
        req.setDoor_fee("￥"+res.getDoor_fee());
        req.setService_fee("￥"+res.getService_fee());
        fee.setText("￥"+res.getDoor_fee());
        serviceFee.setText("￥"+res.getService_fee());
        allFee.setText("￥"+res.getTotal_fee());
    }

    @Override
    public void getData(ReleaseProjRes res) {
        PayActivity.startActivity(ReleaseSkillSureInfoActivity.this, res, 4);
    }
}
