package com.sunc.car.lovecar.bmob

import cn.bmob.v3.BmobObject

/**
 * Created by Administrator on 2017/11/14.
 */
data class Bill(val car: Car,
                val month: String,
                val type: String,
                val icon: String,
                val value: Float,
                val note: String): BmobObject() {
    fun isIn():Boolean {
        return value > 0
    }

    fun hasInNote():Boolean {
        return isIn() && !note.isBlank()
    }

    fun hasOutNote():Boolean {
        return !isIn() && !note.isBlank()
    }

    fun description() : String {
        return if (value > 0) "$type 收入 ${Math.abs(value)} 元"
        else "$type 支出 ${Math.abs(value)} 元"
    }
}