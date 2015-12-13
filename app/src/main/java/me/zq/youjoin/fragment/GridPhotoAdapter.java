package me.zq.youjoin.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.zq.youjoin.R;

/**
 * YouJoin-Android
 * Created by ZQ on 2015/12/12.
 */
public class GridPhotoAdapter  extends BaseAdapter {
    private Context mContext;

    List<String> urls;

    public GridPhotoAdapter(Context c, List<String> urls) {
        this.mContext = c;
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(this.mContext);
            convertView = inflater.inflate(R.layout.gridview_photo_item, parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if(!urls.get(position).isEmpty()){
            Picasso.with(this.mContext)
                    .load(urls.get(position))
                    .resize(400, 400)
                    .centerCrop()
                    .into(holder.imageView);
        }else {
            holder.imageView.setVisibility(View.GONE);
        }

        return convertView;
    }

    public final class ViewHolder{
        public ImageView imageView;
    }
}
