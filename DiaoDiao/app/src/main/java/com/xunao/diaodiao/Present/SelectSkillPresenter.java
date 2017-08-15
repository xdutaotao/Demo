package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.SelectSkillModel;
import com.xunao.diaodiao.View.SelectSkillView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class SelectSkillPresenter extends BasePresenter<SelectSkillView> {
    @Inject
    SelectSkillModel model;

    @Inject
    SelectSkillPresenter() {
    }
}
