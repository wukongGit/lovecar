package com.sunc.mvp.presenter

import com.sunc.base.BasePresenter
import com.sunc.mvp.contract.OilPriceContract
import com.sunc.mvp.model.ServiceModel
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class OilPricePresenter
@Inject constructor(private val mModel: ServiceModel,
                    private val mView: OilPriceContract.View): OilPriceContract.Presenter, BasePresenter() {
    override fun getCity() {
        val subscription = Observable.create(Observable.OnSubscribe<List<String>> { p0 ->
            p0?.onNext(mModel.getOilProvince())
            p0?.onCompleted()
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    model ->
                    mView.setCity(model)
                })

        addSubscription(subscription)
    }

    override fun getOilPrice(province: String) {
        val subscription = Observable.create(Observable.OnSubscribe<Map<String, String>> { p0 ->
            p0?.onNext(mModel.getOilPrice(province))
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