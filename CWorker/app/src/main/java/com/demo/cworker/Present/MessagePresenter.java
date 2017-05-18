package com.demo.cworker.Present;

import com.demo.cworker.Model.MessageModel;
import com.demo.cworker.View.MessageView;

import javax.inject.Inject;

import rx.Subscriber;

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
