package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.ReleaseSkillModel;
import com.xunao.diaodiao.View.ReleaseSkillView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseSkillPresenter extends BasePresenter<ReleaseSkillView> {
    @Inject
    ReleaseSkillModel model;

    @Inject
    ReleaseSkillPresenter() {
    }
}
