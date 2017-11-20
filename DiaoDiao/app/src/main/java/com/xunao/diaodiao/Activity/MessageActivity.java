package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.gzfgeh.GRecyclerView;
import com.gzfgeh.adapter.BaseViewHolder;
import com.gzfgeh.adapter.RecyclerArrayAdapter;
import com.gzfgeh.iosdialog.IOSDialog;
import com.xunao.diaodiao.Bean.MessageListRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;
import com.xunao.diaodiao.Present.MessagePresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.View.MessageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends BaseActivity implements MessageView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    @Inject
    MessagePresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    GRecyclerView recyclerView;

    private RecyclerArrayAdapter<MessageListRes.MessageBean> adapter;
    private SpannableStringBuilder builder;
    private int page = 1;

    MessageListRes.MessageBean select;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "我的消息");



        adapter = new RecyclerArrayAdapter<MessageListRes.MessageBean>(this, R.layout.message_item) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, MessageListRes.MessageBean s) {
                baseViewHolder.setText(R.id.title, s.getTitle());
                baseViewHolder.setText(R.id.look, s.getContent());
                baseViewHolder.setText(R.id.time, s.getCreate_time());

                if(s.getIs_read() == 2){
                    //未读
                    baseViewHolder.setTextColorRes(R.id.look, R.color.accept_btn_default);
                }

                if(s.getInfo_type() == 3){
                    baseViewHolder.setText(R.id.title, "我的消息");
                }
//                ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.accept_btn_default));
//                builder = new SpannableStringBuilder(s.getContent());
//                builder.setSpan(span, s.getContent().toString().length()-4,
//                        s.getContent().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        };

        adapter.setOnItemClickListener((view, i) -> {
            if(adapter.getAllData().get(i).getInfo_type() == 3){
                //不跳转

            }else{
                select = adapter.getAllData().get(i);
                presenter.readMessage(this, adapter.getAllData().get(i).getMessage_id());
            }

        });

        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int i) {

                new IOSDialog(MessageActivity.this).builder()
                        .setMsg("删除消息")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("删除", v -> {
                            presenter.cancelMessage(MessageActivity.this,
                                    adapter.getAllData().get(i).getMessage_id());
                        })
                        .show();


                return false;
            }
        });

        recyclerView.setAdapterDefaultConfig(adapter, this, this);
        //onRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void getData(MessageListRes res) {
        if(page == 1)
            adapter.clear();

        if(res.getMessage().size() != 10){
            adapter.stopMore();
        }

        adapter.addAll(res.getMessage());
    }

    @Override
    public void getData(Object res) {
        if(select.getInfo_type() == 1){
            //Web
            if(!TextUtils.isEmpty(select.getUrl()))
                WebViewDetailActivity.startActivity(this, select);
        }else{
            MessageDetail.startActivity(this, select.getContent());
        }
    }

    @Override
    public void deteleMsg(Object res) {
        ToastUtil.show("删除成功");
        onRefresh();
    }

    @Override
    public void onRefresh() {
        page = 1;
        presenter.messageList(this, page);
    }

    @Override
    public void onLoadMore() {
        page++;
        presenter.messageList(this, page);
    }


    @Override
    public void onFailure() {
        if(adapter.getAllData().size() > 0){
            adapter.stopMore();
        }else{
            recyclerView.showEmpty();
        }
    }


}
