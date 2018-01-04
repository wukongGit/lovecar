package com.sunc.mvp.contract

import com.sunc.car.lovecar.yun.ApiCityModel


/**
 * Created by Administrator on 2017/11/14.
 */
interface VehicleLimitContract {
    interface View {
        fun setData(model: List<ApiCityModel>?)
        fun setResult(model: ApiCityModel?)
    }

    interface Model {
        fun getCity(): List<ApiCityModel>
        fun getCityLimit(city: String, date: String): ApiCityModel
    }

    interface Presenter {
        fun getCity()
        fun getCityLimit(city: String, date: String)
    }

}