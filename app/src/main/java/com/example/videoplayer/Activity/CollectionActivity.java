package com.example.videoplayer.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoplayer.Adapter.CollectionAdapter;
import com.example.videoplayer.Bean.CollectionBean;
import com.example.videoplayer.CollectionDao;
import com.example.videoplayer.R;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class CollectionActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView collectionRv;

//    List<VideoBean.ItemListBean> mDatas;
    List<CollectionBean> collections;
    CollectionBean collection;
    private CollectionAdapter adapter;
    private CollectionDao collectionDao;
    private Button returnBtn;
    int id;
    String name,description,iconUrl,playUrl,title,thumbUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        getSupportActionBar().hide();
        collectionDao = CollectionDao.getInstance(this);
        //        数据源
        collections = new ArrayList<>();

        initViews();
        queryData();


        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        JzvdStd.releaseAllVideos();    //释放正在被播放的视频信息
    }

    private void initViews() {
        collectionRv = findViewById(R.id.collection_rv);
        returnBtn = (Button) findViewById(R.id.item_collection_btn_return);

//        delAllBtn.setOnClickListener(this);


    }

    private void queryData(){
        try {
            collections = collectionDao.queryCollectionData();
            if (collections != null) {
                initAdapter();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initAdapter(){
        //        创建适配器对象
        adapter = new CollectionAdapter(this, collections);
        collectionRv.setLayoutManager(new LinearLayoutManager(CollectionActivity.this));
//        设置适配器
        collectionRv.setAdapter(adapter);
        adapter.setOnDelClickListener(new CollectionAdapter.OnDelClickListener() {
            @Override
            public void onDelClick(int position) {
                // 删除当前历史记录
                collectionDao.delCollection(collections.get(position).getName());
                collections.remove(position);//单纯更新UI，collections并没有跟随删除操作更新，重新加载adapter后才更新
//                collections = collectionDao.queryCollectionData();
                adapter.notifyDataSetChanged();

//                collectionDao.delCollection(name);
                Toast.makeText(CollectionActivity.this,/**"点击"+position*/"已取消收藏", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_btn_collection:

                break;
        }
    }
}
