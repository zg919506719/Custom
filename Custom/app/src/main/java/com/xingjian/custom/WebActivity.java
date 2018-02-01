package com.xingjian.custom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by thinkpad on 2018/1/31.
 */

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/haha.html");
        WebSettings settings = webView.getSettings();
        //http://blog.csdn.net/l_serein/article/details/9007479
        //不显示缩放按钮，防止内存泄漏
        settings.setBuiltInZoomControls(false);
        //不支持缩放
        settings.setSupportZoom(false);
        settings.setUseWideViewPort(false);
        //支持内部javascript交互
        settings.setJavaScriptEnabled(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        //执行web里面的方法
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("javascript:add()");
            }
        });
        //web执行android方法，方法名字和最后的对象名称一致
        webView.addJavascriptInterface(this, "js");
    }

    @JavascriptInterface
    public void clickOnAndroid() {
        Toast.makeText(WebActivity.this, "从android传来的", Toast.LENGTH_LONG).show();
    }

}
