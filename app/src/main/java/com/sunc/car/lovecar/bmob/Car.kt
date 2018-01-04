package com.sunc.car.lovecar.bmob

import cn.bmob.v3.BmobObject

/**
 * Created by Administrator on 2017/11/14.
 */
class Car: BmobObject() {
    var user: User? = null
    var name: String = ""
}