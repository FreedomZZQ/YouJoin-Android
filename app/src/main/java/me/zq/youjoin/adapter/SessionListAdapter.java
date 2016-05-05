package me.zq.youjoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
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
 * Created by ZQ on 2016/5/4.
 */
public class SessionListAdapter extends BaseAdapter {


    public static final String TAG = "YouJoin";

    private List<AVIMConversation> dataList;
    private LayoutInflater inflater;
    private Context context;

    public SessionListAdapter(Context context, List<AVIMConversation> dataList){
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
    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder holder;
        if(convertView == null){

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.session_list_item, null);
            holder.avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            holder.content = (TextView) convertView.findViewById(R.id.lastMsg);
            holder.nickname = (TextView) convertView.findViewById(R.id.nickname);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        // TODO: 2016/5/5 这里有两点。第一，members顺序不固定；第二，获取用户信息不能只通过本地缓存。
        String username = dataList.get(position).getMembers().get(0);
        if(username.equals(YouJoinApplication.getCurrUser().getUsername())){
            username = dataList.get(position).getMembers().get(1);
        }
        DataPresenter.requestUserInfoAuto(username, new DataPresenter.GetUserInfo() {
            @Override
            public void onGetUserInfo(UserInfo info) {
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

                holder.nickname.setText(info.getNickname());
            }
        });

//        holder.content.setText(StringUtils.getEmotionContent(
//                YouJoinApplication.getAppContext(), holder.content,
//                dataList.get(position).getComment_content()));
        dataList.get(position).getLastMessage(new AVIMSingleMessageQueryCallback() {
            @Override
            public void done(AVIMMessage avimMessage, AVIMException e) {

                holder.content.setText(((AVIMTextMessage) avimMessage).getText());


            }
        });
        return convertView;
    }

    public final class ViewHolder{
        public CircleImageView avatar;
        public TextView content;
        public TextView nickname;
    }
}
