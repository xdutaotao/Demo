package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.EditSkillModel;
import com.xunao.diaodiao.View.EditSkillView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by
 */
public class EditSkillPresenter extends BasePresenter<EditSkillView> {
    @Inject
    EditSkillModel model;

    @Inject
    EditSkillPresenter() {
    }
}
