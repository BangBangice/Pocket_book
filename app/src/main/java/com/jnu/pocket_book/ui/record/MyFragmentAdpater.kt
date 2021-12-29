package com.jnu.pocket_book.ui.record

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyFragmentAdpater(fragmentActivity: FragmentActivity)
    :FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        if (position == 0)
            return OutcomeFragment.newInstance()
        //else


        return  OutcomeFragment.newInstance()
    }

}