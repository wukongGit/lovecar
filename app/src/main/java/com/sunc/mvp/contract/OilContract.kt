package com.sunc.mvp.contract

import com.sunc.car.lovecar.bmob.Car
import com.sunc.car.lovecar.bmob.Oil

/**
 * Created by Administrator on 2017/11/14.
 */
interface OilContract {
    interface View {
        fun  setNormalData(mountTotal: String, feeTotal: String, mountMax: String, feeMax: String, first: String)
        fun  setDetailData(results: MutableList<Oil>?)
        fun  onError(msg:String)
    }

    interface Model {
        fun getNormalData(car: Car, view: OilContract.View)
        fun getDetailData(car: Car, view: OilContract.View)
    }

    interface Presenter {
        fun getData(car: Car)
    }
}