package com.sunc.car.lovecar.bmob

import cn.bmob.v3.BmobObject

/**
 * Created by Administrator on 2017/11/14.
 */
data class Oil(val car: Car,
               val mount: Float,
               val fee: Float,
               val price: Float,
               val station: String,
               val note: String): BmobObject()