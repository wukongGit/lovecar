package com.sunc.di.component

import com.sunc.car.lovecar.oil.OilFragment
import com.sunc.mvp.contract.OilContract
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Administrator on 2017/11/14.
 */
@Subcomponent(modules = arrayOf(OilModule::class))
interface OilComponent {
    fun inject(fragment: OilFragment)
}

@Module
class OilModule(private val mView: OilContract.View){
    @Provides
    fun getView() = mView
}