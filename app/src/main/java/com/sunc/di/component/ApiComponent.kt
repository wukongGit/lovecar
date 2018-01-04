package com.sunc.di.component

import com.sunc.car.lovecar.App
import com.sunc.di.module.AppModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Administrator on 2017/11/14.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface ApiComponent {
    fun inject(app: App)

    fun plus(module: LoginModule) : LoginComponent
    fun plus(module: MapModule) : MapComponent
    fun plus(module: OilModule) : OilComponent
    fun plus(module: BillModule) : BillComponent
    fun plus(module: CarModule) : CarComponent
    fun plus(module: RecordModule) : RecordComponent
    fun plus(module: ServiceModule) : ServiceComponent
    fun plus(module: VehicleLimitModule) : VehicleLimitComponent
    fun plus(module: VinModule) : VinComponent
    fun plus(module: OilPriceModule) : OilPriceComponent
    fun plus(module: ViolationModule) : ViolationComponent
    fun plus(module: DriverLicenseModule) : DriverLicenseComponent
}