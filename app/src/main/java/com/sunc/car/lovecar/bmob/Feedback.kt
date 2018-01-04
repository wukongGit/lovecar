package com.sunc.car.lovecar.bmob

import cn.bmob.v3.BmobObject

/**
 * Created by Administrator on 2017/11/14.
 */
class Feedback : BmobObject() {
    var content: String = ""
    var contact: String = ""
    var phoneInfo: String = ""
}