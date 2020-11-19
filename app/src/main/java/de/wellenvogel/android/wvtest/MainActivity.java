package de.wellenvogel.android.wvtest;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
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
        //getSupportActionBar().hide();
        setContentView(R.layout.content_main);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.startsWith(Constants.ASSETS)) {
                    String remain=url.substring(Constants.ASSETS.length()+1);
                    if (remain.startsWith(Constants.QUERYURL)) {
                        try {
                            URL cu = new URL(queryUrl);
                            URLConnection con = cu.openConnection();
                            InputStream is = con.getInputStream();
                            return new WebResourceResponse(con.getContentType(), con.getContentEncoding(), is);
                        } catch (Exception e) {
                            Log.e("HTTP", "unable to query " + queryUrl + ": " + e);
                            return new WebResourceResponse(null,null,404,e.getLocalizedMessage(),null,null);
                        }
                    }
                    else{
                        try {
                            InputStream is=getAssets().open(remain);
                            return new WebResourceResponse(URLConnection.guessContentTypeFromName(remain),null,is);
                        } catch (IOException e) {
                            Log.e("HTTP","unable to load asset "+remain+": "+e);
                            return null;
                        }
                    }
                }
                else {
                    return super.shouldInterceptRequest(view, url);
                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //we fake some http url to allow xml http requests
        mWebView.loadUrl(Constants.ASSETS+"/index.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.action_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i=new Intent(this,StartActivity.class);
        i.putExtra(Constants.FORCEPREF,true);
        startActivity(i);
        finish();
        return true;
    }
}
