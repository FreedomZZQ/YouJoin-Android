package me.zq.youjoin.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.activity.SearchUserActivity;
import me.zq.youjoin.activity.UserInfoActivity;
import me.zq.youjoin.adapter.FriendListAdapter;
import me.zq.youjoin.event.FriendUpdateEvent;
import me.zq.youjoin.model.FriendsInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.widget.sidebar.CustomEditText;
import me.zq.youjoin.widget.sidebar.SideBar;


public class FriendFragment extends BaseFragment
        implements DataPresenter.GetFriendList, SideBar.OnTouchingLetterChangedListener, TextWatcher{

    private FriendListAdapter adapter;
    private List<FriendsInfo.FriendsEntity> dataList = new ArrayList<>();

    @Bind(R.id.add_friend_fab)
    FloatingActionButton addFriendFab;
    @Bind(R.id.refresher)
    SwipeRefreshLayout refresher;
    @Bind(R.id.search_input)
    CustomEditText searchInput;
    @Bind(R.id.friend_list)
    ListView friendList;
    @Bind(R.id.friend_dialog)
    TextView friendDialog;
    @Bind(R.id.friend_sidebar)
    SideBar friendSidebar;

    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.yj_fragment_friend, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        addFriendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchUserActivity.actionStart(getActivity());
            }
        });

        refresher.setColorSchemeResources(R.color.colorPrimary);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        initView();

        return view;
    }

    private void initView() {

        friendSidebar.setTextView(friendDialog);
        friendSidebar.setOnTouchingLetterChangedListener(this);

        searchInput.addTextChangedListener(this);
        
        adapter = new FriendListAdapter(friendList, dataList);
        friendList.setAdapter(adapter);

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfoActivity.actionStart(getActivity(),
                        UserInfoActivity.TYPE_OTHER_USER, dataList.get(position).getId());

            }
        });

        refreshData();
    }

    private void refreshData() {
        refresher.setRefreshing(true);
        DataPresenter.requestFriendList(YouJoinApplication.getCurrUser().getId(), FriendFragment.this);
    }

    @Override
    public void onGetFriendList(FriendsInfo info) {
        refresher.setRefreshing(false);
        if (info.getResult().equals(NetworkManager.SUCCESS)) {
            dataList.clear();
            dataList.addAll(info.getFriends());
            adapter.refresh(dataList);
            searchInput.setText("");
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.error_network),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FriendUpdateEvent event){
        refreshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onTouchingLetterChanged(String s){
        int position = 0;
        //该字母首次出现的位置
        if(adapter != null){
            position = adapter.getPositionForSection(s.charAt(0));
        }
        if(position != -1){
            friendList.setSelection(position);
        }else if(s.contains("#")){
            friendList.setSelection(0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        List<FriendsInfo.FriendsEntity> temp = new ArrayList<>();
        temp.addAll(dataList);
        if(!searchInput.getText().toString().equals("")){
            for (FriendsInfo.FriendsEntity data : dataList) {
                if (data.getNickname().contains(s) || data.getPinyin().contains(s)) {
                } else {
                    temp.remove(data);
                }
            }
        }

        if (adapter != null) {
            adapter.refresh(temp);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

}
