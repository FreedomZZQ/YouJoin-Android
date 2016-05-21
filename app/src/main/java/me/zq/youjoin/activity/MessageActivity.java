package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.adapter.MessageListAdapter;
import me.zq.youjoin.event.ImTypeMessageEvent;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.widget.enter.EmojiFragment;
import me.zq.youjoin.widget.enter.EnterEmojiLayout;
import me.zq.youjoin.widget.enter.EnterLayout;

public class MessageActivity extends BaseActivity implements EmojiFragment.EnterEmojiLayout{

    @Bind(R.id.sendmsg)
    ImageButton sendmsg;
    @Bind(R.id.msg_List)
    ListView msgList;

    private String receiver;
//    private UserInfo receiver;
    public static final String RECEIVER = "receiver";

    EnterEmojiLayout enterLayout;
    EditText msgEdit;
    private boolean mFirstFocus = true;

//    List<PrimsgInfo.MessageEntity> dataList = new ArrayList<>();
    List<AVIMMessage> dataList = new ArrayList<>();
    MessageListAdapter adapter;
    AVIMConversation conversation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
//        receiver = getIntent().getParcelableExtra(RECEIVER);
        receiver = getIntent().getStringExtra(RECEIVER);

        initEnter();

        adapter = new MessageListAdapter(MessageActivity.this, dataList);
        msgList.setAdapter(adapter);
        //DataPresenter.getPrimsgList(receiver.getId(), MessageActivity.this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            UserInfo recv = DataPresenter.requestUserInfoFromCache(receiver);
            if(!recv.getNickname().equals("")){
                actionBar.setTitle(recv.getNickname());
            }else{
                actionBar.setTitle(receiver);
            }

        }

        fetchConversation();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void fetchConversation() {
        String curUsername = YouJoinApplication.getCurrUser().getUsername();
        List<String> members = new ArrayList<>();
        members.add(curUsername);
        members.add(receiver);
        AVIMClient.getInstance(curUsername).createConversation(
                members, curUsername + " & " + receiver,
                null, false, true,
                new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVIMException e) {
                        conversation = avimConversation;
                        fetchMessages();
                    }
                });
    }

    private void fetchMessages(){
        conversation.queryMessages(new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
                if (filterException(e)) {
                    dataList.clear();
                    dataList.addAll(list);
                    adapter.notifyDataSetChanged();
                    //scrollToBottom();
                }
            }
        });
    }

    protected boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            Toast.makeText(MessageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
//                this.finish();
            default:
                this.finish();
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.sendmsg)
    protected void sendMsg(){
        final AVIMTextMessage msg = new AVIMTextMessage();
        msg.setText(msgEdit.getText().toString());
        msgEdit.setText("");
        conversation.sendMessage(msg, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if(e == null){
                    dataList.add(msg);
                    adapter.notifyDataSetChanged();
                    msgList.setSelection(msgList.getBottom());
                }
            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImTypeMessageEvent event){
        List<String> members = event.conversation.getMembers();
        String curUsername = YouJoinApplication.getCurrUser().getUsername();
        if(members.contains(receiver) && members.contains(curUsername)){
            dataList.add(event.message);
            adapter.notifyDataSetChanged();
        }
    }

    private void initEnter() {
        enterLayout = new EnterEmojiLayout(this, null);
        msgEdit = enterLayout.content;
        enterLayout.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterLayout.popKeyboard();
            }
        });
        enterLayout.content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mFirstFocus && hasFocus) {
                    mFirstFocus = false;
                    enterLayout.popKeyboard();
                }
            }
        });

    }

    @Override
    protected void onStop() {
        enterLayout.closeEnterPanel();
        super.onStop();
    }

    @Override
    public EnterLayout getEnterLayout() {
        return enterLayout;
    }

    public static void actionStart(Context context, String receiver) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(RECEIVER, receiver);
        context.startActivity(intent);
    }

}
