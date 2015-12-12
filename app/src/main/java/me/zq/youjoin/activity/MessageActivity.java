package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.model.ResultInfo;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.network.ResponseListener;
import me.zq.youjoin.utils.LogUtils;
import me.zq.youjoin.widget.enter.EmojiFragment;
import me.zq.youjoin.widget.enter.EnterEmojiLayout;
import me.zq.youjoin.widget.enter.EnterLayout;

public class MessageActivity extends BaseActivity implements EmojiFragment.EnterEmojiLayout {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.sendmsg)
    ImageButton sendmsg;

    private UserInfo receiver;
    public static final String RECEIVER = "receiver";

    EnterEmojiLayout enterLayout;
    EditText msgEdit;
    private boolean mFirstFocus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        receiver = getIntent().getParcelableExtra(RECEIVER);
        setSupportActionBar(toolbar);

        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.postSendMessage(YouJoinApplication.getCurrUser().getId(),
                        receiver.getId(), msgEdit.getText().toString(), new ResponseListener<ResultInfo>() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                LogUtils.e(TAG, volleyError.toString());
                            }

                            @Override
                            public void onResponse(ResultInfo info) {
                                if(info.getResult().equals(NetworkManager.SUCCESS)){
                                    onSuccess();
                                }else {

                                }
                            }
                        });
            }
        });
        initEnter();
    }

    private void onSuccess() {
        msgEdit.setText("");
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
