package com.sunc.di.component

import com.sunc.car.lovecar.map.MapFragment
import com.sunc.car.lovecar.map.SearchActivity
import com.sunc.di.module.AppModule
import com.sunc.mvp.contract.MapContract
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Administrator on 2017/11/14.
 */
@Subcomponent(modules = arrayOf(MapModule::class))
interface MapComponent {
    fun inject(fragment: MapFragment)
    fun inject(activity: SearchActivity)
}

@Module
class MapModule(private val mView: MapContract.View){
    @Provides
    fun getView() = mView
}