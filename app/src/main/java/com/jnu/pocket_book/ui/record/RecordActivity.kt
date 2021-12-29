package com.jnu.pocket_book.ui.record

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jnu.pocket_book.R

class RecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        //设置viewpager 加载页面
        initPager()
    }



    private fun initPager() {
        //初始化viewpager页面的集合

        val viewPagerFragments = findViewById<ViewPager2>(R.id.record_vp)
        viewPagerFragments.adapter = MyFragmentAdpater(this)

        val tabLayoutHeader = findViewById<TabLayout>(R.id.record_tabs)
        val tabLayoutMediator = TabLayoutMediator(
            tabLayoutHeader, viewPagerFragments
        ) { tab, position ->
            if (position == 0) tab.text = "支出"
            else if (position == 1) tab.text = "收入"
        }
        tabLayoutMediator.attach()
    }


}