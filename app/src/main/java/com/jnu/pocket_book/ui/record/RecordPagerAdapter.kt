package com.jnu.pocket_book.ui.record

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class RecordPagerAdapter(fm:FragmentManager,val fragmentList:List<Fragment>):
    FragmentPagerAdapter(fm) {
    val titles=ArrayList<String>()


    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Fragment {
        TODO("Not yet implemented")
    }
}