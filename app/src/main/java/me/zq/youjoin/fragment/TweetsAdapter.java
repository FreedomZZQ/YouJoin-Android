package me.zq.youjoin.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zq.youjoin.R;
import me.zq.youjoin.model.TweetInfo;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/10.
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    public OnItemClickListener itemClickListener;
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
        public ImageButton btnMore;
        public TextView likeCount;
        public CircleImageView avatar;

        public ViewHolder(View itemView){
            super(itemView);

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
    public void onBindViewHolder(ViewHolder viewHolder, int i){
        //建立起ViewHolder中视图与数据的关联
        viewHolder.likeCount.setText(dataList.get(i).getUpvote_num());
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
