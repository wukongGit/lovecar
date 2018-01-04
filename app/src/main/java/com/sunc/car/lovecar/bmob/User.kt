package com.sunc.car.lovecar.bmob

import cn.bmob.v3.BmobUser

/**
 * Created by Administrator on 2017/11/28.
 */

class User : BmobUser() {
    var sex: Int = 1
    var age: Int = 0
    var work: String = ""
    var money: Int = 0

    fun isMale() = (sex == 1)
    fun sexDes() = if (isMale()) "男" else "女"
    fun ageDes() = "" + age.toString()
}
