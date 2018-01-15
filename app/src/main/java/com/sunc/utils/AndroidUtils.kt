package com.sunc.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Process
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.sunc.car.lovecar.R
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*
import android.content.pm.PackageManager
import android.content.pm.ApplicationInfo
import android.text.TextUtils





/**
 * Created by Administrator on 2017/11/22.
 */
object AndroidUtils {
    var lastClickTime: Long = 0
    fun isFastDoubleClick(): Boolean {
        val time = System.currentTimeMillis()
        val timeD = time - lastClickTime
        if (timeD in 1..799) {
            return true
        }
        lastClickTime = time
        return false
    }

    fun isNetworkConnected(context: Context): Boolean {
        val info = (context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return info != null && info.isAvailable
    }
    /**
     * 获取当前运行的进程名
     *
     * @return
     */
    fun getCurrentProcessName(): String? {
        return try {
            val file = File("/proc/" + Process.myPid() + "/" + "cmdline")
            val mBufferedReader = BufferedReader(FileReader(file))
            val processName = mBufferedReader.readLine().trim { it <= ' ' }
            mBufferedReader.close()
            processName
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    fun getSystemVersion(): String {
        return android.os.Build.VERSION.RELEASE
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    fun getSystemModel(): String {
        return android.os.Build.MODEL
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    fun getDeviceBrand(): String {
        return android.os.Build.BRAND
    }

    /**
     * 序列号
     *
     * @return
     */
    fun getSerialNumber(): String? {
        var serial: String? = null
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            serial = get.invoke(c, "ro.serialno") as String
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return serial
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    fun getSystemLanguage(): String {
        return Locale.getDefault().language
    }


    fun showInputMethod(context: Activity, v: View) {
        val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(v, 0)
    }

    fun getIconByReflect(iconName: String):Int {
        try {
            val field = Class.forName("com.sunc.car.lovecar.R\$mipmap").getField(iconName)
            return field.getInt(field)
        } catch (e: Exception) {

        }
        return R.mipmap.ic_normal

    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    fun getAppMetaData(ctx: Context?, key: String): String? {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null
        }
        var resultData: String? = null
        try {
            val packageManager = ctx.packageManager
            if (packageManager != null) {
                val applicationInfo = packageManager.getApplicationInfo(ctx.packageName, PackageManager.GET_META_DATA)
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.get(key)!!.toString() + ""
                    }
                }

            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return resultData
    }

}