package com.sunc.mvp.presenter

import com.sunc.base.BasePresenter
import com.sunc.car.lovecar.yun.ApiCityModel
import com.sunc.mvp.contract.VehicleLimitContract
import com.sunc.mvp.model.ServiceModel
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class VehiclePresenter
@Inject constructor(private val mModel: ServiceModel,
                    private val mView: VehicleLimitContract.View): VehicleLimitContract.Presenter, BasePresenter() {
    override fun getCity() {
        val subscription = Observable.create(Observable.OnSubscribe<List<ApiCityModel>> { p0 ->
            p0?.onNext(mModel.getCity())
            p0?.onCompleted()
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    model ->
                    mView.setData(model)
                })

        addSubscription(subscription)
    }

    override fun getCityLimit(city: String, date: String) {
        val subscription = Observable.create(Observable.OnSubscribe<ApiCityModel> { p0 ->
            p0?.onNext(mModel.getCityLimit(city, date))
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