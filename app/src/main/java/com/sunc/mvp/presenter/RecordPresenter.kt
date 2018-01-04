package com.sunc.mvp.presenter

import com.sunc.car.lovecar.bmob.Bill
import com.sunc.car.lovecar.bmob.Oil
import com.sunc.mvp.contract.RecordContract
import com.sunc.mvp.model.RecordModel
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class RecordPresenter
@Inject constructor(private val mModel: RecordModel,
                    private val mView: RecordContract.View): RecordContract.Presenter {
    override fun addBill(bill: Bill) {
        mModel.addBill(bill, mView)
    }

    override fun addOil(oil: Oil) {
        mModel.addOil(oil, mView)
    }

}