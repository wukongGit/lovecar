package com.sunc.mvp.contract

import com.sunc.car.lovecar.yun.ApiViolationModel


/**
 * Created by Administrator on 2017/11/14.
 */
interface ViolationContract {
    interface View {
        fun setCity(list: List<ApiViolationModel.ApiViolationProvince>)
        fun setResult(result: ApiViolationModel)
    }

    interface Presenter {
        fun getCity()
        fun getViolation(plateNumber: String, vin: String, engineNo: String, city: String)
    }

}