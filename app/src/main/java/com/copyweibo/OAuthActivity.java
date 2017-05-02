package com.copyweibo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.copyweibo.Util.CommonTool;

public class OAuthActivity extends AppCompatActivity {

    //保存数据信息
    private SharedPreferences preferences;
    private static final String TAG = "OAuthActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CommonTool.checkNetWork(OAuthActivity.this);
        //获取只能本Activity读写的SharedPreferences对象
        preferences = getSharedPreferences("OAuth2.0", Context.MODE_PRIVATE);
        //WebView加载回调页
        WebView webView = (WebView) findViewById(R.id.webview);
        //管理WebView
        WebSettings webSettings = webView.getSettings();
        //启用JavaScript调用功能
        webSettings.setJavaScriptEnabled(true);
        //启用缩放网页功能
        webSettings.setSupportZoom(true);
        //获取焦点
        webView.requestFocus();

        String url = "https://open.weibo.cn/oauth2/authorize";
        webView.loadUrl(url);




    }
}
