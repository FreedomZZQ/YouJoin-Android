package me.zq.youjoin.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.model.TweetInfo;
import me.zq.youjoin.model.UserInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.network.ResponseListener;
import me.zq.youjoin.utils.LogUtils;
import me.zq.youjoin.utils.StringUtils;
import me.zq.youjoin.widget.enter.AutoHeightGridView;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/10.
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    public static final String TAG = "YouJoin";

    public OnItemClickListener itemClickListener;

    public void setDataList(List<TweetInfo.TweetsEntity> dataList) {
        this.dataList = dataList;
    }

    private List<TweetInfo.TweetsEntity> dataList;

    public TweetsAdapter(List<TweetInfo.TweetsEntity> data){
        dataList = data;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageButton btnLike;
        public ImageButton btnComments;
        public TextView nickname;
        public TextView likeCount;
        public TextView commentCount;
        public TextView tweetContent;
        public TextView time;
        public CircleImageView avatar;
        public AutoHeightGridView gridView;

        public ViewHolder(View itemView){
            super(itemView);
            likeCount = (TextView) itemView.findViewById(R.id.like_count);
            commentCount = (TextView) itemView.findViewById(R.id.comment_count);
            tweetContent = (TextView) itemView.findViewById(R.id.content);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            btnLike = (ImageButton) itemView.findViewById(R.id.btnLike);
            btnComments = (ImageButton) itemView.findViewById(R.id.btnComments);
            gridView = (AutoHeightGridView) itemView.findViewById(R.id.gridView);
            nickname = (TextView) itemView.findViewById(R.id.username);
            time = (TextView) itemView.findViewById(R.id.time);
        }

        //通过接口回调来实现点击事件
        @Override
        public void onClick(View v){
            if(itemClickListener != null){
                itemClickListener.onItemClick(v, getPosition());
            }
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i){
        //建立起ViewHolder中视图与数据的关联
        viewHolder.likeCount.setText(Integer.toString(dataList.get(i).getUpvote_num()));
        viewHolder.commentCount.setText(Integer.toString(dataList.get(i).getComment_num()));
        viewHolder.tweetContent.setText(StringUtils.getEmotionContent(
                YouJoinApplication.getAppContext(), viewHolder.tweetContent,
                dataList.get(i).getTweets_content()));
        viewHolder.time.setText("12:12");

        NetworkManager.postRequestUserInfo(Integer.toString(dataList.get(i).getFriend_id()),
                new ResponseListener<UserInfo>() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, volleyError.toString());
            }

            @Override
            public void onResponse(UserInfo info) {
                Picasso.with(YouJoinApplication.getAppContext())
                        .load(StringUtils.getPicUrlList(info.getImg_url()).get(0))
                        .resize(200, 200)
                        .centerCrop()
                        .into(viewHolder.avatar);
                viewHolder.nickname.setText(info.getNickname());
            }
        });

        List<String> urls = StringUtils.getPicUrlList(dataList.get(i).getTweets_img());
        GridPhotoAdapter adapter = new GridPhotoAdapter(YouJoinApplication.getAppContext(), urls);
        viewHolder.gridView.setAdapter(adapter);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        //将布局转化为view并传递给RecyclerView封装好的ViewHolder
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tweets_list_item,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount(){
        return dataList.size();
    }

}
