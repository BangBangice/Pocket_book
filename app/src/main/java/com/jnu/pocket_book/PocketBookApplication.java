package com.jnu.pocket_book;

import android.app.Application;
import android.content.Context;

import com.jnu.pocket_book.data.db.DBManager;

/* 表示全局应用的类*/
public class PocketBookApplication extends Application {

    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库
        DBManager.initDB(getApplicationContext());
        //方便在任何时候获取到Context对象
        context=getApplicationContext();
    }
}

