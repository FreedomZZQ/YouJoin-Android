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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.adapter.MessageListAdapter;
import me.zq.youjoin.model.PrimsgInfo;
import me.zq.youjoin.model.ResultInfo;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.widget.enter.EmojiFragment;
import me.zq.youjoin.widget.enter.EnterEmojiLayout;
import me.zq.youjoin.widget.enter.EnterLayout;

public class MessageActivity extends BaseActivity
        implements EmojiFragment.EnterEmojiLayout, DataPresenter.SendPrimsg, DataPresenter.GetPrimsgList{

    @Bind(R.id.sendmsg)
    ImageButton sendmsg;
    @Bind(R.id.msg_List)
    ListView msgList;

    private UserInfo receiver;
    public static final String RECEIVER = "receiver";

    EnterEmojiLayout enterLayout;
    EditText msgEdit;
    private boolean mFirstFocus = true;

    List<PrimsgInfo.MessageEntity> dataList = new ArrayList<>();
    MessageListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        receiver = getIntent().getParcelableExtra(RECEIVER);

        initEnter();

        adapter = new MessageListAdapter(MessageActivity.this, dataList);
        msgList.setAdapter(adapter);
        DataPresenter.getPrimsgList(receiver.getId(), MessageActivity.this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
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
        PrimsgInfo.MessageEntity msg = new PrimsgInfo.MessageEntity();
        msg.setSender_id(YouJoinApplication.getCurrUser().getId());
        msg.setContent(msgEdit.getText().toString());
        //msg.setName(YouJoinApplication.getCurrUser().getNickname());
        dataList.add(msg);
        DataPresenter.sendPrimsg(receiver.getId(), msg.getContent(), MessageActivity.this);
    }

    @Override
    public void onSendPrimsg(ResultInfo info){
        if (info.getResult().equals(NetworkManager.SUCCESS)) {
            msgEdit.setText("");
            adapter.notifyDataSetChanged();
            msgList.setSelection(msgList.getBottom());
        } else {
            Toast.makeText(MessageActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetPrimsgList(PrimsgInfo info){
        if(info.getResult().equals(NetworkManager.SUCCESS)){
            dataList.clear();
            for(PrimsgInfo.MessageEntity entity : info.getMessage()){
                dataList.add(entity);
            }
            adapter.notifyDataSetChanged();
            msgList.setSelection(msgList.getBottom());
        }else {
            Toast.makeText(MessageActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
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

    public static void actionStart(Context context, UserInfo receiver) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(RECEIVER, receiver);
        context.startActivity(intent);
    }

}
