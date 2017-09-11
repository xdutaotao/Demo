package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.MyFavoriteItem;
import com.xunao.diaodiao.Bean.MyFavoriteRes;
import com.xunao.diaodiao.Present.MyFavoritePresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.MyFavoriteView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class MyFavoriteActivity extends BaseActivity implements MyFavoriteView {

    @Inject
    MyFavoritePresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RecyclerArrayAdapter<MyFavoriteItem> adapter;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyFavoriteActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "我的收藏");

        adapter = new RecyclerArrayAdapter<MyFavoriteItem>(this, R.layout.my_favorite_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, MyFavoriteItem s) {
                if (TextUtils.equals(s.getClasses(), "项目")){
                    baseViewHolder.setBackgroundRes(R.id.bg, R.drawable.bg_xiangmu);
                    baseViewHolder.setText(R.id.title, s.getTitle());
                    baseViewHolder.setImageResource(R.id.shou_cang, R.drawable.icon_shoucang02_fill);
                    baseViewHolder.setText(R.id.address, s.getAddress());
                    baseViewHolder.setText(R.id.price, "￥ "+s.getFee());
                    baseViewHolder.setText(R.id.type, s.getType());
                }else if (TextUtils.equals(s.getClasses(), "零工")){
                    baseViewHolder.setBackgroundRes(R.id.bg, R.drawable.bg_lingong);
                    baseViewHolder.setText(R.id.title, s.getTitle());
                    baseViewHolder.setImageResource(R.id.shou_cang, R.drawable.icon_shoucang02_fill);
                    baseViewHolder.setText(R.id.address, s.getAddress());
                    baseViewHolder.setText(R.id.price_text, "共"+s.getTotal_day()+"天");
                    baseViewHolder.setText(R.id.price, "￥ "+s.getFee() + "/天");
                    baseViewHolder.setVisible(R.id.type, false);
                }else{
                    baseViewHolder.setBackgroundRes(R.id.bg, R.drawable.bg_jianli);
                    baseViewHolder.setText(R.id.title, s.getTitle());
                    baseViewHolder.setImageResource(R.id.shou_cang, R.drawable.icon_shoucang02_fill);
                    baseViewHolder.setText(R.id.address, s.getAddress());
                    if (TextUtils.isEmpty(s.getFee())){
                        baseViewHolder.setVisible(R.id.price, false);
                        baseViewHolder.setVisible(R.id.price_text, false);
                    }else{
                        baseViewHolder.setText(R.id.price, "￥ "+s.getFee());
                    }
                    baseViewHolder.setText(R.id.type, s.getType());
                }
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        presenter.getCollectList(this);
    }

    @Override
    public void getData(MyFavoriteRes data) {
        List<MyFavoriteItem> list = new ArrayList<>();
        if (data.getProject().size() > 0){
            for(MyFavoriteRes.Project project: data.getProject()){
                MyFavoriteItem projItem = new MyFavoriteItem();
                projItem.setId(project.getId());
                projItem.setTitle(project.getTitle());
                projItem.setAddress(project.getAddress());
                projItem.setClasses(project.getClasses());
                projItem.setCollect_id(project.getCollect_id());
                projItem.setFee(project.getProject_fee());
                projItem.setType(project.getType());
                list.add(projItem);
            }
        }


        if (data.getSupervisor().size() > 0){
            for(MyFavoriteRes.Supervisor supervisor: data.getSupervisor()){
                MyFavoriteItem projItem = new MyFavoriteItem();
                projItem.setId(supervisor.getId());
                projItem.setTitle(supervisor.getTitle());
                projItem.setAddress(supervisor.getAddress());
                projItem.setClasses(supervisor.getClasses());
                projItem.setCollect_id(supervisor.getCollect_id());
                projItem.setFee(supervisor.getSupervisor_fee());
                projItem.setType(supervisor.getType());
                list.add(projItem);
            }
        }


        if (data.getOdd().size() > 0){
            for(MyFavoriteRes.Odd odd: data.getOdd()){
                MyFavoriteItem projItem = new MyFavoriteItem();
                projItem.setId(odd.getId());
                projItem.setTitle(odd.getTitle());
                projItem.setAddress(odd.getAddress());
                projItem.setClasses(odd.getClasses());
                projItem.setCollect_id(odd.getCollect_id());
                projItem.setFee(odd.getDaily_wage());
                projItem.setType(odd.getType());
                projItem.setTotal_day(odd.getTotal_day());
                list.add(projItem);
            }
        }

        if (data.getMaintenance().size() > 0){
            for(MyFavoriteRes.Maintenance maintenance: data.getMaintenance()){
                MyFavoriteItem projItem = new MyFavoriteItem();
                projItem.setId(maintenance.getId());
                projItem.setTitle(maintenance.getTitle());
                projItem.setAddress(maintenance.getAddress());
                projItem.setClasses(maintenance.getClasses());
                projItem.setCollect_id(maintenance.getCollect_id());
                projItem.setFee(maintenance.getDoor_fee());
                projItem.setType(maintenance.getType());
                list.add(projItem);
            }
        }

        if (data.getMutual().size() > 0){
            for(MyFavoriteRes.Mutual mutual: data.getMutual()){
                MyFavoriteItem projItem = new MyFavoriteItem();
                projItem.setId(mutual.getId());
                projItem.setTitle(mutual.getTitle());
                projItem.setAddress(mutual.getAddress());
                projItem.setClasses(mutual.getClasses());
                projItem.setCollect_id(mutual.getCollect_id());
                list.add(projItem);
            }
        }

        adapter.clear();
        adapter.addAll(list);

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
