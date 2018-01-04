package com.sunc.mvp.contract

import com.sunc.car.lovecar.bmob.Bill
import com.sunc.car.lovecar.bmob.Oil

/**
 * Created by Administrator on 2017/11/14.
 */
interface RecordContract {
    interface View {
        fun billAdded()
        fun oilAdded()
        fun  onError(msg:String)
    }

    interface Model {
        fun addBill(bill: Bill, view: RecordContract.View)
        fun addOil(oil: Oil, view: RecordContract.View)
    }

    interface Presenter {
        fun addBill(bill: Bill)
        fun addOil(oil: Oil)
    }
}