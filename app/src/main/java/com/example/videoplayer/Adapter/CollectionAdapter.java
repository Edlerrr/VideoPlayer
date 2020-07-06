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

import com.example.videoplayer.Bean.CollectionBean;
import com.example.videoplayer.CollectionDao;
import com.example.videoplayer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.jzvd.JzvdStd;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolderII> {
    Context context;
    List<CollectionBean> mDatas;
    CollectionDao collectionDao;
    private OnDelClickListener listener = null;//接收从activity传递的listener对象

    public CollectionAdapter(Context context, List<CollectionBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        System.out.println("mDatas:" + mDatas);
    }

    public void setOnDelClickListener(OnDelClickListener listener){//给activity调用传递的对象
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolderII onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolderII holder = new MyViewHolderII(LayoutInflater.from(context).inflate(R.layout.item_collection, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderII holder, int position) {
//        获取指定位置的数据源
        /**VideoBean.ItemListBean.DataBean dataBean = mDatas.get(position).getData();*/
//        设置发布者的信息
        /** VideoBean.ItemListBean.DataBean.AuthorBean author = dataBean.getAuthor();*/
        holder.itemCollectionTvName.setText(mDatas.get(position).getName());
        holder.itemCollectionTvDescription.setText(mDatas.get(position).getDescription());
        String iconURL = mDatas.get(position).getIconURL();
        if (!TextUtils.isEmpty(iconURL)) {
            Picasso.with(context).load(iconURL).into(holder.itemCollectionIvIcon);
        }
//     获取点赞数和评论数
        /**VideoBean.ItemListBean.DataBean.ConsumptionBean consumpBean = dataBean.getConsumption();
         holder.itemMainTvLove.setText(consumpBean.getRealCollectionCount()+"");
         //        holder.replyTv.setText(consumpBean.getReplyCount()+"");*/
//      设置视频播放器的信息
        holder.jzvdStd.setUp(mDatas.get(position).getPlayURL(),mDatas.get(position).getTitle(), JzvdStd.SCREEN_NORMAL);
        String thumbUrl = mDatas.get(position).getThumbURL();  //缩略图的网络地址
        Picasso.with(context).load(thumbUrl).into(holder.jzvdStd.posterImageView);
        holder.jzvdStd.positionInList = position;

        if(listener != null){
            holder.itemCollectionTvDelete.setOnClickListener(new View.OnClickListener() {//点击事件方法2：
                @Override
                public void onClick(View v) {
                    listener.onDelClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class MyViewHolderII extends RecyclerView.ViewHolder{
        JzvdStd jzvdStd;
        ImageView itemCollectionIvIcon;
        TextView itemCollectionTvName,itemCollectionTvDescription,itemCollectionTvDelete;

        public MyViewHolderII(View view){
            super(view);
            jzvdStd = view.findViewById(R.id.jz_video);
            itemCollectionIvIcon = view.findViewById(R.id.item_collection_iv_icon);
            itemCollectionTvName = view.findViewById(R.id.item_collection_tv_name);
            itemCollectionTvDescription = view.findViewById(R.id.item_collection_tv_remark);
            itemCollectionTvDelete = view.findViewById(R.id.item_collection_tv_delete);
        }
    }

    public interface OnDelClickListener{
        public void onDelClick(int position);
    }

}
