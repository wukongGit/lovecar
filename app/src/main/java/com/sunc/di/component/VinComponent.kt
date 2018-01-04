package com.sunc.di.component

import com.sunc.car.lovecar.third.VinSearchActivity
import com.sunc.mvp.contract.VinContract
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Administrator on 2017/11/14.
 */
@Subcomponent(modules = arrayOf(VinModule::class))
interface VinComponent {
    fun inject(activity: VinSearchActivity)
}

@Module
class VinModule(private val mView: VinContract.View){
    @Provides
    fun getView() = mView
}