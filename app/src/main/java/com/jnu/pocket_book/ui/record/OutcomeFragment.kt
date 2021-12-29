package com.jnu.pocket_book.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jnu.pocket_book.databinding.FragmentOutcomeBinding
import com.jnu.pocket_book.ui.utils.KeyBoardUtils

class OutcomeFragment : Fragment() {

    private var _binding: FragmentOutcomeBinding? = null
    private val binding get() = _binding!!  //给Fragment引入viewbinding

    companion object {
        fun newInstance() = OutcomeFragment()
    }

    private lateinit var viewModel: OutcomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOutcomeBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {


        //让自定义软键盘显示出来
        //让自定义软键盘显示出来
        val boardUtils = KeyBoardUtils(binding.fragRecordKeyboard, binding.fragRecordEtMoney)
        boardUtils.showKeyboard()
        //设置接口，监听确定按钮按钮被点击了
        boardUtils.setOnEnsureListener(object :KeyBoardUtils.OnEnsureListener{
            override fun onEnsure() {
                //获取输入钱数

                //获取记录的信息，保存在数据库当中

                // 返回上一级页面

            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OutcomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}