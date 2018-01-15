package com.sunc.car.lovecar

import android.graphics.Color
import android.support.multidex.MultiDexApplication
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobUser
import com.base.bj.paysdk.utils.TrPay
import com.kyview.InitConfiguration
import com.kyview.manager.AdViewBannerManager
import com.kyview.manager.AdViewInstlManager
import com.kyview.manager.AdViewNativeManager
import com.kyview.manager.AdViewSpreadManager
import com.sunc.car.lovecar.bmob.Car
import com.sunc.car.lovecar.bmob.User
import com.sunc.di.component.ApiComponent
import com.sunc.di.component.DaggerApiComponent
import com.sunc.di.module.AppModule
import com.sunc.utils.AndroidUtils
import com.sunc.utils.Constants
import com.sunc.utils.DBKeys
import com.sunc.utils.DBUtils
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class App : MultiDexApplication() {
    init {
        instance = this
    }

    @Inject lateinit var apiComponent: ApiComponent
    var mLatLng = arrayListOf(0.0, 0.0)
    private var mUser: User? = null
    private var mCar: Car? = null
    val key1 = "SDK2017152603124977nr19m1m07tn65"
    val keySet = arrayOf(key1)
    private lateinit var initConfiguration: InitConfiguration

    @Synchronized
    fun setUser(user: User) {
        instance.mUser = user
        DBUtils.write(DBKeys.USER, user)
    }

    @Synchronized
    fun getUser(): User? {
        if (instance.mUser == null) {
            instance.mUser = DBUtils.read(DBKeys.USER)
        }
        return instance.mUser
    }

    @Synchronized
    fun setCar(car: Car) {
        instance.mCar = car
        DBUtils.write(DBKeys.CAR, car)
    }

    @Synchronized
    fun getCar(): Car? {
        if (instance.mCar == null) {
            instance.mCar = DBUtils.read(DBKeys.CAR)
        }
        return instance.mCar
    }

    @Synchronized
    fun logout() {
        BmobUser.logOut()
        instance.mUser = null
        instance.mCar = null
        DBUtils.delete(DBKeys.USER)
        DBUtils.delete(DBKeys.CAR)
    }

    override fun onCreate() {
        super.onCreate()
        DaggerApiComponent.builder().appModule(AppModule(this)).build().inject(this)
        if (applicationInfo.packageName == AndroidUtils.getCurrentProcessName()) {
            Bmob.initialize(this, Constants.BMOB_APPKEY)
            TrPay.getInstance(this).initPaySdk(Constants.TRPAY_APPKEY,AndroidUtils.getAppMetaData(this, "UMENG_CHANNEL"))
            initAd()
        }
    }

    private fun initAd() {
        //获取后台配置
        initConfiguration = InitConfiguration.Builder(
                this).setUpdateMode(InitConfiguration.UpdateMode.EVERYTIME)
                .setBannerCloseble(InitConfiguration.BannerSwitcher.CANCLOSED)
//                .setRunMode(InitConfiguration.RunMode.TEST)
                .build()
        //横幅 配置
        AdViewBannerManager.getInstance(this).init(initConfiguration, keySet)
        //插屏 配置
        AdViewInstlManager.getInstance(this).init(initConfiguration, keySet)
        //原生 配置
        //AdViewNativeManager.getInstance(this).init(initConfiguration, keySet)
        //开屏 配置
        AdViewSpreadManager.getInstance(this).init(initConfiguration, keySet)

        // 设置开屏下方LOGO，必须调用该方法
        AdViewSpreadManager.getInstance(this).setSpreadLogo(R.mipmap.ic_launcher)
        // 设置开屏背景颜色，可不设置
        AdViewSpreadManager.getInstance(this).setSpreadBackgroudColor(Color.WHITE)
        AdViewSpreadManager.getInstance(this).setSpreadNotifyType(AdViewSpreadManager.NOTIFY_COUNTER_NUM)
    }

    companion object {
        lateinit var instance: App
    }
}