package com.example.videoplayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyHelper extends SQLiteOpenHelper {

    private static final int version = 2;
    public static final String DATABASE_NAME = "videos.db";
//     /*** 在SQLiteOpenHelper的子类当中，必须有该构造函数
//     * @param context   上下文对象
//     * @param name      数据库名称
//     * @param factory
//     * @param version   当前数据库的版本，值必须是整数并且是递增的状态*/
     //这里是构造方法，主要实现SQLiteOpenHelper的初始化工作，注意：若SQLiteOpenHelper没有初始化，则在使用时会报空指针异常
     //即，需要明确指出Context即环境，否则会报NULLPOINT错误
     //这是系统提供的初始化构造方法，你也可以使用虾下面的构造方法来实现数据库自己命名
    public MyHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
//     public MyHelper(Context context) {
//         super(context, DATABASE_NAME, null, version);
//     }

    /**该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table videos(id Integer primary key autoincrement," +
                                        "vdName text,"+
                                        "vdDescription text," +
                                        "vdIconUrl text," +
                                        "vdPlayUrl text,"+
                                        "vdTitle text," +
                                        "vdThumbUrl text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        sqLiteDatabase.execSQL("Alter table videos add column vdName Text ");
//        onCreate(sqLiteDatabase);
    }


}
