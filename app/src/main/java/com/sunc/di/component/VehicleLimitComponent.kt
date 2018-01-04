package com.sunc.di.component

import com.sunc.car.lovecar.third.VehicleLimitActivity
import com.sunc.mvp.contract.VehicleLimitContract
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Administrator on 2017/11/14.
 */
@Subcomponent(modules = arrayOf(VehicleLimitModule::class))
interface VehicleLimitComponent {
    fun inject(activity: VehicleLimitActivity)
}

@Module
class VehicleLimitModule(private val mView: VehicleLimitContract.View){
    @Provides
    fun getView() = mView
}