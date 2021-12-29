package com.jnu.pocket_book

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class PocketBookApplication: Application() {
    // 声明常量，以在MVVM架构中的VM层方便地获取到context
    //然后可以在项目的任何位置调用PocketBookApplication.context来获取Context对象
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}