package com.sunc.mvp.presenter

import com.sunc.base.BasePresenter
import com.sunc.car.lovecar.yun.ApiCarModel
import com.sunc.mvp.contract.ServiceContract
import com.sunc.mvp.model.ServiceModel
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class ServicePresenter
@Inject constructor(private val mModel: ServiceModel,
                    private val mView: ServiceContract.View): ServiceContract.Presenter, BasePresenter() {
    override fun getCarDetail(carid: String) {
        val subscription = Observable.create(Observable.OnSubscribe<List<ApiCarModel>> { p0 ->
            p0?.onNext(mModel.getCarDetail(carid))
            p0?.onCompleted()
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    model ->
                    mView.setData(model)
                })

        addSubscription(subscription)
    }

    override fun getCarModelContain(parentid: String) {
        val subscription = Observable.create(Observable.OnSubscribe<List<ApiCarModel>> { p0 ->
            p0?.onNext(mModel.getCarModelContain(parentid))
            p0?.onCompleted()
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    model ->
                    mView.setData(model)
                })

        addSubscription(subscription)
    }

    override fun getCarModel() {
        val subscription = Observable.create(Observable.OnSubscribe<List<ApiCarModel>> { p0 ->
            p0?.onNext(mModel.getCarModel())
            p0?.onCompleted()
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    model ->
                    mView.setData(model)
                })

        addSubscription(subscription)
    }
}