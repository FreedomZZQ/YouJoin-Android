package me.zq.youjoin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.activity.MessageActivity;
import me.zq.youjoin.adapter.SessionListAdapter;
import me.zq.youjoin.event.ImTypeMessageEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends BaseFragment {


    @Bind(R.id.sessionlist)
    ListView sessionlist;
    @Bind(R.id.refresher)
    SwipeRefreshLayout refresher;

    private List<AVIMConversation> sessionData = new ArrayList<>();
    private SessionListAdapter adapter;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);

        EventBus.getDefault().register(this);

        refresher.setColorSchemeResources(R.color.colorPrimary);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        TextView emptyView = new TextView(getActivity());
        emptyView.setText(getString(R.string.hint_nodata));
        emptyView.setPadding(20, 20, 0, 0);
        ViewGroup parentView = (ViewGroup) sessionlist.getParent();
        parentView.addView(emptyView, 1);
        sessionlist.setEmptyView(emptyView);

        adapter = new SessionListAdapter(getActivity(), sessionData);
        sessionlist.setAdapter(adapter);
        sessionlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = sessionData.get(position).getMembers().get(0);
                if (username.equals(YouJoinApplication.getCurrUser().getUsername())) {
                    username = sessionData.get(position).getMembers().get(1);
                }
                MessageActivity.actionStart(getActivity(), username);
            }
        });

        refreshData();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImTypeMessageEvent event) {
        AVIMTypedMessage message = event.message;
        AVIMConversation conversation = event.conversation;
        refreshData();
    }

    private void refreshData() {
        refresher.setRefreshing(true);
        AVIMClient tom = AVIMClient.getInstance(YouJoinApplication.getCurrUser().getUsername());
        tom.open(new AVIMClientCallback() {

            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    //登录成功
                    AVIMConversationQuery query = client.getQuery();
                    //query.limit(20);
                    query.findInBackground(new AVIMConversationQueryCallback() {
                        @Override
                        public void done(List<AVIMConversation> convs, AVIMException e) {
                            refresher.setRefreshing(false);
                            if (e == null) {
                                //convs就是获取到的conversation列表
                                //注意：按每个对话的最后更新日期（收到最后一条消息的时间）倒序排列
                                sessionData.clear();
                                sessionData.addAll(convs);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
