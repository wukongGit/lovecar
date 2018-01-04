package com.sunc.mvp.contract

import com.sunc.car.lovecar.bill.MonthBill
import com.sunc.car.lovecar.bmob.Bill
import com.sunc.car.lovecar.bmob.Car
import lecho.lib.hellocharts.model.SliceValue

/**
 * Created by Administrator on 2017/11/14.
 */
interface BillContract {
    interface View {
        fun  setData(results: List<Bill>?)
        fun setOutStatisticData(total: String)
        fun setInStatisticData(total: String)
        fun setMonthStatisticData(total: List<MonthBill>)
        fun setPieOutStatisticData(total: List<SliceValue>)
        fun setPieInStatisticData(total: List<SliceValue>)
        fun onError(msg: String)
    }

    interface Model {
        fun getData(page: Int, car: Car, view: BillContract.View)
        fun statisticData(car: Car, view: BillContract.View)
        fun billPieStatistic(car: Car, view: BillContract.View)
    }

    interface Presenter {
        fun getData(page: Int, car: Car)
        fun statisticData(car: Car)
        fun billPieStatistic(car: Car)
    }
}