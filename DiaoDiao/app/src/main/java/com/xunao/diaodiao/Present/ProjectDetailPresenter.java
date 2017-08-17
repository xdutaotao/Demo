package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.ProjectDetailModel;
import com.xunao.diaodiao.View.ProjectDetailView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class ProjectDetailPresenter extends BasePresenter<ProjectDetailView> {
    @Inject
    ProjectDetailModel model;

    @Inject
    ProjectDetailPresenter() {
    }
}
