package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.model.ImageInfo;
import me.zq.youjoin.model.ResultInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.utils.GlobalUtils;
import me.zq.youjoin.widget.enter.EmojiFragment;
import me.zq.youjoin.widget.enter.EnterEmojiLayout;
import me.zq.youjoin.widget.enter.EnterLayout;

public class PublishActivity extends BaseActivity
        implements EmojiFragment.EnterEmojiLayout, DataPresenter.SendTweet{

    @Bind(R.id.lay_photo_container)
    LinearLayout layPhotoContainer;
    @Bind(R.id.popPhoto)
    ImageButton btnPopPhoto;
    @Bind(R.id.btn_send)
    ImageButton btnSend;

    ArrayList<String> mSelectPath;
    ArrayList<ImageInfo> mData = new ArrayList<>();
    EnterEmojiLayout enterLayout;
    EditText msgEdit;

    private boolean mFirstFocus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yj_activity_publish);
        ButterKnife.bind(this);

        btnPopPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImageSelectorActivity.startSelect(PublishActivity.this, 2, 9,
                        MultiImageSelectorActivity.MODE_MULTI);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPresenter.sendTweet(YouJoinApplication.getCurrUser().getId(),
                        msgEdit.getText().toString(), mData, PublishActivity.this);
            }
        });

        initEnter();

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

    @Override
    public void onSendTweet(ResultInfo info){
        if(info.getResult().equals(NetworkManager.SUCCESS)){
            msgEdit.setText("");
            mData.clear();
            layPhotoContainer.removeAllViews();
            GlobalUtils.popSoftkeyboard(PublishActivity.this, msgEdit, false);
            Toast.makeText(PublishActivity.this, getString(R.string.send_tweet_success)
                    , Toast.LENGTH_SHORT).show();
            PublishActivity.this.finish();
        }else{
            Toast.makeText(PublishActivity.this, getString(R.string.error_network)
                    , Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                StringBuilder sb = new StringBuilder();
                layPhotoContainer.removeAllViews();
                for (String p : mSelectPath) {
//                    sb.append(p);
//                    sb.append("\n");

                    View itemView = View.inflate(PublishActivity.this, R.layout.yj_item_publish_photo, null);
                    ImageView img = (ImageView) itemView.findViewById(R.id.img);
                    itemView.setTag(p);

                    Picasso.with(PublishActivity.this)
                            .load(new File(p))
                            .resize(200, 200)
                            .centerCrop()
                            .into(img);
                    if (layPhotoContainer != null) {
                        layPhotoContainer.addView(itemView,
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                    }

                    mData.add(new ImageInfo(p));
                }
                //yjPublishEdit.setText(sb.toString());
            }
        }
    }


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PublishActivity.class);
        context.startActivity(intent);
    }
}
