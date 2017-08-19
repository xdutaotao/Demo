package com.demo.step.Present;

import com.demo.step.Model.MessageModel;
import com.demo.step.View.MessageView;

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
