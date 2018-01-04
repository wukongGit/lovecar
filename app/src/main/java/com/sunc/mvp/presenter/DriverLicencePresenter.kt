package com.sunc.mvp.presenter

import com.sunc.base.BasePresenter
import com.sunc.car.lovecar.yun.ApiDriverLicenseModel
import com.sunc.mvp.contract.DriverLicenseContract
import com.sunc.mvp.model.ServiceModel
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class DriverLicencePresenter
@Inject constructor(private val mModel: ServiceModel,
                    private val mView: DriverLicenseContract.View): DriverLicenseContract.Presenter, BasePresenter() {
    override fun getDriverLicense(licenseid: String, licensenumber: String) {
        val subscription = Observable.create(Observable.OnSubscribe<ApiDriverLicenseModel> { p0 ->
            p0?.onNext(mModel.getDriverLicense(licenseid, licensenumber))
            p0?.onCompleted()
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    model ->
                    mView.setResult(model)
                })

        addSubscription(subscription)
    }

}