package com.example.userApp.healper

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView


class CustomAutoCompleteTextView : AutoCompleteTextView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun clearFocus() {
        val inputManager: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.hideSoftInputFromWindow(
                findFocus().windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        ) {
            dismissDropDown()
        }
        setText("")
        super.clearFocus()
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        val inputManager: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inputManager.hideSoftInputFromWindow(
                    findFocus().windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            ) {
                dismissDropDown()
            }
            return true
        }
        return super.onKeyPreIme(keyCode, event)
    }
}