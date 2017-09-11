package com.shenni.lives.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shenni.lives.R;
import com.shenni.lives.utils.Constants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 去掉s
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (-1 == resp.errCode) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.wx_pay_callback_msg)
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        })
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();

            } else {
                finish();
            }
            if (1 == 2) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.app_tip);
                builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
                builder.show();
            }


//			0	成功	展示成功页面
//			-1	错误	可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
//			-2	用户取消	无需处理。发生场景：用户不支付了，点击取消，返回APP。

        }
    }
}