package com.jnu.pocket_book.ui.utils
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.text.Editable
import android.text.InputType
import android.view.View
import android.widget.EditText
import com.jnu.pocket_book.R

class KeyBoardUtils(val keyboardView: KeyboardView,val editText: EditText) {
    interface OnEnsureListener{
        fun onEnsure()
    }
    lateinit var onEnsureListener2:OnEnsureListener

    fun setOnEnsureListener(onEnsureListener: OnEnsureListener){
        onEnsureListener2=onEnsureListener
    }
    init {
        editText.setInputType(InputType.TYPE_NULL)  //取消弹出系统键盘
        val k1 = Keyboard(this.editText.context, R.xml.key)  //我们的键盘

        with(keyboardView) {

            setKeyboard(k1)   //设置要显示键盘的演示
            setEnabled(true)
            setPreviewEnabled(false)
            setOnKeyboardActionListener(listener())
        }  //键盘被点击的监听
    }

    inner class listener:KeyboardView.OnKeyboardActionListener {
        override fun onPress(p0: Int) {
            TODO("Not yet implemented")
        }

        override fun onRelease(p0: Int) {
            TODO("Not yet implemented")
        }

        override fun onKey(p0: Int, p1: IntArray?) {
            val editable:Editable=editText.text
            val start=editText.selectionStart
            when(p0){
                Keyboard.KEYCODE_DELETE->
                    if (editable!=null&&editable.length>0)
                        if(start>0)
                            editable.delete(start-1,start)
                Keyboard.KEYCODE_CANCEL->editable.clear()
                Keyboard.KEYCODE_DONE->onEnsureListener2.onEnsure()

                else -> editable.insert(start,p0 as String) //数字键，直接进行显示
            }
        }

        override fun onText(p0: CharSequence?) {
            TODO("Not yet implemented")
        }

        override fun swipeLeft() {
            TODO("Not yet implemented")
        }

        override fun swipeRight() {
            TODO("Not yet implemented")
        }

        override fun swipeDown() {
            TODO("Not yet implemented")
        }

        override fun swipeUp() {
            TODO("Not yet implemented")
        }


    }
    //显示键盘
    fun showKeyboard(){
        val visibility:Int=keyboardView.visibility
        if(visibility== View.INVISIBLE||visibility==View.GONE)
            keyboardView.setVisibility(View.VISIBLE)
    }
    //隐藏键盘
    fun hideKeyboard(){
        val visibility:Int=keyboardView.visibility
        if(visibility==View.VISIBLE||visibility==View.INVISIBLE)
            keyboardView.setVisibility(View.GONE)
    }

    fun onEnsureListener(value: () -> Unit) {

    }

}