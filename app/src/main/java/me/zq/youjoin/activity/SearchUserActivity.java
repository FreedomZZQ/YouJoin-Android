package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
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

    List<UserInfo> searchResult = new ArrayList<>();

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
                        searchResult.add(info);

                    } else {
                        Toast.makeText(SearchUserActivity.this, "Request UserInfo failure!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public class SearchResultAdapter extends BaseAdapter{

        private LayoutInflater mInflater;

        public SearchResultAdapter(Context context){
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount(){
            return searchResult.size();
        }

        @Override
        public Object getItem(int pos){
            return searchResult.get(pos);
        }

        @Override
        public long getItemId(int pos){
            return pos;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent){
            ViewHolder holder = null;
            //判断是否缓存
            if(convertView == null){
                holder = new ViewHolder();
                //通过layoutInflater实例化布局
                convertView = mInflater.inflate(R.layout.userlist_item, null);
                holder.img = (CircleImageView) convertView.findViewById(R.id.icon);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            }else{
                //通过tag找到缓存布局
                holder = (ViewHolder) convertView.getTag();
            }
            //设置布局中控件要显示的视图
            holder.img.setBackgroundResource(R.mipmap.ic_account_circle_white_48dp);
            holder.name.setText(searchResult.get(pos).getUsername());
            return convertView;
        }

        public final class ViewHolder{
            public CircleImageView img;
            public TextView name;
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchUserActivity.class);
        context.startActivity(intent);
    }

}
