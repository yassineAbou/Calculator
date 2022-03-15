package com.example.calculator0.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.calculator0.database.PrevOperation

@BindingAdapter("input")
fun TextView.setTextInput(item: PrevOperation?) {
    item?.let {
        text = item.previousInput
    }
}


@BindingAdapter("output")
fun TextView.setTextOutput(item: PrevOperation?) {
    item?.let {
        text = item.previousOutput
    }
}

fun parseDouble(`val`: String?): Double {
    return if (`val` == null || `val`.isEmpty()) 0.0 else `val`.toDouble()
}

inline fun <A, B, R> ifNotNull(a: A?, b: B?, code: (A, B) -> R) {
    if (a != null && b != null) {
        code(a, b)
    }
}