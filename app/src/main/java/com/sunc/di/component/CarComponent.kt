package com.sunc.di.component

import com.sunc.car.lovecar.login.CarListActivity
import com.sunc.mvp.contract.CarContract
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Administrator on 2017/11/14.
 */
@Subcomponent(modules = arrayOf(CarModule::class))
interface CarComponent {
    fun inject(activity: CarListActivity)
}

@Module
class CarModule(private val mView: CarContract.View){
    @Provides
    fun getView() = mView
}