package com.example.calculator.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.calculator.database.PrevOperation

@BindingAdapter("setTextInput")
fun TextView.setTextInput(item: PrevOperation?) {
    item?.let {
        text = item.input
    }
}

@BindingAdapter("setTextResult")
fun TextView.setTextResult(item: PrevOperation?) {
    item?.let {
        text = item.result
    }
}

fun parseDouble(`val`: String?): Double {
    return if (`val` == null || `val`.isEmpty()) 0.0 else `val`.toDouble()
}

inline fun <T1: Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2)->R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

fun isBalancedBrackets(str: String): Boolean {
    var count = 0
    var i = 0
    while (i < str.length && count >= 0) {
        if (str[i] == '(') count++ else if (str[i] == ')') count--
        i++
    }
    return count == 0
}

fun trimTrailingZero(value: String?): String? {
    return if (!value.isNullOrEmpty()) {
        if (value.indexOf(".") < 0) {
            value

        } else {
            value.replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "")
        }

    } else {
        value
    }
}
