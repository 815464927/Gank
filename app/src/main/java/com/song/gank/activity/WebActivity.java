package com.song.gank.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.song.gank.R;
import com.song.gank.consts.Consts;
import com.song.gank.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity {


    @BindView(R.id.wv_activity)
    WebView mVebView;
    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_web_title)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        InitData();
    }

    private void InitData() {
        ButterKnife.bind(this);
        String mUrl = getIntent().getStringExtra(Consts.GANK_WEB);

        WebSettings webSettings = mVebView.getSettings();
        //支持JS
        webSettings.setJavaScriptEnabled(true);
        //允许使用数据库api
        webSettings.setDomStorageEnabled(true);
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);
        //缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //支持变焦
        webSettings.setSupportZoom(true);
        //支持缩放
        webSettings.setBuiltInZoomControls(true);
        //支持内容重新布局(4种)
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        //设置进度条
        mVebView.setWebChromeClient(new webClient());
        //设置webVewclient 不调用系统浏览器 在本webView中显示
        mVebView.setWebViewClient(new mWebViewClient());

        mVebView.loadUrl(mUrl);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mVebView.canGoBack()){
                mVebView.goBack();
            }else{
                finish();
            }
        }
        return true;
    }

    class webClient extends WebChromeClient {
        //设置进度条
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress == 100){
                mProgressBar.setVisibility(View.INVISIBLE);
            }else{
                if(View.INVISIBLE == mProgressBar.getVisibility()){
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            String t = mVebView.getTitle();
            mTitle.setText(t == null ? "":t);
            super.onReceivedTitle(view, title);
        }
    }

    //控制新的连接在当前WebView中打开
    class mWebViewClient extends WebViewClient {
        //网页开始加载
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtils.debug("onStart",url);
            super.onPageStarted(view, url, favicon);
        }
        //网页加载完毕
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
}
