package com.sunc.di.component

import com.sunc.car.lovecar.third.CarContainActivity
import com.sunc.car.lovecar.third.CarDetailActivity
import com.sunc.car.lovecar.third.CarQueryActivity
import com.sunc.car.lovecar.third.VehicleLimitActivity
import com.sunc.mvp.contract.ServiceContract
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Administrator on 2017/11/14.
 */
@Subcomponent(modules = arrayOf(ServiceModule::class))
interface ServiceComponent {
    fun inject(activity: CarQueryActivity)
    fun inject(activity: CarContainActivity)
    fun inject(activity: CarDetailActivity)
}

@Module
class ServiceModule(private val mView: ServiceContract.View){
    @Provides
    fun getView() = mView
}