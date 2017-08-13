package com.xunao.diaodiao.Present;

import com.xunao.diaodiao.Model.MessageModel;
import com.xunao.diaodiao.View.MessageView;

import javax.inject.Inject;

/**
 * Created by
 */
public class MessagePresenter extends BasePresenter<MessageView> {
    @Inject
    MessageModel model;

    @Inject
    MessagePresenter() {
    }
}
