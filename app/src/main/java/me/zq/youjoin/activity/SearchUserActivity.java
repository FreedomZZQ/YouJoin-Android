package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.network.ResponseListener;

public class SearchUserActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_user_fab)
    FloatingActionButton searchUserFab;
    @Bind(R.id.search_content)
    EditText searchContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        searchUserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSearch();

            }
        });

    }

    private void doSearch() {
        String search = searchContent.getText().toString();
        if (search.equals("")) {
            searchContent.setError("Please input userid or email or username.");
        } else {
            NetworkManager.postRequestUserInfo(search, new ResponseListener<UserInfo>() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    searchContent.setError(volleyError.toString());
                }

                @Override
                public void onResponse(UserInfo info) {
                    if (info.getResult().equals("sueecss")) {

                    } else {
                        Toast.makeText(SearchUserActivity.this, "Request UserInfo failure!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchUserActivity.class);
        context.startActivity(intent);
    }

}
