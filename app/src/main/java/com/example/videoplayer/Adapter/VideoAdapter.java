package com.example.videoplayer.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.Bean.VideoBean;
import com.example.videoplayer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.jzvd.JzvdStd;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    Context context;
    List<VideoBean.ItemListBean> mDatas;
    private OnAddClickListener listener = null;//接收从activity传递的listener对象

    public VideoAdapter(Context context, List<VideoBean.ItemListBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        System.out.println("mDatas:" + mDatas);
    }

    public void setOnAddClickListener(OnAddClickListener listener){//给activity调用传递的对象
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
//        获取指定位置的数据源
        VideoBean.ItemListBean.DataBean dataBean = mDatas.get(position).getData();
//        设置发布者的信息
        VideoBean.ItemListBean.DataBean.AuthorBean author = dataBean.getAuthor();
        holder.itemMainTvName.setText(author.getName());
        holder.itemMainTvDescription.setText(author.getDescription());
        String iconURL = author.getIcon();
        if (!TextUtils.isEmpty(iconURL)) {
            Picasso.with(context).load(iconURL).into(holder.itemMainIvIcon);
        }
//     获取点赞数和评论数
        VideoBean.ItemListBean.DataBean.ConsumptionBean consumpBean = dataBean.getConsumption();
        holder.itemMainTvLove.setText(consumpBean.getRealCollectionCount() + "");
//        holder.replyTv.setText(consumpBean.getReplyCount()+"");
//      设置视频播放器的信息
        holder.jzvdStd.setUp(dataBean.getPlayUrl(), dataBean.getTitle(), JzvdStd.SCREEN_NORMAL);
        String thumbUrl = dataBean.getCover().getFeed();  //缩略图的网络地址
        Picasso.with(context).load(thumbUrl).into(holder.jzvdStd.posterImageView);
        holder.jzvdStd.positionInList = position;

        if(listener != null){
            holder.itemMainTvCollect.setOnClickListener(new View.OnClickListener() {//点击事件方法2：
                @Override
                public void onClick(View v) {
                    listener.onAddClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        JzvdStd jzvdStd;
        ImageView itemMainIvIcon;
        TextView itemMainTvName, itemMainTvDescription, itemMainTvLove, itemMainTvCollect;

        public MyViewHolder(View view) {
            super(view);
            jzvdStd = view.findViewById(R.id.jz_video);
            itemMainIvIcon = view.findViewById(R.id.item_main_iv_icon);
            itemMainTvName = view.findViewById(R.id.item_main_tv_name);
            itemMainTvDescription = view.findViewById(R.id.item_collection_tv_remark);
            itemMainTvLove = view.findViewById(R.id.item_main_tv_love);
            itemMainTvCollect = view.findViewById(R.id.item_main_tv_collect);
        }
    }

    public interface OnAddClickListener{
        public void onAddClick(int position);
    }
}