package com.sunc.mvp.presenter

import com.sunc.base.BasePresenter
import com.sunc.mvp.contract.VinContract
import com.sunc.mvp.model.ServiceModel
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Administrator on 2017/11/14.
 */
class VinPresenter
@Inject constructor(private val mModel: ServiceModel,
                    private val mView: VinContract.View): VinContract.Presenter, BasePresenter() {
    override fun getVinInfo(vin: String) {
        val subscription = Observable.create(Observable.OnSubscribe<Map<String, String>> { p0 ->
            p0?.onNext(mModel.getVinInfo(vin))
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