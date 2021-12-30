package com.jnu.pocket_book.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jnu.pocket_book.R;
import com.jnu.pocket_book.adapter.AccountAdapter;
import com.jnu.pocket_book.data.model.AccountBean;
import com.jnu.pocket_book.data.db.DBManager;
import com.jnu.pocket_book.ui.chart.MonthChartActivity;
import com.jnu.pocket_book.ui.record.RecordActivity;
import com.jnu.pocket_book.ui.utils.BudgetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv;  //展示今日收支情况的ListView
    ImageButton moreBtn;
    //声明数据源
    List<AccountBean>mDatas;
    AccountAdapter adapter;
    int year,month,day;
    //头布局相关控件
    View headerView;
    TextView topOutTv,topInTv,topbudgetTv,topConTv;

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
        //添加ListView的头布局
        addLVHeaderView();
        mDatas = new ArrayList<>();
        //设置适配器：加载每一行数据到列表当中
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);

        setSupportActionBar(this.<Toolbar>findViewById(R.id.toolbar));

    }
     /** 初始化自带的View的方法*/
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        moreBtn = findViewById(R.id.main_btn_more);
        moreBtn.setOnClickListener(this);
        setLVLongClickListener();
    }
    /** 设置ListView的长按事件*/
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {  //点击了头布局
                    return false;
                }
                int pos = position-1;
                AccountBean clickBean = mDatas.get(pos);  //获取正在被点击的这条信息

                //弹出提示用户是否删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }
    /* 弹出是否删除某一条记录的对话框*/
    private void showDeleteItemDialog(final  AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择").setMessage("点击删除删除该条账目，点击更改更改该条账目")
                .setNegativeButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //执行删除的操作
                        DBManager.deleteItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean);   //实时刷新，移除集合当中的对象
                        adapter.notifyDataSetChanged();   //提示适配器更新数
                        Intent intent = new Intent();  //跳转界面
                        intent.setClass(getApplicationContext(), RecordActivity.class);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //执行删除的操作
                        DBManager.deleteItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean);   //实时刷新，移除集合当中的对象
                        adapter.notifyDataSetChanged();   //提示适配器更新数据
                        setTopTvShow();   //改变头布局TextView显示的内容
                    }
                });
        builder.create().show();   //显示对话框
    }

    /** 给ListView添加头布局的方法*/
    private void addLVHeaderView() {
        //将布局转换成View对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        //查找头布局可用控件
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);


        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);


    }
    /* 获取今日的具体时间*/
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    // 当activity获取焦点时，会调用的方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }
    /* 设置头布局当中文本内容的显示*/
    private void setTopTvShow() {
        //获取今日支出和收入总金额，显示在view当中
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "今日支出 ￥"+outcomeOneDay+"  收入 ￥"+incomeOneDay;
        topConTv.setText(infoOneDay);
//        获取本月收入和支出总金额
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("￥"+incomeOneMonth);
        topOutTv.setText("￥"+outcomeOneMonth);

//    设置显示运算剩余
        float bmoney = preferences.getFloat("bmoney", 0);//预算
        if (bmoney == 0) {
            topbudgetTv.setText("￥ 0");
        }else{
            float syMoney = bmoney-outcomeOneMonth;
            topbudgetTv.setText("￥"+syMoney);
        }
    }

    // 加载数据库数据
    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_more:
                Intent it1 = new Intent(this, RecordActivity.class);  //跳转界面
                startActivity(it1);
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
        }
        if (v == headerView) {
            //头布局被点击了
            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);
        }
    }
    /** 显示运算设置对话框*/
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //将预算金额写入到共享参数当中，进行存储
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney",money);
                editor.commit();
                //计算剩余金额
                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float syMoney = money-outcomeOneMonth;//预算剩余 = 预算-支出
                topbudgetTv.setText("￥"+syMoney);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //右上角的item点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent=new Intent();
        if(item.getItemId()==R.id.action_details){
            intent.setClass(getApplicationContext(), MonthChartActivity.class);
            startActivity(intent);
        }
        else{
            intent.setClass(getApplicationContext(), HistoryActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
