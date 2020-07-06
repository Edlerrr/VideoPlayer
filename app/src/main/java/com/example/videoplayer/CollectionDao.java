package com.example.videoplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.videoplayer.Bean.CollectionBean;
import com.example.videoplayer.Bean.VideoBean;

import java.util.ArrayList;
import java.util.List;

public class CollectionDao {
    //创建数据库
    private MyHelper myHelper;
    //List<VideoBean.ItemListBean> itemlists;
    //List<CollectionBean> collections;


    private CollectionDao(Context context) {
        myHelper = new MyHelper(context, "videos.db", null, 1);
    }

    private static CollectionDao instance;

    public static synchronized CollectionDao getInstance(Context context) {
        if (instance == null) {
            instance = new CollectionDao(context);
        }
        return instance;
    }

    /**
     * 增加收藏记录
     */
    public void addCollection(int position ,List<VideoBean.ItemListBean> mDatas) {
        //        获取指定位置的数据源
        VideoBean.ItemListBean.DataBean dataBean = mDatas.get(position).getData();
//        设置发布者的信息
        VideoBean.ItemListBean.DataBean.AuthorBean author = dataBean.getAuthor();
        //获得一个可写的数据库的一个引用
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("vdName", author.getName());// KEY 是列名，value 是该列的值
        contentValues.put("vdDescription",author.getDescription());
        contentValues.put("vdIconUrl",author.getIcon());
        contentValues.put("vdPlayUrl",dataBean.getPlayUrl());
        contentValues.put("vdTitle",dataBean.getTitle());
        contentValues.put("vdThumbUrl", dataBean.getCover().getFeed());

        db.insert("videos", null, contentValues);

    }

    /**
     * 删除收藏记录
     */
    public void delCollection(String name) {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        //表名  删除的条件
        db.delete("videos", "vdName = ?",new String[]{name});
    }

    /**
     * 删除全部收藏记录
     */
    public void delAllCollection(List<CollectionBean> beans) {
        for (int i=0;i<beans.size();i++){
            delCollection(beans.get(i).getName());
        }
    }

    /**
    /**
     * 查询所有收藏记录
     */
    public List<CollectionBean> queryCollectionData() {
        //创建集合对象
        List<CollectionBean> collections = new ArrayList<CollectionBean>();
        SQLiteDatabase db = myHelper.getReadableDatabase();
        // Cursor cursor = db.rawQuery("select * from history order by lasttime desc;", null);
        Cursor cursor = db.query("videos",new String[]{"id","vdName","vdDescription","vdIconUrl","vdPlayUrl","vdTitle","vdThumbUrl"},null,null,null,null,null,null);
        // 返回的 cursor 默认是在第一行的上一行
        //遍历
        while (cursor.moveToNext()) {// cursor.moveToNext() 向下移动一行,如果有内容，返回true
            int id=cursor.getInt(1);
            String name = cursor.getString(cursor.getColumnIndex("vdName"));
            String description = cursor.getString(cursor.getColumnIndex("vdDescription"));
            String iconUrl = cursor.getString(cursor.getColumnIndex("vdIconUrl"));
            String playUrl = cursor.getString(cursor.getColumnIndex("vdPlayUrl"));
            String title = cursor.getString(cursor.getColumnIndex("vdTitle"));
            String thumbUrl = cursor.getString(cursor.getColumnIndex("vdThumbUrl"));

            Log.e("queryName",name);
            System.out.println("queryName:"+name);

            CollectionBean collection = new CollectionBean();
            collection.setId(id);
            collection.setName(name);
            collection.setDescription(description);
            collection.setIconURL(iconUrl);
            collection.setPlayURL(playUrl);
            collection.setTitle(title);
            collection.setThumbURL(thumbUrl);

            Log.d("queryNameOnCollection",collection.getName());
            System.out.println("queryNameOnCollection:"+collection.getName());

            //封装的对象添加到集合中
            collections.add(collection);

        }
//        Log.e("queryIntoCollection",collections.get(0).getDescription());
//        System.out.println("queryIntoCollection:"+collections.get(0).getDescription());
//        Log.e("queryIntoCollection",collections.get(1).getDescription());
//        System.out.println("queryIntoCollection:"+collections.get(1).getDescription());
        //关闭cursor
        cursor.close();
        //SystemClock.sleep(1000);// 休眠2秒，数据比较多，比较耗时的情况
        return collections;

    }

    /**
     * 获得收藏记录数量
     */
    public int getCollectionCount() {
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query("videos", new String[]{"count(*)"}, null, null, null, null, null);

        cursor.moveToNext();
        int count = cursor.getInt(0);// 仅查了一列，count(*) 这一刻列

        cursor.close();

        return count;

    }


}
