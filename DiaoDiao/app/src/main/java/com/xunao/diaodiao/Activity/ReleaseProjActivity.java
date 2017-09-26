package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Bean.ReleaseProjReq;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Present.ReleaseProjPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ReleaseProjView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class ReleaseProjActivity extends BaseActivity implements ReleaseProjView, View.OnClickListener {

    @Inject
    ReleaseProjPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.next)
    LinearLayout next;
    @BindView(R.id.first_recycler_view)
    RecyclerView firstRecyclerView;
    @BindView(R.id.second_recycler_view)
    RecyclerView secondRecyclerView;
    @BindView(R.id.third_recycler_view)
    RecyclerView thirdRecyclerView;
    @BindView(R.id.fourth_recycler_view)
    RecyclerView fourthRecyclerView;
    @BindView(R.id.all_select)
    TextView allSelect;

    private List<TypeInfoRes.Type_Info> skills = new ArrayList<>();
    private List<String> skillsName = new ArrayList<>();


    private RecyclerArrayAdapter<String> firstAdapter;

    private RecyclerArrayAdapter<TypeInfoRes.Type_Info> adapter;

    private List<String> allSelectList = new ArrayList<>();
    private ReleaseProjReq req = new ReleaseProjReq();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseProjActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_proj);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "选择项目");
        next.setOnClickListener(this);

        adapter = new RecyclerArrayAdapter<TypeInfoRes.Type_Info>(this, R.layout.type_info_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, TypeInfoRes.Type_Info type_info) {
                if (Integer.valueOf(type_info.getParent_id()) == 0){
                    baseViewHolder.setText(R.id.type, type_info.getTitle());
                }else{
                    RecyclerView recyclerView = baseViewHolder.getView(R.id.recycler_view);
                    recyclerView.setAdapter(firstAdapter);

                }
            }
        };

        firstAdapter = new RecyclerArrayAdapter<String>(this, R.layout.select_skill_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.skill_text, s);

                if (skillsName.toString().contains(s)) {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blue_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.white);
                } else {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blank_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.gray);
                }

                baseViewHolder.setOnClickListener(R.id.skill_text, v -> {
                    if (skillsName.toString().contains(s)) {
                        v.setBackgroundResource(R.drawable.btn_blank_bg);
                        ((TextView) v).setTextColor(getResources().getColor(R.color.gray));
                        skillsName.remove(s);
                    } else {
                        v.setBackgroundResource(R.drawable.btn_blue_bg);
                        ((TextView) v).setTextColor(Color.WHITE);
                        skillsName.add(s);
                    }
                    setSelect(s);
                });
            }
        };
        presenter.getTypeInfo(this);
    }

    @Override
    public void getData(TypeInfoRes res) {
        adapter.addAll(res.getType_info());

    }

    private void setSelect(String item){
        if (allSelectList.contains(item)){
            allSelectList.remove(item);
        }else{
            allSelectList.add(item);
        }
        if (allSelectList.size() > 0) {
            allSelect.setText(allSelectList.toString().subSequence(1, allSelectList.toString().length() - 1));
        }else{
            allSelectList.clear();
            allSelect.setText("");
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                if (TextUtils.isEmpty(allSelect.getText().toString())){
                    ToastUtil.show("请选择");
                    return;
                }
                req.setProject_class(allSelect.getText().toString());
                ReleaseProjSecondActivity.startActivity(this, req);
                break;
        }
    }


}
