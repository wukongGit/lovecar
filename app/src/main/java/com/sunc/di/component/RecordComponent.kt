package com.sunc.di.component

import com.sunc.car.lovecar.bill.RecordActivity
import com.sunc.mvp.contract.RecordContract
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Administrator on 2017/11/14.
 */
@Subcomponent(modules = arrayOf(RecordModule::class))
interface RecordComponent {
    fun inject(activity: RecordActivity)
}

@Module
class RecordModule(private val mView: RecordContract.View){
    @Provides
    fun getView() = mView
}