package com.sunc.car.lovecar

import android.content.Context
import android.widget.Toast

/**
 * Created by Administrator on 2017/11/14.
 */
fun Context.getMainComponent() = App.instance.apiComponent

fun Context.toast(msg:String,length:Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, msg, length).show()
}