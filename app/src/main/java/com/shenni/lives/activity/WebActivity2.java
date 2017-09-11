package com.shenni.lives.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shenni.lives.R;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.utils.DialogUtil;
import com.shenni.lives.utils.StringUtil;
import com.shenni.lives.utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class WebActivity2 extends BaseActivity {

    private static final String TAG = "WebActivity2";
    @InjectView(R.id.wv)
    WebView wv;

    String url;
    private boolean isLoadUrl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.inject(this);
        setTitleBar("充值中");
        url = getIntent().getStringExtra("url");
//        url = "http://meimei.xipku.pw?pay=29";

        //				HTTPS://QR.ALIPAY.COM/FKX001613RAUBNJIPGHU3B
//				wxp://f2f0_9-L-4WuKqZ1x3oHWcLrsyuOOMdsAvci
//        webview.loadUrl("http://meimei.xipku.pw?pay=29");
        if (isAili(url))
            initAili();
        else
//            initWX();
            inteWXOhter();
    }


    public void inteWXOhter() {

        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastUtil.toast("请安装微信最新版！");
        }

    }

    public void initWX() {
        if (StringUtil.isNullOrEmpty(url)) {
            ToastUtil.toast(R.string.pay_null_tips);
            return;
        }
        if (!url.toLowerCase().startsWith("http"))
            url = "http://" + url;
        Log.e("url", url);
        if (!StringUtil.isNullOrEmpty(url)) {
            DialogUtil.showMyDialog(this);
        }
        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        wv.getSettings().setJavaScriptEnabled(true);  //支持javascript
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        wv.getSettings().setUseWideViewPort(true);
        //wv.getSettings().setSupportZoom(true);   //支持缩放
        wv.getSettings().setCacheMode(wv.getSettings().LOAD_CACHE_ELSE_NETWORK); //优先使用缓存
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.loadUrl(url);
//        wv.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
////                view.loadUrl(url);
//                return false;
//            }
//        });
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100) {
//                    finish();
                    //加载完成
                    DialogUtil.closeMyDialog();
                }
            }

        });

    }


    private void initAili() {
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // webSettings.setDatabaseEnabled(true);

        // 使用localStorage则必须打开
        webSettings.setDomStorageEnabled(true);

        webSettings.setGeolocationEnabled(true);
        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "访问的url地址：" + url);
                if (parseScheme(url)) {
                    try {
                        Uri uri = Uri.parse(url);
                        Intent intent;
                        intent = Intent.parseUri(url,
                                Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        // intent.setSelector(null);
                        startActivity(intent);

                    } catch (Exception e) {
                        ToastUtil.toast("请先安装支付宝");
                    }
                } else {
                    view.loadUrl(url);
                }

                return true;

            }

        });
    }


    public boolean parseScheme(String url) {

        if (url.contains("platformapi/startapp")) {

            return true;
        } else {

            return false;
        }
    }

    public boolean isAili(String url) {
        if (url.toLowerCase().contains("alipay")) {
            return true;
        } else {
            return false;
        }
    }
}

