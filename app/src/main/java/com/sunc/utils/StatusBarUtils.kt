@file:Suppress("DEPRECATION")

package com.sunc.utils

import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.view.WindowManager

/**
 * 状态栏处理工具
 */
object StatusBarUtils {

    fun setWindowStatusBarColor(activity: Activity, colorResId: Int) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = activity.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = activity.resources.getColor(colorResId)

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun setWindowStatusBarColor(dialog: Dialog, colorResId: Int) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = dialog.window
                window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = dialog.context.resources.getColor(colorResId)

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun setTranslucentStatus(activity: Activity, on: Boolean): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val win = activity.window
            val winParams = win.attributes
            val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
            true
        } else {
            false
        }
    }

    /**
     * 获取状态栏高度
     * @param activity
     * @return
     */
    fun getStatusBarHeight(activity: Activity): Int {
        var height = 0
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen",
                "android")
        if (resourceId > 0) {
            height = activity.resources.getDimensionPixelSize(resourceId)
        }
        return height
    }
}
