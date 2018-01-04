package com.sunc.di.component

import com.sunc.car.lovecar.third.CarViolationActivity
import com.sunc.car.lovecar.third.VinSearchActivity
import com.sunc.mvp.contract.ViolationContract
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Administrator on 2017/11/14.
 */
@Subcomponent(modules = arrayOf(ViolationModule::class))
interface ViolationComponent {
    fun inject(activity: CarViolationActivity)
}

@Module
class ViolationModule(private val mView: ViolationContract.View){
    @Provides
    fun getView() = mView
}