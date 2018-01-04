package com.sunc.mvp.presenter

import com.sunc.car.lovecar.bmob.Car
import com.sunc.mvp.contract.OilContract
import com.sunc.mvp.model.OilModel
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class OilPresenter
@Inject constructor(private val mModel: OilModel,
                    private val mView: OilContract.View): OilContract.Presenter {
    override fun getData(car: Car) {
        mModel.getNormalData(car, mView)
        mModel.getDetailData(car, mView)
    }
}