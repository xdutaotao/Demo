package com.xunao.diaodiao.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Activity.ReleaseHelpActivity;
import com.xunao.diaodiao.Activity.ReleaseSkillActivity;
import com.xunao.diaodiao.Activity.ReleaseSkillInforActivity;
import com.xunao.diaodiao.Bean.MaintenanceTypeRes;
import com.xunao.diaodiao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xunao.diaodiao.Activity.ReleaseHelpActivity.projectBrandName;
import static com.xunao.diaodiao.Activity.ReleaseHelpActivity.projectClassName;
import static com.xunao.diaodiao.Activity.ReleaseHelpActivity.projectTypeName;
import static com.xunao.diaodiao.Activity.ReleaseHelpActivity.project_brand;
import static com.xunao.diaodiao.Activity.ReleaseHelpActivity.project_class;
import static com.xunao.diaodiao.Activity.ReleaseHelpActivity.project_type;
import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

public class ReleaseTabItemFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String SELECT_KEY = "SELECT_KEY";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private boolean isRepair;

    private RecyclerArrayAdapter<MaintenanceTypeRes.RepairBean.BrandBean> adapter;
    private ArrayList<MaintenanceTypeRes.RepairBean.BrandBean> listBean;
    private MaintenanceTypeRes.RepairBean bean;

    public static ReleaseTabItemFragment newInstance(String param1, MaintenanceTypeRes.RepairBean list, boolean isRepair) {
        ReleaseTabItemFragment fragment = new ReleaseTabItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(INTENT_KEY, list);
        args.putBoolean(SELECT_KEY, isRepair);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            bean = (MaintenanceTypeRes.RepairBean)getArguments().getSerializable(INTENT_KEY);
            listBean = (ArrayList<MaintenanceTypeRes.RepairBean.BrandBean>) bean.getBrands();
            isRepair = getArguments().getBoolean(SELECT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_release_tab_item, container, false);
        unbinder = ButterKnife.bind(this, view);

        adapter = new RecyclerArrayAdapter<MaintenanceTypeRes.RepairBean.BrandBean>(getContext(), R.layout.release_tab_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, MaintenanceTypeRes.RepairBean.BrandBean s) {
                baseViewHolder.setText(R.id.recommend_tv, s.getBrand_name());

                if(isRepair){

                    //for(String item: ReleaseHelpActivity.selectRepairTitle){
                        if (TextUtils.equals(s.getBrand_id(), project_brand+"")){
                            baseViewHolder.setTextColorRes(R.id.recommend_tv, R.color.white);
                            baseViewHolder.setBackgroundRes(R.id.recommend_tv, R.drawable.btn_blue_bg);
                        }else{
                            baseViewHolder.setTextColorRes(R.id.recommend_tv, R.color.nav_gray);
                            baseViewHolder.setBackgroundRes(R.id.recommend_tv, R.drawable.btn_blank_bg);
                        }
                    //}

                    baseViewHolder.setOnClickListener(R.id.recommend_tv, v -> {
                        if(TextUtils.equals(s.getBrand_id(), project_brand+"")){
                            v.setBackgroundResource(R.drawable.btn_blank_bg);
                            ((TextView) v).setTextColor(getResources().getColor(R.color.gray));
//                            ReleaseHelpActivity.selectRepairTitle.remove(s.getBrand_id());
                        }else {
                            v.setBackgroundResource(R.drawable.btn_blue_bg);
                            ((TextView) v).setTextColor(Color.WHITE);
                            ReleaseHelpActivity.selectRepairTitle.add(s.getBrand_id());
//                            if(!ReleaseHelpActivity.typeIDs.contains(bean.getClass_id())){
//                                ReleaseHelpActivity.typeIDs.add(bean.getClass_id());
//                            }
                        }

                        project_brand = Integer.valueOf(s.getBrand_id());
                        project_class = Integer.valueOf(bean.getClass_id());
                        project_type = 1;

                        projectClassName = bean.getClass_name();
                        projectBrandName = s.getBrand_name();
                        projectTypeName = "维修";
                        adapter.notifyDataSetChanged();

//                        if(ReleaseHelpActivity.repairNames.contains(bean.getClass_name()+"-"+s.getBrand_name())){
//                            ReleaseHelpActivity.repairNames.remove(bean.getClass_name()+"-"+s.getBrand_name());
//                        }else{
//                            ReleaseHelpActivity.repairNames.add(bean.getClass_name()+"-"+s.getBrand_name());
//                        }
//
//                        if(isContain(ReleaseHelpActivity.repairIDs, s.getBrand_id())){
//                            ReleaseHelpActivity.repairIDs.remove(s.getBrand_id());
//                        }else{
//                            ReleaseHelpActivity.repairIDs.add(s.getBrand_id());
//                        }
//
//                        if((ReleaseHelpActivity.repairIDs.size() == 0) &&
//                                ReleaseHelpActivity.typeIDs.contains(bean.getClass_id())){
//                            ReleaseHelpActivity.typeIDs.remove(bean.getClass_id());
//                        }else{
//                            ReleaseHelpActivity.typeIDs.add(bean.getClass_id());
//                        }

                    });

                }else{

                    //for(String item: ReleaseHelpActivity.selectMainTainTitle){
                        if (TextUtils.equals(s.getBrand_id(), project_brand+"")){
                            baseViewHolder.setTextColorRes(R.id.recommend_tv, R.color.white);
                            baseViewHolder.setBackgroundRes(R.id.recommend_tv, R.drawable.btn_blue_bg);
                        }else{
                            baseViewHolder.setTextColorRes(R.id.recommend_tv, R.color.nav_gray);
                            baseViewHolder.setBackgroundRes(R.id.recommend_tv, R.drawable.btn_blank_bg);
                        }
                    //}

                    baseViewHolder.setOnClickListener(R.id.recommend_tv, v -> {
                        if(TextUtils.equals(s.getBrand_id(), project_brand+"")){
                            v.setBackgroundResource(R.drawable.btn_blank_bg);
                            ((TextView) v).setTextColor(getResources().getColor(R.color.gray));
//                            ReleaseHelpActivity.selectMainTainTitle.remove(s.getBrand_id());
                        }else {
                            v.setBackgroundResource(R.drawable.btn_blue_bg);
                            ((TextView) v).setTextColor(Color.WHITE);
//                            ReleaseHelpActivity.selectMainTainTitle.add(s.getBrand_id());
//                            if(!ReleaseHelpActivity.typeIDs.contains(bean.getClass_id())){
//                                ReleaseHelpActivity.typeIDs.add(bean.getClass_id());
//                            }
                        }

                        project_brand = Integer.valueOf(s.getBrand_id());
                        project_class = Integer.valueOf(bean.getClass_id());
                        project_type = 2;
                        projectClassName = bean.getClass_name();
                        projectBrandName = s.getBrand_name();
                        projectTypeName = "保养";
                        adapter.notifyDataSetChanged();

//                        if(ReleaseHelpActivity.mainTainNames.contains(bean.getClass_name()+"-"+s.getBrand_name())){
//                            ReleaseHelpActivity.mainTainNames.remove(bean.getClass_name()+"-"+s.getBrand_name());
//                        }else{
//                            ReleaseHelpActivity.mainTainNames.add(bean.getClass_name()+"-"+s.getBrand_name());
//                        }
//
//                        if(isContain(ReleaseHelpActivity.mainTainIDs, s.getBrand_id())){
//                            ReleaseHelpActivity.mainTainIDs.remove(s.getBrand_id());
//                        }else{
//                            ReleaseHelpActivity.mainTainIDs.add(s.getBrand_id());
//                        }



                    });

                }


            }
        };



        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView.setAdapter(adapter);
        adapter.addAll(listBean);

        return view;
    }

    public void update(){
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    private boolean isContain(List<String> list, String s){
        for(String item: list){
            if (TextUtils.equals(s, item)){
                return true;
            }
        }
        return false;
    }


    public String getTitle() {
        return mParam1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
