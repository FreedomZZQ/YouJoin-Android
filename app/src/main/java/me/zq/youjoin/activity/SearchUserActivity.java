package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;

public class SearchUserActivity extends BaseActivity
implements DataPresenter.GetUserInfo{

    @Bind(R.id.search_user_fab)
    FloatingActionButton searchUserFab;
    @Bind(R.id.search_content)
    EditText searchContent;
    @Bind(R.id.search_result_list)
    ListView searchResultList;

    List<UserInfo> searchResult = new ArrayList<>();
    SearchResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ButterKnife.bind(this);

        adapter = new SearchResultAdapter(SearchUserActivity.this);
        searchResultList.setAdapter(adapter);
        searchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfoActivity.actionStart(SearchUserActivity.this, UserInfoActivity.TYPE_OTHER_USER,
                        searchResult.get(position).getId());
            }
        });

        searchUserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSearch();

            }
        });
    }

    @Override
    public void onGetUserInfo(UserInfo userInfo){
        if(userInfo.getResult().equals(NetworkManager.SUCCESS)){
            searchResult.add(userInfo);
            adapter.notifyDataSetChanged();
        }else{
            Toast.makeText(SearchUserActivity.this, getString(R.string.error_network),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void doSearch() {
        searchResult.clear();
        adapter.notifyDataSetChanged();
        String search = searchContent.getText().toString();
        if (search.equals("")) {
            searchContent.setError("Please input userid or email or username.");
        } else {
            DataPresenter.requestUserInfoAuto(search, SearchUserActivity.this);
        }
    }

    public class SearchResultAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public SearchResultAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return searchResult.size();
        }

        @Override
        public Object getItem(int pos) {
            return searchResult.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //判断是否缓存
            if (convertView == null) {
                holder = new ViewHolder();
                //通过layoutInflater实例化布局
                convertView = mInflater.inflate(R.layout.userlist_item_content, null);
                holder.img = (CircleImageView) convertView.findViewById(R.id.icon);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                //通过tag找到缓存布局
                holder = (ViewHolder) convertView.getTag();
            }
            //设置布局中控件要显示的视图
            holder.img.setBackgroundResource(R.mipmap.ic_account_circle_white_48dp);
            holder.name.setText(searchResult.get(pos).getUsername());
            return convertView;
        }

        public final class ViewHolder {
            public CircleImageView img;
            public TextView name;
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchUserActivity.class);
        context.startActivity(intent);
    }

}
