package com.jnu.pocket_book

import android.app.Application
import android.content.Context
import com.jnu.pocket_book.data.db.DBManager
import com.jnu.pocket_book.PocketBookApplication

/* 表示全局应用的类*/
class PocketBookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化数据库
        DBManager.initDB(applicationContext)
        //方便在任何时候获取到Context对象
        context = applicationContext
    }
    companion object {
        lateinit var context: Context
    }
}