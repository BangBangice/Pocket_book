package com.jnu.pocket_book.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jnu.pocket_book.PocketBookApplication
import com.jnu.pocket_book.R
import com.jnu.pocket_book.databinding.FragmentHomeBinding
import com.jnu.pocket_book.ui.record.RecordActivity


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //初始化view控件
    val editBtn: Button = binding.mainBtnEdit
    var todayLv: ListView = binding.mainLv//展示今日收支情况的ListView
    var searchIv: ImageView = binding.mainIvSearch
    var moreBtn: ImageButton = binding.mainBtnMore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        editBtn.setOnClickListener {
            val intent1=Intent(this.context,RecordActivity::class.java)
            startActivity(intent1)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}