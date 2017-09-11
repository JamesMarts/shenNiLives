package com.shenni.lives.activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shenni.lives.R;
import com.shenni.lives.base.BaseActivity;
import com.shenni.lives.utils.DialogUtil;
import com.shenni.lives.utils.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class WebActivity extends BaseActivity {

    @InjectView(R.id.wv)
    WebView wv;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.inject(this);
        setTitleBar("");
        init();
    }

    public void init() {
        url = getIntent().getStringExtra("url");
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
        wv.setWebViewClient(new WebViewClient() {
//            覆写shouldOverrideUrlLoading实现内部显示网页
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view.loadUrl(url);
//                当你点击一个页面中一个链接时会经过shouldOverrideUrlLoading。return true时，你可以自己来处理这个url，webview则不再处理这个url；return false时，webview来处理这个url。
//                通俗的说，当返回true时，你点任何链接都是失效的，需要你自己跳转。return false时webview会自己跳转
                return false;
            }
        });

//        支付链接时仅使用这个方法
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress >= 30) {
                    //加载完成
                    DialogUtil.closeMyDialog();
                }
            }

        });

    }
}

