package me.zq.youjoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zq.youjoin.DataPresenter;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.utils.LogUtils;
import me.zq.youjoin.utils.StringUtils;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/12.
 */
public class MessageListAdapter extends BaseAdapter {

    public static final int TYPE_IN = 0;
    public static final int TYPE_OUT = 1;

    public static final String TAG = "YouJoin";

    private List<AVIMMessage> dataList;
    private LayoutInflater inflater;
    private Context context;

    public MessageListAdapter(Context context, List<AVIMMessage> dataList){
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount(){
        return dataList.size();
    }

    @Override
    public Object getItem(int position){
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemViewType(int position){
        AVIMMessage msg = dataList.get(position);
        if(msg.getFrom().equals(YouJoinApplication.getCurrUser().getUsername())){
            return TYPE_OUT;
        }else{
            return TYPE_IN;
        }
    }

    @Override
    public int getViewTypeCount(){
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder holder;
        if(convertView == null){
            if(getItemViewType(position) == TYPE_IN){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.msg_item_in, null);
                holder.avatar = (CircleImageView) convertView.findViewById(R.id.icon_in);
                holder.text = (TextView) convertView.findViewById(R.id.text_in);
            }else {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.msg_item_out, null);
                holder.avatar = (CircleImageView) convertView.findViewById(R.id.icon_out);
                holder.text = (TextView) convertView.findViewById(R.id.text_out);
            }
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        UserInfo info = DataPresenter.requestUserInfoFromCache(dataList.get(position).getFrom());
        if(info.getResult().equals(NetworkManager.SUCCESS)
                && info.getImg_url() != null){
            Picasso.with(context)
                    .load(StringUtils.getPicUrlList(info.getImg_url()).get(0))
                    .resize(200, 200)
                    .centerCrop()
                    .into(holder.avatar);
        }else {
            LogUtils.e(TAG, "MessageListAdapter get pic failure");
        }

        holder.text.setText(StringUtils.getEmotionContent(
                YouJoinApplication.getAppContext(),
                ((AVIMTextMessage)dataList.get(position)).getText()));
        return convertView;
    }

    public final class ViewHolder{
        public CircleImageView avatar;
        public TextView text;
    }
}
