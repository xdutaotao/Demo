package com.xunao.diaodiao.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xunao.diaodiao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommandActivity extends BaseActivity {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rating_star)
    RatingBar ratingStar;
    @BindView(R.id.percent)
    TextView percent;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.post)
    Button post;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RecommandActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommand);
        ButterKnife.bind(this);
        showToolbarBack(toolBar, titleText, "评价");

        post.setOnClickListener(v -> {

        });

    }
}
