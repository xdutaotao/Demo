package com.demo.cworker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.cworker.Bean.HomeResponseBean;
import com.demo.cworker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.demo.cworker.Common.Constants.INTENT_KEY;

public class HomeDetailActivity extends BaseActivity {
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.author)
    TextView author;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.vip)
    TextView vip;
    @BindView(R.id.read)
    Button read;
    @BindView(R.id.content)
    TextView content;
    private HomeResponseBean.TopicBean.GroupDataBean bean;

    public static void startActivity(Context context, HomeResponseBean.TopicBean.GroupDataBean bean) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_KEY, bean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_detail);
        ButterKnife.bind(this);

        if (getIntent().getSerializableExtra(INTENT_KEY) != null) {
            bean = (HomeResponseBean.TopicBean.GroupDataBean) getIntent().getSerializableExtra(INTENT_KEY);
            showToolbarBack(toolBar, titleText, bean.getTitle());
            Glide.with(this).load(bean.getPic()).placeholder(R.drawable.ic_launcher_round).into(image);
            title.setText(bean.getTitle());
            author.setText("作者:" + bean.getAuthor());
            time.setText("上传时间:" + bean.getUpdateTime());
            vip.setVisibility(bean.getVipRes() != 0 ? View.VISIBLE : View.GONE);
            content.setText(bean.getDescription());
        }

        read.setOnClickListener(v -> {

        });
    }
}
