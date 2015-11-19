package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.zq.youjoin.R;

public class PublishActivity extends BaseActivity {

    @Bind(R.id.yj_publish_edit)
    EditText yjPublishEdit;
    @Bind(R.id.btn_publish_photo)
    ImageButton btnPublishPhoto;
    @Bind(R.id.btn_publish_emotion)
    ImageButton btnPublishEmotion;
    @Bind(R.id.btn_publish_send)
    ImageButton btnPublishSend;
    @Bind(R.id.scroll_photo_container)
    HorizontalScrollView scrollPhotoContainer;
    @Bind(R.id.lay_photo_container)
    LinearLayout layPhotoContainer;


    private ArrayList<String> mSelectPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yj_activity_publish);
        ButterKnife.bind(this);

        btnPublishPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImageSelectorActivity.startSelect(PublishActivity.this, 2, 9, MultiImageSelectorActivity.MODE_MULTI);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                layPhotoContainer.removeAllViews();
                for (String p : mSelectPath) {
                    sb.append(p);
                    sb.append("\n");

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

                }
                yjPublishEdit.setText(sb.toString());

            }
        }
    }


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PublishActivity.class);
        context.startActivity(intent);
    }
}
