package com.sunc.mvp.presenter

import com.sunc.car.lovecar.bmob.Car
import com.sunc.mvp.contract.BillContract
import com.sunc.mvp.model.BillModel
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class BillPresenter
@Inject constructor(private val mModel: BillModel,
                    private val mView: BillContract.View): BillContract.Presenter {
    override fun billPieStatistic(car: Car) {
        mModel.billPieStatistic(car, mView)
    }

    override fun statisticData(car: Car) {
        mModel.statisticData(car, mView)
    }

    override fun getData(page: Int, car: Car) {
        mModel.getData(page, car, mView)
    }
}