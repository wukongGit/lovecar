package com.sunc.utils

import android.content.Context

/**
 * Created by Administrator on 2017/11/22.
 */
object DimenUtils {
    fun dp2px(context: Context, dp: Float): Int {
        return Math.round(dp * context.resources.displayMetrics.density)
    }
}