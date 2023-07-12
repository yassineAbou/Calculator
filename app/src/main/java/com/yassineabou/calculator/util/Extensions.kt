package com.yassineabou.calculator.util

import android.content.Context
import android.content.res.Configuration
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.yassineabou.calculator.data.model.PreviousOperation
import java.math.RoundingMode
import java.text.DecimalFormat

@BindingAdapter("setInput")
fun TextView.setInput(previousOperation: PreviousOperation?) {
    previousOperation?.let {
        text = it.input
    }
}

@BindingAdapter("setResult")
fun TextView.setResult(previousOperation: PreviousOperation?) {
    previousOperation?.let {
        text = it.result
    }
}

fun Context.isDarkMode(): Boolean {
    val darkModeFlag = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
}

fun String.parseDouble(): Double {
    return if (this.isEmpty()) 0.0 else this.toDouble()
}

inline fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
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

fun Number.decimalFormat(): String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this).takeIf { it != "NaN" } ?: "Not applicable"
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

