package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.xunao.diaodiao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

public class MessageDetail extends BaseActivity {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.message)
    TextView message;

    public static void startActivity(Context context, String content) {
        Intent intent = new Intent(context, MessageDetail.class);
        intent.putExtra(INTENT_KEY, content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);

        showToolbarBack(toolBar, titleText, "消息详情");

        message.setText(getIntent().getStringExtra(INTENT_KEY));
    }
}
