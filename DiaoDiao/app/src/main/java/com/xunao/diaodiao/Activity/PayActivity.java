package com.xunao.diaodiao.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.gzfgeh.iosdialog.IOSDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xunao.diaodiao.Bean.AuthResult;
import com.xunao.diaodiao.Bean.BalancePayRes;
import com.xunao.diaodiao.Bean.PayFeeReq;
import com.xunao.diaodiao.Bean.PayRes;
import com.xunao.diaodiao.Bean.PayResult;
import com.xunao.diaodiao.Bean.ReleaseHelpReq;
import com.xunao.diaodiao.Bean.ReleaseProjRes;
import com.xunao.diaodiao.Bean.WeiXinPayRes;
import com.xunao.diaodiao.Common.Constants;
import com.xunao.diaodiao.Present.PayPresenter;
import com.xunao.diaodiao.R;
import com.xunao.diaodiao.Utils.RxBus;
import com.xunao.diaodiao.Utils.ToastUtil;
import com.xunao.diaodiao.Utils.Utils;
import com.xunao.diaodiao.View.PayView;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xunao.diaodiao.Common.Constants.INTENT_KEY;

/**
 * create by
 */
public class PayActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, PayView {

    @Inject
    PayPresenter presenter;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.pay)
    Button pay;
    @BindView(R.id.fee)
    TextView fee;
    @BindView(R.id.current)
    CheckBox current;
    @BindView(R.id.zhifubao)
    CheckBox zhifubao;
    @BindView(R.id.wechat)
    CheckBox wechat;
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.canclePay)
    Button canclePay;

    private ReleaseProjRes req;
    private int projType = 0;
    private PayFeeReq payFeeReq = new PayFeeReq();

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    public static boolean isWeixin = false;
    public static boolean wxFail = false;
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2017083108479643";
    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "";

    public static void startActivity(Context context, ReleaseProjRes req, int projType) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra(INTENT_KEY, req);
        intent.putExtra("projType", projType);
        context.startActivity(intent);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        paySuccess(new Object());
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        //销毁订单
                        payFeeReq.setOrder_no(req.getOrder_no());
                        //1项目2监理3零工4维保
                        payFeeReq.setProject_type(projType);
                        presenter.destoryOrder(PayActivity.this, payFeeReq);
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        showToolbarBack(toolBar, titleText, "支付");

        req = (ReleaseProjRes) getIntent().getSerializableExtra(INTENT_KEY);
        projType = getIntent().getIntExtra("projType", 0);

        fee.setText("￥ " + req.getTotal_fee() + "元");
        balance.setText("当前余额：" + req.getBalance() + "元");
        current.setChecked(true);
        if(Float.valueOf(req.getBalance()) > Float.valueOf(req.getTotal_fee())){
            zhifubao.setChecked(false);
            zhifubao.setFocusable(false);
            zhifubao.setFocusableInTouchMode(false);
            zhifubao.setClickable(false);

            wechat.setChecked(false);
            wechat.setFocusable(false);
            wechat.setFocusableInTouchMode(false);
            wechat.setClickable(false);

            payFeeReq.setIs_combination(0);
        }else{
            payFeeReq.setIs_combination(1);
        }

        pay.setOnClickListener(this);
        //current.setOnCheckedChangeListener(this);
        zhifubao.setOnCheckedChangeListener(this);
        wechat.setOnCheckedChangeListener(this);

        canclePay.setOnClickListener(this);

        RxBus.getInstance().toObservable(String.class)
                .filter(s -> TextUtils.equals(s, "weixinpay"))
                .subscribe(s -> {
                    paySuccess(new Object());
                });

    }

    private static final String APP_ID = "wxfa5af658b9f2e5d4";
    private IWXAPI api;

    private void registerWeiXin() {
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay:
                if (zhifubao.isChecked()) {
                    if(Float.valueOf(req.getBalance()) > 0){
                        payFeeReq.setOrder_no(req.getOrder_no());
                        payFeeReq.setPay_fee(req.getTotal_fee());
                        //1项目2监理3零工4维保
                        payFeeReq.setProject_type(projType);
                        presenter.balancePay(this, payFeeReq);
                    }else{
                        payFeeReq.setOrder_no(req.getOrder_no());
                        payFeeReq.setPrice(req.getTotal_fee());
                        payFeeReq.setBalance(req.getBalance());
                        presenter.aliPay(this, payFeeReq);
                    }

                } else if (wechat.isChecked()) {
                    //weixinPay();
                    //ToastUtil.show("敬请期待");
                    if(Float.valueOf(req.getBalance()) > 0){
                        payFeeReq.setOrder_no(req.getOrder_no());
                        payFeeReq.setPay_fee(req.getTotal_fee());
                        //1项目2监理3零工4维保
                        payFeeReq.setProject_type(projType);
                        presenter.balancePay(this, payFeeReq);
                    }else{
                        payFeeReq.setOrder_no(req.getOrder_no());
                        payFeeReq.setPrice(req.getTotal_fee());
                        payFeeReq.setIp(Utils.getHostIP());
                        payFeeReq.setBalance(req.getBalance());
                        presenter.wxPay(this, payFeeReq);
                    }

                }else {
                    if(Float.valueOf(req.getBalance()) < Float.valueOf(req.getTotal_fee())){
                        ToastUtil.show("余额不足");
                        return;
                    }
                    payFeeReq.setOrder_no(req.getOrder_no());
                    payFeeReq.setPay_fee(req.getTotal_fee());
                    //1项目2监理3零工4维保
                    payFeeReq.setProject_type(projType);
                    presenter.balancePay(this, payFeeReq);
                }


                break;


            case R.id.canclePay:

                payFeeReq.setOrder_no(req.getOrder_no());
                //1项目2监理3零工4维保
                payFeeReq.setProject_type(projType);
                presenter.cancelPublish(this, payFeeReq);
                break;
        }
    }


    @Override
    public void getData(BalancePayRes s) {
        //余额支付成功
        //presenter.paySuccess(this, payFeeReq);
        if(zhifubao.isChecked()){
            payFeeReq.setOrder_no(req.getOrder_no());
            payFeeReq.setPrice(s.getFee());
            payFeeReq.setBalance(req.getBalance());
            presenter.aliPay(this, payFeeReq);
        }else if(wechat.isChecked()){
            payFeeReq.setOrder_no(req.getOrder_no());
            payFeeReq.setPrice(s.getFee());
            payFeeReq.setIp(Utils.getHostIP());
            payFeeReq.setBalance(req.getBalance());
            presenter.wxPay(this, payFeeReq);
        }else{
            presenter.paySuccess(this, payFeeReq);
        }
    }

    @Override
    public void paySuccess(Object s) {
        new IOSDialog(this).builder()
                .setContentView(R.layout.pay_dialog)
                .setNegativeBtnColor(R.color.light_gray)
                .setNegativeButton("再次发布", v1 -> {

                    if (projType == 1) {
                        //项目
                        ReleaseProjActivity.startActivity(this, false);
                    } else if (projType == 3) {
                        //零工
                        ReleaseSkillActivity.startActivity(this);
                    }else if(projType == 4){
                        //维保
                        ReleaseHelpActivity.startActivity(this);
                    }else if(projType == 2){
                        //监理
                        ReleaseProjActivity.startActivity(this, true);
                    }
                    finish();
                })
                .setPositiveBtnColor(R.color.light_blank)
                .setPositiveButton("查看订单", v1 -> {
                    RxBus.getInstance().post(Constants.DESTORY);
                    WebViewActivity.startActivity(this, req.getUrl());
                    finish();
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        RxBus.getInstance().post(Constants.DESTORY);
                        finish();
                    }
                })
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .show();
    }

    @Override
    public void payAli(PayRes s) {
        String orderInfo = s.getOrderInfo();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public void payWX(PayRes res) {
        registerWeiXin();

        PayReq payReq = new PayReq();
        payReq.appId = APP_ID;
        payReq.partnerId = res.getPartnerid();
        payReq.prepayId = res.getPrepayid();
        payReq.packageValue = res.getPack_age();
        payReq.nonceStr = res.getNoncestr();
        payReq.timeStamp = res.getTimestamp();
        payReq.sign = res.getSign();

        api.sendReq(payReq);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(wxFail){
            //销毁订单
            payFeeReq.setOrder_no(req.getOrder_no());
            //1项目2监理3零工4维保
            payFeeReq.setProject_type(projType);
            presenter.destoryOrder(PayActivity.this, payFeeReq);
        }else if(isWeixin){
            //微信支付
            paySuccess(new Object());
        }
    }

    @Override
    public void canclePublish(Object s) {
        ToastUtil.show("取消订单成功");
        isWeixin = false;
        wxFail = false;
        RxBus.getInstance().post(Constants.DESTORY);
    }

    @Override
    public void destoryOrder(Object s) {
        ToastUtil.show("撤销订单成功");
        isWeixin = false;
        wxFail = false;
        finish();
    }

    @Override
    public void onFail(String s) {
        //余额支付失败
        ToastUtil.show(s);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            return;
        }


        switch (buttonView.getId()) {
            case R.id.current:
                //current.setChecked(isChecked);
                zhifubao.setChecked(!isChecked);
                wechat.setChecked(!isChecked);
                break;

            case R.id.zhifubao:
                //current.setChecked(!isChecked);
                zhifubao.setChecked(isChecked);
                wechat.setChecked(!isChecked);
                break;

            case R.id.wechat:
                //current.setChecked(!isChecked);
                zhifubao.setChecked(!isChecked);
                wechat.setChecked(isChecked);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onFailure() {
        ToastUtil.show("支付失败");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isWeixin = false;
        wxFail = false;
        presenter.detachView();

    }

}
