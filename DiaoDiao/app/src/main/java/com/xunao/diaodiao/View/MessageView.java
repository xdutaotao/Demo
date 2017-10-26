package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.MessageListRes;
import com.xunao.diaodiao.Bean.TypeInfoRes;

/**
 * Created by
 */
public interface MessageView extends BaseView {
    void getData(MessageListRes res);
    void getData(Object res);
}
