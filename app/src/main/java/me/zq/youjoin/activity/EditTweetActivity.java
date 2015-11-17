package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import me.zq.youjoin.R;

public class EditTweetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tweet);
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, EditTweetActivity.class);
        context.startActivity(intent);
    }
}
