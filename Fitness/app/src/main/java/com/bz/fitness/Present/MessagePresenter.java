package com.bz.fitness.Present;

import com.bz.fitness.Model.MessageModel;
import com.bz.fitness.View.MessageView;

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
