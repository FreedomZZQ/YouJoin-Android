package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PluginActivity.class);
        context.startActivity(intent);
    }
}
