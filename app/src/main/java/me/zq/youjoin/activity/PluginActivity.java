package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;

public class PluginActivity extends BaseActivity {

    @Bind(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        ButterKnife.bind(this);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });

//        webView.loadUrl("file:///mnt/sdcard/YouJoinFiles/pintu/demo.html");
        webView.loadUrl("file:///android_asset/pintu/demo.html");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                actionBar.setElevation(0);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
//                this.finish();
            default:
                this.finish();
                return super.onOptionsItemSelected(item);
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PluginActivity.class);
        context.startActivity(intent);
    }
}
