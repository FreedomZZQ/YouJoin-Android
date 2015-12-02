package me.zq.youjoin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.zq.youjoin.R;
import me.zq.youjoin.model.ImageInfo;
import me.zq.youjoin.widget.enter.AutoHeightGridView;
import me.zq.youjoin.widget.enter.EmojiFragment;
import me.zq.youjoin.widget.enter.EnterEmojiLayout;
import me.zq.youjoin.widget.enter.EnterLayout;

public class PublishActivity extends BaseActivity implements EmojiFragment.EnterEmojiLayout {

    public static final int PHOTO_MAX_COUNT = 9;

    @Bind(R.id.comment)
    EditText yjPublishEdit;
//    @Bind(R.id.btn_publish_photo)
//    ImageButton btnPublishPhoto;
//    @Bind(R.id.btn_publish_emotion)
//    ImageButton btnPublishEmotion;
//    @Bind(R.id.btn_publish_send)
//    ImageButton btnPublishSend;
//    @Bind(R.id.scroll_photo_container)
//    HorizontalScrollView scrollPhotoContainer;
    @Bind(R.id.lay_photo_container)
    LinearLayout layPhotoContainer;
    @Bind(R.id.gridView)
    AutoHeightGridView gridView;

    ArrayList<String> mSelectPath;
    ArrayList<ImageInfo> mData = new ArrayList<>();
    LayoutInflater mInflater;
    EnterEmojiLayout enterLayout;
    EditText msgEdit;
    BaseAdapter adapter = new BaseAdapter() {

        ArrayList<ViewHolder> holderList = new ArrayList<>();

        public int getCount() {
            return mData.size() + 1;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                holder.image = (ImageView) mInflater.inflate(R.layout.image_publish, parent, false);
                holderList.add(holder);
                holder.image.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == getCount() - 1) {
                if (getCount() == (PHOTO_MAX_COUNT + 1)) {
                    holder.image.setVisibility(View.INVISIBLE);

                } else {
                    holder.image.setVisibility(View.VISIBLE);
                    holder.image.setImageResource(R.mipmap.ic_insert_photo_white_48dp);
                    holder.uri = "";
                }

            } else {
                holder.image.setVisibility(View.VISIBLE);
                ImageInfo photoData = mData.get(position);
//                Uri data = photoData.;
//                holder.uri = data.toString();

//                ImageLoader.getInstance().loadImage(data.toString(), mSize, new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        for (ViewHolder item : holderList) {
//                            if (item.uri.equals(imageUri)) {
//                                item.image.setImageBitmap(loadedImage);
//                            }
//                        }
//                    }
//                });
            }

            return holder.image;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            gridView.setVisibility(getCount() > 1 ? View.VISIBLE : View.GONE);
        }

        class ViewHolder {
            ImageView image;
            String uri = "";
        }

    };

    private boolean mFirstFocus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yj_activity_publish);
        ButterKnife.bind(this);

//        btnPublishPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MultiImageSelectorActivity.startSelect(PublishActivity.this, 2, 9, MultiImageSelectorActivity.MODE_MULTI);
//            }
//        });

        initEnter();
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
