package com.sunc.mvp.model

import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.sunc.car.lovecar.bmob.Bill
import com.sunc.car.lovecar.bmob.Car
import com.sunc.car.lovecar.bmob.Oil
import com.sunc.mvp.contract.RecordContract
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class RecordModel
@Inject constructor(private val loginModel: LoginModel): RecordContract.Model {
    override fun addBill(bill: Bill, view: RecordContract.View) {
        loginModel.addBill(bill, object : SaveListener<String>() {
            override fun done(p0: String?, p1: BmobException?) {
                if (p1 == null) {
                    view.billAdded()
                } else {
                    view.onError(p1.message!!)
                }
            }
        })
    }

    override fun addOil(oil: Oil, view: RecordContract.View) {
        loginModel.addOil(oil, object : SaveListener<String>() {
            override fun done(p0: String?, p1: BmobException?) {
                if (p1 == null) {
                    view.oilAdded()
                } else {
                    view.onError(p1.message!!)
                }
            }
        })
    }

}