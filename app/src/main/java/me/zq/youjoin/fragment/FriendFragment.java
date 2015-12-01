package me.zq.youjoin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.widget.sidebar.IndexableListView;
import me.zq.youjoin.widget.sidebar.StringMatcher;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


public class FriendFragment extends Fragment {

    ArrayList<UserInfo> mData = new ArrayList<>();
    //ArrayList<UserInfo> mSearchData = new ArrayList<>();

    @Bind(R.id.userlist)
    IndexableListView userlist;

    UserAdapter adapter = new UserAdapter();

    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        ButterKnife.bind(this, view);

        initUserList();

        return view;
    }

    private void initUserList(){

        adapter.initSection();

        userlist.setFastScrollEnabled(true);
        userlist.setFastScrollAlwaysVisible(true);
        userlist.setAdapter(adapter);

        for(int i = 0; i < 26; i++){
            for(int j = 0; j < 10; j++){
                UserInfo info = new UserInfo();
                char [] name = {(char)('A' + i), (char)('A' + i) };
                info.setUsername(new String(name));
                mData.add(info);
            }
        }

        adapter.notifyDataSetChanged();
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
                    UserInfo item = mData.get(i);
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
            final UserInfo data = mData.get(position);

            if (isSection(position)) {
                holder.divideTitle.setVisibility(View.VISIBLE);
                holder.divideTitle.setText(data.getFirstLetter());
            } else {
                holder.divideTitle.setVisibility(View.GONE);
            }

            holder.name.setText(data.getUsername());
            //iconfromNetwork(holder.icon, data.getImg_url());
            holder.icon.setImageResource(R.mipmap.ic_account_circle_white_48dp);
//            Picasso.with(getActivity())
//                    .load(data.getImg_url())
//                    .centerCrop()
//                    .into(holder.icon);

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
                            if (StringMatcher.match(((UserInfo) getItem(j)).getFirstLetter().toUpperCase(), String.valueOf(k)))
                                return j;
                        }
                    } else {
                        if (StringMatcher.match(((UserInfo) getItem(j)).getFirstLetter().toUpperCase(), String.valueOf(mSections.charAt(i))))
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
