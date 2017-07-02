package com.seafire.cworker.Present;

import com.seafire.cworker.Model.MessageModel;
import com.seafire.cworker.View.MessageView;

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
