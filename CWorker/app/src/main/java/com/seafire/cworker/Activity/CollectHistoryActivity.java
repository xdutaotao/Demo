package com.seafire.cworker.Activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.gzfgeh.iosdialog.IOSDialog;
import com.seafire.cworker.Bean.CollectBean;
import com.seafire.cworker.R;
import com.seafire.cworker.Utils.JsonUtils;
import com.seafire.cworker.Utils.ShareUtils;
import com.seafire.cworker.Utils.ToastUtil;
import com.seafire.cworker.Utils.Utils;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seafire.cworker.Common.Constants.COLLECT_LIST;
import static com.seafire.cworker.Common.Constants.POST_COLLECT_TIME;

public class CollectHistoryActivity extends BaseActivity {
    public static final int REQUEST_CODE = 999;

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RecyclerArrayAdapter<CollectBean> adapter;
    private List<CollectBean> list = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CollectHistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_history);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "上传记录");



        adapter = new RecyclerArrayAdapter<CollectBean>(this, R.layout.collect_history_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, CollectBean bean) {
                baseViewHolder.setText(R.id.item_txt, bean.getPartCode());
                baseViewHolder.setText(R.id.item_right_txt, bean.getTime());
                baseViewHolder.setTextColor(R.id.item_txt, getResources().getColor(!bean.getIsSuccess() ? R.color.red : android.R.color.black));
                baseViewHolder.setTextColor(R.id.item_right_txt, getResources().getColor(!bean.getIsSuccess() ? R.color.red : android.R.color.black));
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            if(!list.get(i).getIsSuccess())
                CollectActivity.startActivityForResult(this, list.get(i));
        });

        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int i) {
                if (adapter.getAllData().get(i).getIsSuccess()){
                    new IOSDialog(CollectHistoryActivity.this).builder()
                            .setMsg("复制", v -> {
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                // 将文本内容放到系统剪贴板里。
                                cm.setText(adapter.getAllData().get(i).getPartCode());
                                ToastUtil.show("复制成功");
                            })
                            .show();
                }else{
                    new IOSDialog(CollectHistoryActivity.this).builder()
                            .setMsg("是否删除记录？")
                            .setNegativeButton("取消", null)
                            .setNegativeBtnColor(R.color.colorAccent)
                            .setPositiveBtnColor(R.color.colorAccent)
                            .setPositiveButton("确认", v -> {
                                deleteData(adapter.getAllData().get(i));
                            }).show();
                }
                return false;
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void deleteData(CollectBean bean){
        if (ShareUtils.getValue(COLLECT_LIST, null) != null) {
            List<CollectBean> collectBeanList  = JsonUtils.getInstance()
                    .JsonToCollectList(ShareUtils.getValue(COLLECT_LIST, null));

            List<CollectBean> newList = new ArrayList<>();
            for(CollectBean collectBean : collectBeanList){
                if (collectBean.getLongTime() != bean.getLongTime()){
                    newList.add(collectBean);
                }
            }

            ShareUtils.putValue(COLLECT_LIST, JsonUtils.getInstance().CollectListToJson(newList));
            ShareUtils.putValue(POST_COLLECT_TIME, newList.size());

            setData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setData(){
        if (ShareUtils.getValue(COLLECT_LIST, null) != null){
            list.clear();
            list.addAll(JsonUtils.getInstance()
                    .JsonToCollectList(ShareUtils.getValue(COLLECT_LIST, null)));


            List<CollectBean> collectBeanList = new ArrayList<>();
            for(CollectBean bean : list){
                if (bean.getIsSuccess()) {
                    if(TextUtils.equals(Utils.getNowTime(), bean.getTime())) {
                        collectBeanList.add(bean);
                    }

                }else {
                    collectBeanList.add(bean);
                }
            }
            adapter.clear();
            adapter.addAll(collectBeanList);
            ShareUtils.putValue(COLLECT_LIST, JsonUtils.getInstance().CollectListToJson(collectBeanList));
            ShareUtils.putValue(POST_COLLECT_TIME, collectBeanList.size());
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CODE){
                setData();
            }
        }
    }
}
