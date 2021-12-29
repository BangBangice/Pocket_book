package com.jnu.pocket_book.data

data class TypeBean(
    val id: Int,
    val typename: String,//类型名称
    val imageId: Int,//未被选中图片id
    val simageId: Int,//被选中图片id
    val kind: Int //收入-1  支出-0
) {

}