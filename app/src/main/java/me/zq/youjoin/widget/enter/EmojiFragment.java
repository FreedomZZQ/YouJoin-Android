package me.zq.youjoin.widget.enter;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.HashMap;

import me.zq.youjoin.R;

/**
 * Created by chaochen on 14-10-30.
 */
public class EmojiFragment extends Fragment {

//    public enum Type {
//        Small, Big, Zhongqiu
//    }

    private LayoutInflater mInflater;
    private String[] mEmojiData;
    private MyImageGetter myImageGetter;
    private int deletePos;
    private EnterLayout mEnterLayout;

//    private Type mType;
    private int mItemLayout = R.layout.gridview_emotion_emoji;
    private int mGridViewColumns;

    public static HashMap<String, String> emojiMonkeyMap = new HashMap<String, String>();


    public EmojiFragment() {
        super();
    }

    public void init(String[] emojis, MyImageGetter imageGetter, EnterLayout enterLayout) {
        mEmojiData = emojis;
        myImageGetter = imageGetter;
        deletePos = emojis.length - 1;
        mEnterLayout = enterLayout;


        mItemLayout = R.layout.gridview_emotion_emoji;
        mGridViewColumns = 7;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("mEmojiData", mEmojiData);
        outState.putInt("deletePos", deletePos);
        outState.putInt("mItemLayout", mItemLayout);
        outState.putInt("mGridViewColumns", mGridViewColumns);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mInflater = inflater;
        View v = inflater.inflate(R.layout.emoji_gridview, container, false);

        if (savedInstanceState != null) {
            mEmojiData = savedInstanceState.getStringArray("mEmojiData");
            deletePos = savedInstanceState.getInt("deletePos");
            mItemLayout = savedInstanceState.getInt("mItemLayout");
            mGridViewColumns = savedInstanceState.getInt("mGridViewColumns");

            myImageGetter = new MyImageGetter(getActivity());
            Activity activity = getActivity();
            if (activity instanceof EnterEmojiLayout) {
                mEnterLayout = ((EnterEmojiLayout) activity).getEnterLayout();
            }
        }

        GridView gridView = (GridView) v.findViewById(R.id.gridView);
        gridView.setNumColumns(mGridViewColumns);
        gridView.setAdapter(adapterIcon);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int realPos = (int) id;
                if (realPos == deletePos) {
                    mEnterLayout.deleteOneChar();
                } else {
                    String name = (String) adapterIcon.getItem((int) id);

                    if (name.equals("my100")) {
                        name = "100";
                    } else if (name.equals("a00001")) {
                        name = "+1";
                    } else if (name.equals("a00002")) {
                        name = "-1";
                    }

                    mEnterLayout.insertEmoji(name);
                }
            }
        });
        return v;
    }

    BaseAdapter adapterIcon = new BaseAdapter() {

        @Override
        public int getCount() {
            return mEmojiData.length;
        }

        @Override
        public Object getItem(int position) {
            return mEmojiData[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(mItemLayout, parent, false);
                holder = new ViewHolder();
                holder.icon = convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String iconName = mEmojiData[position];
            holder.icon.setBackgroundDrawable(myImageGetter.getDrawable(iconName));

            return convertView;
        }

        class ViewHolder {
            public View icon;
        }
    };

    public interface EnterEmojiLayout {
        public EnterLayout getEnterLayout();
    }

}
