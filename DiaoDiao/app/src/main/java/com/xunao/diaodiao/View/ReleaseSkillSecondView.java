package com.xunao.diaodiao.View;

import com.xunao.diaodiao.Bean.OddFeeRes;
import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.View.BaseView;

/**
 * Created by
 */
public interface ReleaseSkillSecondView extends BaseView {
    void getData(ReleaseProjRes res);
    void getData(Object res);
    void getData(OddFeeRes res);
}
