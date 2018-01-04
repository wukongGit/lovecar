package com.sunc.utils

import java.util.regex.Pattern

/**
 * Created by Administrator on 2017/11/22.
 */
object Strings {
    fun isMobilePhone(mobile: String): Boolean {
        val regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,1,2,5-9])|(177))\\d{8}$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(mobile)
        return matcher.matches()
    }

    fun isLongerEnough(password: String, len: Int): Boolean {
        return password.length > len
    }

    fun getYYMMDD(date: String): Array<String> {
        val yy = date.subSequence(0, 4).toString()
        var mm = date.subSequence(5, 7).toString()
        if (mm.startsWith("0")) mm = mm.substring(1)
        var dd = date.subSequence(8, 10).toString()
        if (dd.startsWith("0")) dd = dd.substring(1)
        return arrayOf(yy, mm, dd)
    }

    fun char2Str(label: CharArray): String {
        var str = ""
        for (char in label) {
            str += char
        }
        return str
    }

    fun isBlank(str: String?) : Boolean {
        return str == null || str.trim().isEmpty()
    }
}