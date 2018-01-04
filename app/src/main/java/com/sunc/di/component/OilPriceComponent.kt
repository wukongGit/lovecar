package com.sunc.di.component

import com.sunc.car.lovecar.third.OilPriceActivity
import com.sunc.mvp.contract.OilPriceContract
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Administrator on 2017/11/14.
 */
@Subcomponent(modules = arrayOf(OilPriceModule::class))
interface OilPriceComponent {
    fun inject(activity: OilPriceActivity)
}

@Module
class OilPriceModule(private val mView: OilPriceContract.View){
    @Provides
    fun getView() = mView
}