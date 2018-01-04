package com.sunc.car.lovecar.bmob

import cn.bmob.v3.BmobObject

/**
 * Created by Administrator on 2017/11/14.
 */
class Order : BmobObject() {
    var price: Int = 0
    var product: String = ""
}