package com.sunc.mvp.presenter

import com.sunc.base.BasePresenter
import com.sunc.car.lovecar.yun.ApiViolationModel
import com.sunc.mvp.contract.ViolationContract
import com.sunc.mvp.model.ServiceModel
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class ViolationPresenter
@Inject constructor(private val mModel: ServiceModel,
                    private val mView: ViolationContract.View): ViolationContract.Presenter, BasePresenter() {
    override fun getCity() {
        val subscription = Observable.create(Observable.OnSubscribe<List<ApiViolationModel.ApiViolationProvince>> { p0 ->
            p0?.onNext(mModel.getViolationCity())
            p0?.onCompleted()
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    model ->
                    mView.setCity(model)
                })

        addSubscription(subscription)
    }

    override fun getViolation(plateNumber: String, vin: String, engineNo: String, city: String) {
        val subscription = Observable.create(Observable.OnSubscribe<ApiViolationModel> { p0 ->
            p0?.onNext(mModel.getViolations(plateNumber, vin, engineNo, city))
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