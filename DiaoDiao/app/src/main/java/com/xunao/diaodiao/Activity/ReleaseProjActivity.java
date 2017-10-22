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
import com.xunao.diaodiao.Bean.FindProjDetailRes;
import com.xunao.diaodiao.Bean.OrderCompRes;
import com.xunao.diaodiao.Bean.ReleaseProjReq;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.ReleaseProjPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.ReleaseProjView;
import com.xunao.diaodiao.Widget.Tag.Tag;
import com.xunao.diaodiao.Widget.Tag.TagLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

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
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.all_select)
    TextView allSelect;

    private List<String> skillsName = new ArrayList<>();


    private RecyclerArrayAdapter<TypeInfoRes.Type_Info> firstAdapter;

    private RecyclerArrayAdapter<List<TypeInfoRes.Type_Info>> adapter;

    private List<String> allSelectList = new ArrayList<>();
    private List<String> allSelectName = new ArrayList<>();
    private ReleaseProjReq req = new ReleaseProjReq();
    private List<List<TypeInfoRes.Type_Info>> listData = new ArrayList<>();
    private TypeInfoRes response = new TypeInfoRes();
    private List<String> fatherIds = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseProjActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, ReleaseProjReq projectBean) {
        Intent intent = new Intent(context, ReleaseProjActivity.class);
        intent.putExtra(INTENT_KEY, projectBean);
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

        adapter = new RecyclerArrayAdapter<List<TypeInfoRes.Type_Info>>(this, R.layout.type_info_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, List<TypeInfoRes.Type_Info> type_info) {

                firstAdapter = new RecyclerArrayAdapter<TypeInfoRes.Type_Info>(ReleaseProjActivity.this, R.layout.select_skill_item) {
                    @Override
                    protected void convert(BaseViewHolder baseViewHolder, TypeInfoRes.Type_Info s) {
                        baseViewHolder.setText(R.id.skill_text, s.getTitle());

                        if (isContain(s.getId())) {
                            baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blue_bg);
                            baseViewHolder.setTextColorRes(R.id.skill_text, R.color.white);
                        } else {
                            baseViewHolder.setBackgroundRes(R.id.skill_text, R.drawable.btn_blank_bg);
                            baseViewHolder.setTextColorRes(R.id.skill_text, R.color.gray);
                        }

                        baseViewHolder.setOnClickListener(R.id.skill_text, v -> {
                            //采暖和新风不能同时选择
                            if((isContain("6") || isContain("7") || isContain("8") || isContain("9"))
                                    && Integer.valueOf(s.getId()) == 16){
                                ToastUtil.show("采暖和新风不能同时选择");
                                return;
                            }

                            if(isContain("16")
                                    && (Integer.valueOf(s.getId()) == 6 || Integer.valueOf(s.getId()) == 7
                                        || Integer.valueOf(s.getId()) == 8 || Integer.valueOf(s.getId()) == 9)){
                                ToastUtil.show("采暖和新风不能同时选择");
                                return;
                            }

                            //地暖和电地暖是唯一选择
//                            if (isContain("6") &&
//                                    Integer.valueOf(s.getId()) == 7) {
//                                ToastUtil.show("和地暖重复");
//                                return;
//                            }
//                            if (isContain("7") &&
//                                    Integer.valueOf(s.getId()) == 6) {
//                                ToastUtil.show("和电地暖重复");
//                                return;
//                            }
                            //暖气片明装和暗装是唯一选择
                            if (isContain("8") &&
                                    Integer.valueOf(s.getId()) == 9) {
                                ToastUtil.show("和暖气片明装重复");
                                return;
                            }
                            if (isContain("9") &&
                                    Integer.valueOf(s.getId()) == 8) {
                                ToastUtil.show("和暖气片暗装重复");
                                return;
                            }
                            //水处理只能选择一种
//                            if ((isContain("10")
//                                    || isContain("11")
//                                    || isContain("12")
//                                    || isContain("13"))
//                                    && !isContain(s.getId())
//                                    && (Integer.valueOf(s.getId()) > 9
//                                            && Integer.valueOf(s.getId()) < 14)){
//                                ToastUtil.show("水处理只能选择一个");
//                                return;
//                            }
                            //空调系统只能选择一种
                            if (isContain("14")
                                    && Integer.valueOf(s.getId()) == 15){
                                ToastUtil.show("空调系统只能选择一个");
                                return;
                            }
                            if (isContain("15")
                                    && Integer.valueOf(s.getId()) == 14){
                                ToastUtil.show("空调系统只能选择一个");
                                return;
                            }
                            //水泥回填选中时其他不能被选中
                            if (Integer.valueOf(s.getId()) == 17){
                                skillsName.clear();
                                allSelectList.clear();
                            }

                            if (isContain("17")){
                                skillsName.clear();
                                allSelectList.clear();
                            }

                            //空调和地暖不能同时被选中
                            if (Integer.valueOf(s.getId()) == 6
                                    && (isContain("14")
                                        || isContain("15"))){
                                ToastUtil.show("空调和地暖不能同时被选中");
                                return;
                            }

                            if (Integer.valueOf(s.getId()) == 7
                                    && (isContain("14")
                                    || isContain("15"))){
                                ToastUtil.show("空调和地暖不能同时被选中");
                                return;
                            }

                            if ((Integer.valueOf(s.getId()) == 14 || Integer.valueOf(s.getId()) == 15)
                                    && (isContain("6") || isContain("7")) ){
                                ToastUtil.show("空调和地暖不能同时被选中");
                                return;
                            }

                            if (isContain(s.getId())) {
                                v.setBackgroundResource(R.drawable.btn_blank_bg);
                                ((TextView) v).setTextColor(getResources().getColor(R.color.gray));
                                skillsName.remove(s.getId());
                            } else {
                                v.setBackgroundResource(R.drawable.btn_blue_bg);
                                ((TextView) v).setTextColor(Color.WHITE);
                                skillsName.add(s.getId());
                            }
                            setSelect(s.getId());
                        });
                    }
                };


                firstAdapter.clear();
                RecyclerView recyclerView = (RecyclerView) baseViewHolder.getConvertView().findViewById(R.id.first_recycler_view);
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(firstAdapter);

                List<TypeInfoRes.Type_Info> temp = new ArrayList<>();
                for(TypeInfoRes.Type_Info info : type_info){
                    if (Integer.valueOf(info.getParent_id()) == 0){
                        baseViewHolder.setText(R.id.type, info.getTitle());
                    }else{
                        temp.add(info);
                    }
                }
                firstAdapter.addAll(temp);

            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        presenter.getTypeInfo(this);

        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, Constants.DESTORY))
                .subscribe(s -> {
                    finish();
                });
    }
    
    private boolean isContain(String s){
        for(String item: skillsName){
            if (TextUtils.equals(s, item)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void getData(TypeInfoRes res) {
        response = res;

        for (TypeInfoRes.Type_Info typeInfo : res.getType_info()){
            if (Integer.valueOf(typeInfo.getParent_id()) == 0){
                List<TypeInfoRes.Type_Info> itemData = new ArrayList<>();
                itemData.add(typeInfo);
                listData.add(itemData);
            }
        }

        for (TypeInfoRes.Type_Info typeInfo : res.getType_info()){
            if (Integer.valueOf(typeInfo.getParent_id()) != 0){

                for(List<TypeInfoRes.Type_Info> list: listData){
                    List<TypeInfoRes.Type_Info> temp = new ArrayList<>();
                    temp.addAll(list);
                    for(TypeInfoRes.Type_Info info: temp){
                        if (TextUtils.equals(info.getId(), typeInfo.getParent_id())){
                            list.add(typeInfo);
                        }
                    }
                }
            }
        }


        adapter.addAll(listData);

    }

    private void setSelect(String item){
        if (allSelectList.contains(item)){
            allSelectList.remove(item);
        }else{
            allSelectList.add(item);
        }
        if (allSelectList.size() > 0) {
            idsToString();
        }else{
            allSelectList.clear();
            allSelect.setText("");
        }
        adapter.notifyDataSetChanged();
    }

    private void idsToString(){
        allSelectName.clear();
        fatherIds.clear();
        for(String s: allSelectList){
            for(TypeInfoRes.Type_Info info : response.getType_info()){
                if (TextUtils.equals(info.getId(), s)){
                    allSelectName.add(info.getTitle());
                    if (!fatherIds.contains(info.getParent_id()))
                        fatherIds.add(info.getParent_id());
                }
            }
        }
        allSelect.setText(allSelectName.toString().substring(1,
                allSelectName.toString().length() - 1));

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

                req.setProject_type(fatherIds.toString().substring(1,
                        fatherIds.toString().length() - 1));
                req.setProject_class(allSelectList.toString().substring(1,
                        allSelectList.toString().length() - 1));
                req.setProject_type_name(allSelect.getText().toString());
                req.setProject_type_class(allSelect.getText().toString());
                ReleaseProjSecondActivity.startActivity(this, req);
                break;
        }
    }


}
