package com.sunc.car.lovecar.login

import com.sunc.car.lovecar.yun.BaseBean

/**
 * Created by Administrator on 2017/12/1.
 */
class GuideItem(var title: String, var type: Int): BaseBean() {

    fun isTitle():Boolean {
        return type == TYPE_TITLE
    }

    fun isContent():Boolean {
        return type == TYPE_CONTENT
    }

    companion object {
        val TYPE_TITLE = 1
        val TYPE_CONTENT = 2
    }

}