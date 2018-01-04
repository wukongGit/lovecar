package com.sunc.car.lovecar.bill

import com.sunc.car.lovecar.yun.BaseBean

/**
 * Created by Administrator on 2017/12/6.
 */
data class MonthBill(var _sumValue: String, var month: String): BaseBean() {
    fun description() : String {
        val y = month.subSequence(0,4)
        val m = month.subSequence(4,6)
        return "$y 年 $m 月 结余 $_sumValue 元"
    }
}