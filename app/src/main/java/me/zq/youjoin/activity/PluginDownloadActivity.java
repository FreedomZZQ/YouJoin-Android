package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import me.zq.youjoin.R;

public class PluginDownloadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_download);
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, PluginDownloadActivity.class);
        context.startActivity(intent);
    }
}
