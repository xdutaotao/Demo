package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.ReleaseSkillInforModel;
import com.xunao.diaodiao.View.ReleaseSkillInforView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseSkillInforPresenter extends BasePresenter<ReleaseSkillInforView> {
    @Inject
    ReleaseSkillInforModel model;

    @Inject
    ReleaseSkillInforPresenter() {
    }
}
