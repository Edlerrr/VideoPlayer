package com.example.videoplayer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.videoplayer.CollectionDao;
import com.example.videoplayer.HttpUtils;
import com.example.videoplayer.R;
import com.example.videoplayer.Adapter.VideoAdapter;
import com.example.videoplayer.Bean.VideoBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.jzvd.JzvdStd;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView mainRv;
    String url = "http://baobab.kaiyanapp.com/api/v4/tabs/selected?udid=11111&vc=168&vn=3.3.1&deviceModel=Huawei6&first_channel=eyepetizer_baidu_market&last_channel=eyepetizer_baidu_market&system_version_code=20";
    List<VideoBean.ItemListBean> mDatas;
    List<Collection> collections;
    private VideoAdapter adapter;
    private CollectionDao collectionDao;
    private Button collectionBtn;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String json = (String) msg.obj;
//                解析数据
                VideoBean videoBean= new Gson().fromJson(json,VideoBean.class);
//                过滤了不需要的数据
                List<VideoBean.ItemListBean> itemList = videoBean.getItemList();
                for (int i = 0; i < itemList.size(); i++) {
                    VideoBean.ItemListBean listBean = itemList.get(i);
                    if (listBean.getType().equals("video")) {
                        mDatas.add(listBean);
                    }
                }
//                提示适配器更新数据
                adapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        collectionDao = CollectionDao.getInstance(this);//实例化dao
        initViews();

//        数据源
        mDatas = new ArrayList<>();
//        创建适配器对象
        adapter = new VideoAdapter(this, mDatas);
        mainRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//        设置适配器
        mainRv.setAdapter(adapter);
        adapter.setOnAddClickListener(new VideoAdapter.OnAddClickListener() {
            @Override
            public void onAddClick(int position) {
                collectionDao.addCollection(position,mDatas);
                Toast.makeText(MainActivity.this,/**"点击"+position*/"已收藏", Toast.LENGTH_SHORT).show();
            }
        });
//        加载网络数据
        loadData();
    }

    private void loadData() {
        /* 创建新的线程，完成数据的获取*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonContent = HttpUtils.getJsonContent(url);
//                子线程不能更新UI，需要通过handler发送数据回到主线程
                Message message = new Message();   //发送的消息对象
                message.what = 1;
                message.obj = jsonContent;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        JzvdStd.releaseAllVideos();    //释放正在被播放的视频信息
    }

    private void initViews() {
        mainRv = findViewById(R.id.main_rv);
        collectionBtn = (Button) findViewById(R.id.activity_main_btn_collection);

        collectionBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_btn_collection:
                startActivity(new Intent(MainActivity.this, CollectionActivity.class));
                break;
        }
    }
}
