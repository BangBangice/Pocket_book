package com.jnu.pocket_book;

import android.app.Application;

import com.jnu.pocket_book.db.DBManager;

/* 表示全局应用的类*/
public class PocketBookApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库
        DBManager.initDB(getApplicationContext());
    }
}

