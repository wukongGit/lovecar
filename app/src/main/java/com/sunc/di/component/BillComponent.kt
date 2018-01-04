package com.sunc.di.component

import com.sunc.car.lovecar.bill.BillFragment
import com.sunc.car.lovecar.bill.PieActivity
import com.sunc.mvp.contract.BillContract
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by Administrator on 2017/11/14.
 */
@Subcomponent(modules = arrayOf(BillModule::class))
interface BillComponent {
    fun inject(fragment: BillFragment)
    fun inject(activity: PieActivity)
}

@Module
class BillModule(private val mView: BillContract.View){
    @Provides
    fun getView() = mView
}