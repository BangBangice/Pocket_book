package com.jnu.pocket_book.data

data class AccountBean(
    val id: Int,
    val typename: String,  //类型
    val sImageId: Int,  //被选中类型图片
    val beizhu: String,  //备注
    val money: Float,  //价格
    val time: String,   //保存时间字符串
    val year: Int,
    val month: Int,
    val day: Int,
    val kind: Int  //类型  收入---1   支出---0
) {
}