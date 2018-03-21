package de.wellenvogel.android.wvtest;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private String queryUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b=getIntent().getExtras();
        if (b!=null){
            queryUrl =b.getString(Constants.URLKEY);
        }
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 16){
            try {
                WebSettings settings = mWebView.getSettings();
                Method m = WebSettings.class.getDeclaredMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                m.setAccessible(true);
                m.invoke(settings, true);
            }catch (Exception e){}
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            try {
                Method m=WebView.class.getDeclaredMethod("setWebContentsDebuggingEnabled",boolean.class);
                m.setAccessible(true);
                m.invoke(mWebView,true);
            } catch (Exception e) {
            }
        }
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.startsWith(Constants.QUERYURL)){
                    try {
                        URL cu=new URL(queryUrl);
                        URLConnection con=cu.openConnection();
                        InputStream is=con.getInputStream();
                        return new WebResourceResponse(null,null,is);
                    } catch (Exception e) {
                        Log.e("HTPP","unable to query "+queryUrl+": "+e);
                        return null;
                    }
                }
                else {
                    return super.shouldInterceptRequest(view, url);
                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mWebView.loadUrl("file:///android_asset/index.html");
    }
}
