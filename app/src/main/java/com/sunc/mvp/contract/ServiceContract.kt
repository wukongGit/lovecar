package com.sunc.mvp.contract

import com.sunc.car.lovecar.yun.ApiCarModel
import com.sunc.car.lovecar.yun.ApiCityModel
import com.sunc.car.lovecar.yun.ApiDriverLicenseModel
import com.sunc.car.lovecar.yun.ApiViolationModel


/**
 * Created by Administrator on 2017/11/14.
 */
interface ServiceContract {
    interface View {
        fun setData(model: List<ApiCarModel>?)
    }

    interface Model {
        fun getCarModel(): List<ApiCarModel>?
        fun getCarModelContain(parentid: String): List<ApiCarModel>?
        fun getCarDetail(carid: String): List<ApiCarModel>?
        fun getCity(): List<ApiCityModel>
        fun getCityLimit(city: String, date: String): ApiCityModel
        fun getVinInfo(vin: String): Map<String, String>
        fun getViolationCity(): List<ApiViolationModel.ApiViolationProvince>
        fun getViolations(plateNumber: String, vin: String, engineNo: String, city: String): ApiViolationModel
        fun getDriverLicense(licenseid: String, licensenumber: String): ApiDriverLicenseModel
        fun getOilPrice(province: String): Map<String, String>
        fun getOilProvince(): List<String>
    }

    interface Presenter {
        fun getCarModel()
        fun getCarModelContain(parentid: String)
        fun getCarDetail(carid: String)
    }

}