package com.xunao.diaodiao.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xunao.diaodiao.Present.FeedBackDetailPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.View.FeedBackDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by
 */
public class FeedBackDetailActivity extends BaseActivity implements FeedBackDetailView {

    @Inject
    FeedBackDetailPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.rating_star)
    RatingBar ratingStar;
    @BindView(R.id.percent)
    TextView percent;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FeedBackDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_detail);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "我的评价");

        ratingStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                percent.setText(((int)rating)+"");
                ratingBar.setRating((int)rating);
            }
        });
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
