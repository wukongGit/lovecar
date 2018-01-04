package com.sunc.mvp.contract

import com.sunc.car.lovecar.yun.ApiDriverLicenseModel


/**
 * Created by Administrator on 2017/11/14.
 */
interface DriverLicenseContract {
    interface View {
        fun setResult(result: ApiDriverLicenseModel)
    }

    interface Presenter {
        fun getDriverLicense(licenseid: String, licensenumber: String)
    }

}