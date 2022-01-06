package com.jnu.pocket_book.ui

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import com.jnu.pocket_book.data.model.AccountBean
import com.jnu.pocket_book.adapter.AccountAdapter
import android.widget.TextView
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.AdapterView
import com.jnu.pocket_book.data.db.DBManager
import android.content.Intent
import com.jnu.pocket_book.ui.record.RecordActivity
import com.jnu.pocket_book.R
import com.jnu.pocket_book.ui.chart.MonthChartActivity
import com.jnu.pocket_book.ui.utils.BudgetDialog
import android.content.SharedPreferences.Editor
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import com.jnu.pocket_book.databinding.ActivityMainBinding
import com.jnu.pocket_book.databinding.ItemMainlvBinding
import com.jnu.pocket_book.databinding.ItemMainlvTopBinding
import com.jnu.pocket_book.ui.HistoryActivity
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var todayLv //展示今日收支情况的ListView
            : ListView? = null
    var addBtn: ImageButton? = null

    //声明数据源
    var mDatas: MutableList<AccountBean>? = null
    var adapter: AccountAdapter? = null
    var year = 0
    var month = 0
    var day = 0

    //头布局相关控件
    var headerView: View? = null
    var topOutTv: TextView? = null
    var topInTv: TextView? = null
    var topbudgetTv: TextView? = null
    var topConTv: TextView? = null
    var preferences: SharedPreferences? = null
    private var activityMainBinding: ActivityMainBinding? = null
    private var itemMainlvTopBinding: ItemMainlvTopBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //获取viewbinding
        activityMainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(activityMainBinding!!.root)
        initTime()
        initView()
        preferences = getSharedPreferences("budget", MODE_PRIVATE)
        //添加ListView的头布局
        addLVHeaderView()
        mDatas = ArrayList()
        //设置适配器：加载每一行数据到列表当中
        adapter = AccountAdapter(this, mDatas)
        todayLv!!.adapter = adapter
        setSupportActionBar(activityMainBinding!!.toolbar)
    }

    /** 初始化自带的View的方法 */
    private fun initView() {
        todayLv = activityMainBinding!!.mainLv
        addBtn = activityMainBinding!!.mainBtnMore
        addBtn!!.setOnClickListener(this)
        setLVLongClickListener()
    }

    /** 设置ListView的长按事件 */
    private fun setLVLongClickListener() {
        todayLv!!.onItemLongClickListener = OnItemLongClickListener { parent, view, position, id ->
            if (position == 0) {  //点击了头布局
                return@OnItemLongClickListener false
            }
            val pos = position - 1
            val clickBean = mDatas!![pos] //获取正在被点击的这条信息

            //弹出提示用户是否删除的对话框
            showDeleteItemDialog(clickBean)
            false
        }
    }

    /* 弹出是否删除某一条记录的对话框*/
    private fun showDeleteItemDialog(clickBean: AccountBean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("请选择").setMessage("点击删除删除该条账目，点击更改更改该条账目")
            .setNegativeButton("修改") { dialog, which ->
                val click_id = clickBean.id
                //执行删除的操作
                DBManager.deleteItemFromAccounttbById(click_id)
                mDatas!!.remove(clickBean) //实时刷新，移除集合当中的对象
                adapter!!.notifyDataSetChanged() //提示适配器更新数
                val intent = Intent() //跳转界面
                intent.setClass(applicationContext, RecordActivity::class.java)
                startActivity(intent)
            }
            .setPositiveButton("删除") { dialog, which ->
                val click_id = clickBean.id
                //执行删除的操作
                DBManager.deleteItemFromAccounttbById(click_id)
                mDatas!!.remove(clickBean) //实时刷新，移除集合当中的对象
                adapter!!.notifyDataSetChanged() //提示适配器更新数据
                setTopTvShow() //改变头布局TextView显示的内容
            }
        builder.create().show() //显示对话框
    }

    /** 给ListView添加头布局的方法 */
    private fun addLVHeaderView() {
        //将布局转换成View对象
        headerView = layoutInflater.inflate(R.layout.item_mainlv_top, null)
        todayLv!!.addHeaderView(headerView)
        //查找头布局可用控件
        itemMainlvTopBinding= ItemMainlvTopBinding.inflate(LayoutInflater.from(this))

        topOutTv = itemMainlvTopBinding!!.itemMainlvTopTvOut
        topInTv = itemMainlvTopBinding!!.itemMainlvTopTvIn
        topbudgetTv = itemMainlvTopBinding!!.itemMainlvTopTvBudget
        topConTv = itemMainlvTopBinding!!.itemMainlvTopTvDay
        topbudgetTv!!.setOnClickListener(this)
        headerView!!.setOnClickListener(this)
    }

    /* 获取今日的具体时间*/
    private fun initTime() {
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH] + 1
        day = calendar[Calendar.DAY_OF_MONTH]
    }

    // 当activity获取焦点时，会调用的方法
    override fun onResume() {
        super.onResume()
        loadDBData()
        setTopTvShow()
    }

    /* 设置头布局当中文本内容的显示*/
    private fun setTopTvShow() {
        //获取今日支出和收入总金额，显示在view当中
        val incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1)
        val outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0)
        val infoOneDay = "今日支出 ￥$outcomeOneDay  收入 ￥$incomeOneDay"
        topConTv!!.text = infoOneDay
        //        获取本月收入和支出总金额
        val incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1)
        val outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0)
        topInTv!!.text = "￥$incomeOneMonth"
        topOutTv!!.text = "￥$outcomeOneMonth"

//    设置显示运算剩余
        val bmoney = preferences!!.getFloat("bmoney", 0f) //预算
        if (bmoney == 0f) {
            topbudgetTv!!.text = "￥ 0"
        } else {
            val syMoney = bmoney - outcomeOneMonth
            topbudgetTv!!.text = "￥$syMoney"
        }
    }

    // 加载数据库数据
    private fun loadDBData() {
        val list = DBManager.getAccountListOneDayFromAccounttb(year, month, day)
        mDatas!!.clear()
        mDatas!!.addAll(list)
        adapter!!.notifyDataSetChanged()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.main_btn_more -> {
                val it1 = Intent(this, RecordActivity::class.java) //跳转界面
                startActivity(it1)
            }
            R.id.item_mainlv_top_tv_budget -> showBudgetDialog()
        }
        if (v === headerView) {
            //头布局被点击了
            val intent = Intent()
            intent.setClass(this, MonthChartActivity::class.java)
            startActivity(intent)
        }
    }

    /** 显示运算设置对话框 */
    private fun showBudgetDialog() {
        val dialog = BudgetDialog(this)
        dialog.show()
        dialog.setDialogSize()
        dialog.setOnEnsureListener { money -> //将预算金额写入到共享参数当中，进行存储
            val editor = preferences!!.edit()
            editor.putFloat("bmoney", money)
            editor.commit()
            //计算剩余金额
            val outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0)
            val syMoney = money - outcomeOneMonth //预算剩余 = 预算-支出
            topbudgetTv!!.text = "￥$syMoney"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    //右上角的item点击事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent()
        if (item.itemId == R.id.action_details) {
            intent.setClass(applicationContext, MonthChartActivity::class.java)
            startActivity(intent)
        } else {
            intent.setClass(applicationContext, HistoryActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}