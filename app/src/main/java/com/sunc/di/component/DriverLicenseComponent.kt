package com.sunc.di.component

import com.sunc.car.lovecar.third.LicenseActivity
import com.sunc.mvp.contract.DriverLicenseContract
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Administrator on 2017/11/14.
 */
@Subcomponent(modules = arrayOf(DriverLicenseModule::class))
interface DriverLicenseComponent {
    fun inject(activity: LicenseActivity)
}

@Module
class DriverLicenseModule(private val mView: DriverLicenseContract.View){
    @Provides
    fun getView() = mView
}