package cn.bluemobi.dylan.step.step.accelerometer;

import android.widget.Toast;

/**
 * Created by dylan on 16/9/27.
 */

/*
* 根据StepDetector传入的步点"数"步子
* */
public class StepCount implements StepCountListener {

    private int mCount = 0;
    private StepValuePassListener mStepValuePassListener;
    private long timeOfLastPeak = 0;
    private long timeOfThisPeak = 0;
    private StepDetector stepDetector;

    public StepCount() {
        stepDetector = new StepDetector();
        stepDetector.initListener(this);
    }
    public StepDetector getStepDetector(){
        return stepDetector;
    }

    /*
        * 连续走十步才会开始计步
        * 连续走了9步以下,停留超过3秒,则计数清空
        *
        * 3s 更新一下UI
        * */
    @Override
    public void countStep() {
        this.timeOfLastPeak = this.timeOfThisPeak;
        this.timeOfThisPeak = System.currentTimeMillis();
        this.mCount++;
        if (this.timeOfThisPeak - this.timeOfLastPeak > 3000L) {
            notifyListener();
        }

    }

    public void initListener(StepValuePassListener listener) {
        this.mStepValuePassListener = listener;
    }

    public void notifyListener() {
        if (this.mStepValuePassListener != null)
            this.mStepValuePassListener.stepChanged(this.mCount);
    }


    public void setSteps(int initValue) {
        this.mCount = initValue;
        timeOfLastPeak = 0;
        timeOfThisPeak = 0;
        notifyListener();
    }
}
