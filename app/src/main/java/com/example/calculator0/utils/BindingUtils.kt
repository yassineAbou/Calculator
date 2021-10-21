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
