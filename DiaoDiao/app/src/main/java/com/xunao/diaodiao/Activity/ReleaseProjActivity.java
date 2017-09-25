package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.xunao.diaodiao.Present.ReleaseProjPresenter;
import com.xunao.diaodiao.R;
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

    private String[] skills = {"家电维修", "水泥回填", "家睡维修", "水点回填", "国电维修", "水我回填"};
    private List<String> skillsName = new ArrayList<>();

    private String[] skillsSecond = {"家电维修", "水泥回填", "家睡维修", "水点回填", "国电维修", "水我回填"};
    private List<String> skillsNameSecond = new ArrayList<>();

    private String[] skillsThird = {"家电维修", "水泥回填", "家睡维修", "水点回填", "国电维修", "水我回填"};
    private List<String> skillsNameThird = new ArrayList<>();

    private String[] skillsFourth = {"家电维修", "水泥回填", "家睡维修", "水点回填", "国电维修", "水我回填"};
    private List<String> skillsNameFourth = new ArrayList<>();

    private RecyclerArrayAdapter<String> firstAdapter;
    private RecyclerArrayAdapter<String> secondAdapter;
    private RecyclerArrayAdapter<String> thirdAdapter;
    private RecyclerArrayAdapter<String> fourthAdapter;

    private List<String> allSelectList = new ArrayList<>();

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
                });
            }
        };

        firstAdapter.setOnItemClickListener((view, i) -> {
            String skillItem = firstAdapter.getAllData().get(i);
            if (skillsName.toString().contains(skillItem)) {
                view.setBackgroundResource(R.drawable.btn_blank_bg);
                ((TextView) view.findViewById(R.id.skill_text)).setTextColor(getResources().getColor(R.color.gray));
                skillsName.remove(skillItem);
            } else {
                view.setBackgroundResource(R.drawable.btn_blue_bg);
                ((TextView) view.findViewById(R.id.skill_text)).setTextColor(Color.WHITE);
                skillsName.add(skillItem);
            }
            setSelect(skillItem);
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        firstRecyclerView.setLayoutManager(linearLayoutManager);
        firstRecyclerView.setAdapter(firstAdapter);
        firstAdapter.addAll(skills);


        secondAdapter = new RecyclerArrayAdapter<String>(this, R.layout.select_skill_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.skill_text, s);

                if (skillsNameSecond.toString().contains(s)) {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blue_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.white);
                } else {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blank_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.gray);
                }

//                baseViewHolder.setOnClickListener(R.id.skill_text, v -> {
//                    if (skillsNameSecond.toString().contains(s)) {
//                        v.setBackgroundResource(R.drawable.btn_blank_bg);
//                        ((TextView) v).setTextColor(getResources().getColor(R.color.gray));
//                        skillsNameSecond.remove(s);
//                    } else {
//                        v.setBackgroundResource(R.drawable.btn_blue_bg);
//                        ((TextView) v).setTextColor(Color.WHITE);
//                        skillsNameSecond.add(s);
//                    }
//                });
            }
        };

        secondAdapter.setOnItemClickListener((view, i) -> {
            String skillItem = secondAdapter.getAllData().get(i);
            if (skillsNameSecond.toString().contains(skillItem)) {
                view.setBackgroundResource(R.drawable.btn_blank_bg);
                ((TextView) view.findViewById(R.id.skill_text)).setTextColor(getResources().getColor(R.color.gray));
                skillsNameSecond.remove(skillItem);
            } else {
                view.setBackgroundResource(R.drawable.btn_blue_bg);
                ((TextView) view.findViewById(R.id.skill_text)).setTextColor(Color.WHITE);
                skillsNameSecond.add(skillItem);
            }
            setSelect(skillItem);

        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        secondRecyclerView.setLayoutManager(manager);
        secondRecyclerView.setAdapter(secondAdapter);
        secondAdapter.addAll(skillsSecond);


        thirdAdapter = new RecyclerArrayAdapter<String>(this, R.layout.select_skill_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.skill_text, s);

                if (skillsNameThird.toString().contains(s)) {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blue_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.white);
                } else {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blank_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.gray);
                }

//                baseViewHolder.setOnClickListener(R.id.skill_text, v -> {
//                    if (skillsNameThird.toString().contains(s)) {
//                        v.setBackgroundResource(R.drawable.btn_blank_bg);
//                        ((TextView) v).setTextColor(getResources().getColor(R.color.gray));
//                        skillsNameThird.remove(s);
//                    } else {
//                        v.setBackgroundResource(R.drawable.btn_blue_bg);
//                        ((TextView) v).setTextColor(Color.WHITE);
//                        skillsNameThird.add(s);
//                    }
//                });
            }
        };

        thirdAdapter.setOnItemClickListener((view, i) -> {
            String skillItem = thirdAdapter.getAllData().get(i);
            if (skillsNameThird.toString().contains(skillItem)) {
                view.setBackgroundResource(R.drawable.btn_blank_bg);
                ((TextView) view.findViewById(R.id.skill_text)).setTextColor(getResources().getColor(R.color.gray));
                skillsNameThird.remove(skillItem);
            } else {
                view.setBackgroundResource(R.drawable.btn_blue_bg);
                ((TextView) view.findViewById(R.id.skill_text)).setTextColor(Color.WHITE);
                skillsNameThird.add(skillItem);
            }
            setSelect(skillItem);
        });

        LinearLayoutManager managerThird = new LinearLayoutManager(this);
        managerThird.setOrientation(LinearLayoutManager.HORIZONTAL);
        thirdRecyclerView.setLayoutManager(managerThird);
        thirdRecyclerView.setAdapter(thirdAdapter);
        thirdAdapter.addAll(skillsThird);

        setRecyclerView();

    }

    private void setRecyclerView() {
        fourthAdapter = new RecyclerArrayAdapter<String>(this, R.layout.select_skill_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.skill_text, s);

                if (skillsNameFourth.toString().contains(s)) {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blue_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.white);
                } else {
                    baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blank_bg);
                    baseViewHolder.setTextColorRes(R.id.skill_text, R.color.gray);
                }

//                baseViewHolder.setOnClickListener(R.id.skill_text, v -> {
//                    if (skillsNameFourth.toString().contains(s)) {
//                        v.setBackgroundResource(R.drawable.btn_blank_bg);
//                        ((TextView) v).setTextColor(getResources().getColor(R.color.gray));
//                        skillsNameFourth.remove(s);
//                    } else {
//                        v.setBackgroundResource(R.drawable.btn_blue_bg);
//                        ((TextView) v).setTextColor(Color.WHITE);
//                        skillsNameFourth.add(s);
//                    }
//                });
            }
        };

        fourthAdapter.setOnItemClickListener((view, i) -> {
            String skillItem = fourthAdapter.getAllData().get(i);
            if (skillsNameFourth.toString().contains(skillItem)) {
                view.setBackgroundResource(R.drawable.btn_blank_bg);
                ((TextView) view.findViewById(R.id.skill_text)).setTextColor(getResources().getColor(R.color.gray));
                skillsNameFourth.remove(skillItem);
            } else {
                view.setBackgroundResource(R.drawable.btn_blue_bg);
                ((TextView) view.findViewById(R.id.skill_text)).setTextColor(Color.WHITE);
                skillsNameFourth.add(skillItem);
            }
            setSelect(skillItem);

        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        fourthRecyclerView.setLayoutManager(linearLayoutManager);
        fourthRecyclerView.setAdapter(fourthAdapter);
        fourthAdapter.addAll(skillsFourth);
    }

    private void setSelect(String item){
        if (allSelectList.contains(item)){
            allSelectList.remove(item);
        }else{
            allSelectList.add(item);
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
                ReleaseProjSecondActivity.startActivity(this);
                break;
        }
    }
}
