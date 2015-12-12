package me.zq.youjoin.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.activity.SearchUserActivity;
import me.zq.youjoin.activity.UserInfoActivity;
import me.zq.youjoin.model.FriendsInfo;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.network.ResponseListener;
import me.zq.youjoin.utils.LogUtils;
import me.zq.youjoin.utils.StringUtils;
import me.zq.youjoin.widget.sidebar.IndexableListView;
import me.zq.youjoin.widget.sidebar.StringMatcher;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


public class FriendFragment extends BaseFragment {


    List<FriendsInfo.FriendsEntity> mData = new ArrayList<>();
    //ArrayList<UserInfo> mSearchData = new ArrayList<>();

    @Bind(R.id.userlist)
    IndexableListView userlist;

    UserAdapter adapter = new UserAdapter();
    @Bind(R.id.add_friend_fab)
    FloatingActionButton addFriendFab;
    @Bind(R.id.refresher)
    SwipeRefreshLayout refresher;

    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        ButterKnife.bind(this, view);

        addFriendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchUserActivity.actionStart(getActivity());
            }
        });

        refresher.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        initUserList();

        return view;
    }

    private void initUserList() {

        adapter.initSection();

        userlist.setFastScrollEnabled(true);
        userlist.setFastScrollAlwaysVisible(true);
        userlist.setAdapter(adapter);
        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String friendId = mData.get(position).getId();
                NetworkManager.postRequestUserInfo(friendId, new ResponseListener<UserInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }

                    @Override
                    public void onResponse(UserInfo info) {
                        if (info.getResult().equals(NetworkManager.SUCCESS)) {
                            UserInfoActivity.actionStart(getActivity(),
                                    UserInfoActivity.TYPE_OTHER_USER, info);
                        }
                    }
                });
            }
        });

        refreshData();

//        for (int i = 0; i < 26; i++) {
//            for (int j = 0; j < 10; j++) {
//                UserInfo info = new UserInfo();
//                char[] name = {(char) ('A' + i), (char) ('A' + i)};
//                info.setUsername(new String(name));
//                mData.add(info);
//            }
//        }
    }

    private void refreshData() {
        refresher.setRefreshing(true);
        NetworkManager.postRequestFriendList(YouJoinApplication.getCurrUser().getId(),
                new ResponseListener<FriendsInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        refresher.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(FriendsInfo info) {
                        mData = info.getFriends();
                        adapter.notifyDataSetChanged();
                        refresher.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    static class ViewHolder {
        ImageView icon;
        TextView name;
        //CheckBox mutual;
        TextView divideTitle;
    }

    class UserAdapter extends BaseAdapter implements SectionIndexer, StickyListHeadersAdapter {

        private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private ArrayList<String> mSectionTitle = new ArrayList<>();
        private ArrayList<Integer> mSectionId = new ArrayList<>();

        public void initSection() {
            mSectionTitle.clear();
            mSectionId.clear();

            if (mData.size() > 0) {
                String lastLetter = "";

                for (int i = 0; i < mData.size(); ++i) {
                    FriendsInfo.FriendsEntity item = mData.get(i);
                    if (!item.getFirstLetter().equals(lastLetter)) {
                        lastLetter = item.getFirstLetter();
                        mSectionTitle.add(item.getFirstLetter());
                        mSectionId.add(i);
                    }
                }
            }
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater mInflater = getActivity().getLayoutInflater();
                convertView = mInflater.inflate(R.layout.userlist_item, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
//                holder.mutual = (CheckBox) convertView.findViewById(R.id.followMutual);
//                if (hideFollowButton) {
//                    holder.mutual.setVisibility(View.INVISIBLE);
//                }
                holder.divideTitle = (TextView) convertView.findViewById(R.id.divideTitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final FriendsInfo.FriendsEntity data = mData.get(position);

            if (isSection(position)) {
                holder.divideTitle.setVisibility(View.VISIBLE);
                holder.divideTitle.setText(data.getFirstLetter());
            } else {
                holder.divideTitle.setVisibility(View.GONE);
            }

            holder.name.setText(data.getNickname());
            //iconfromNetwork(holder.icon, data.getImg_url());
            holder.icon.setImageResource(R.mipmap.ic_account_circle_white_48dp);

            Picasso.with(getActivity())
                    .load(StringUtils.getPicUrlList(data.getImg_url()).get(0))
                    .resize(200, 200)
                    .centerCrop()
                    .into(holder.icon);

//            if (!hideFollowButton) {
//                int drawableId = data.follow ? R.drawable.checkbox_fans : R.drawable.checkbox_follow;
//                holder.mutual.setButtonDrawable(drawableId);
//                holder.mutual.setChecked(data.followed);
//                holder.mutual.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        RequestParams params = new RequestParams();
//                        params.put("users", data.global_key);
//                        if (((CheckBox) v).isChecked()) {
//                            postNetwork(HOST_FOLLOW, params, HOST_FOLLOW, position, null);
//                        } else {
//                            postNetwork(HOST_UNFOLLOW, params, HOST_UNFOLLOW, position, null);
//                        }
//                    }
//                });
//            }

            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            initSection();
        }

        private boolean isSection(int pos) {
            if (getCount() == 0) {
                return true;
            }

            if (pos == 0) {
                return true;
            }

            String currentItem = mData.get(pos).getFirstLetter();
            String preItem = mData.get(pos - 1).getFirstLetter();
            return !currentItem.equals(preItem);
        }

        @Override
        public int getPositionForSection(int section) {
            // If there is no item for current section, previous section will be selected
            for (int i = section; i >= 0; i--) {
                for (int j = 0; j < getCount(); j++) {
                    if (i == 0) {
                        // For numeric section
                        for (int k = 0; k <= 9; k++) {
                            if (StringMatcher.match(((FriendsInfo.FriendsEntity)
                                    getItem(j)).getFirstLetter().toUpperCase(), String.valueOf(k)))
                                return j;
                        }
                    } else {
                        if (StringMatcher.match(((FriendsInfo.FriendsEntity)
                                getItem(j)).getFirstLetter().toUpperCase(), String.valueOf(mSections.charAt(i))))
                            return j;
                    }
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }

        @Override
        public Object[] getSections() {
            String[] sections = new String[mSections.length()];
            for (int i = 0; i < mSections.length(); i++)
                sections[i] = String.valueOf(mSections.charAt(i));
            return sections;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.dynamic_list_head, parent, false);
                holder.mHead = (TextView) convertView.findViewById(R.id.head);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }

            holder.mHead.setText(mSectionTitle.get(getSectionForPosition(position)));
            return convertView;
        }

        @Override
        public long getHeaderId(int i) {
            return getSectionForPosition(i);
        }

        class HeaderViewHolder {
            TextView mHead;
        }
    }
}
