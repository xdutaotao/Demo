package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.ReleaseSkillSureInfoModel;
import com.xunao.diaodiao.View.ReleaseSkillSureInfoView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ReleaseSkillSureInfoPresenter extends BasePresenter<ReleaseSkillSureInfoView> {
    @Inject
    ReleaseSkillSureInfoModel model;

    @Inject
    ReleaseSkillSureInfoPresenter() {
    }
}
