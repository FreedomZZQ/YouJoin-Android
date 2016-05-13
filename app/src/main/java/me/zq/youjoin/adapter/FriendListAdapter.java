package me.zq.youjoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.zq.youjoin.R;
import me.zq.youjoin.model.FriendsInfo;
import me.zq.youjoin.utils.StringUtils;

/**
 * YouJoin-Android
 * Created by ZQ on 2016/5/13.
 */
public class FriendListAdapter extends BaseAdapter implements SectionIndexer{

    private List<FriendsInfo.FriendsEntity> dataList;
    private AbsListView listView;
    private Context context;
    private LayoutInflater inflater;


    public FriendListAdapter(ListView view, List<FriendsInfo.FriendsEntity> data){
        if(data == null){
            data = new ArrayList<>();
        }
        Collections.sort(data);
        this.dataList = data;
        this.listView = view;
        context = view.getContext();
        inflater = LayoutInflater.from(context);

    }

    public void refresh(List<FriendsInfo.FriendsEntity> data){
        if(data == null){
            data = new ArrayList<>();
        }
        Collections.sort(data);
        this.dataList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return dataList.size();
    }

    @Override
    public FriendsInfo.FriendsEntity getItem(int pos){
        return dataList.get(pos);
    }

    @Override
    public long getItemId(int pos){
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.userlist_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.divideTitle = (TextView) convertView.findViewById(R.id.divideTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final FriendsInfo.FriendsEntity item = dataList.get(position);
        holder.divideTitle.setVisibility(View.GONE);
        if(position == 0){
            holder.divideTitle.setVisibility(View.VISIBLE);
            holder.divideTitle.setText(Character.toString(item.getFirstChar()));
        }else{
            FriendsInfo.FriendsEntity prev = dataList.get(position - 1);
            if(prev.getFirstChar() != item.getFirstChar()){
                holder.divideTitle.setVisibility(View.VISIBLE);
                holder.divideTitle.setText(Character.toString(item.getFirstChar()));
            }
        }
        holder.name.setText(item.getNickname());
        holder.icon.setImageResource(R.mipmap.ic_account_circle_white_48dp);
        Picasso.with(context)
                .load(StringUtils.getPicUrlList(item.getImg_url()).get(0))
                .resize(200, 200)
                .centerCrop()
                .into(holder.icon);

        return convertView;
    }

    class ViewHolder{
        TextView name;
        ImageView icon;
        TextView divideTitle;
    }

    public int getSectionForPosition(int position){
        FriendsInfo.FriendsEntity item = dataList.get(position);
        return item.getFirstChar();
    }

    public int getPositionForSection(int section){
        for(int i = 0; i < getCount(); i++){
            char firstChar = dataList.get(i).getFirstChar();
            if(firstChar == section){
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

}
