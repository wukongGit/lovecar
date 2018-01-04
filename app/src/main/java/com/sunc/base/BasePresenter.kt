package com.sunc.base

import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by Administrator on 2017/12/13.
 */
open class BasePresenter {
    var compositeSubscription = CompositeSubscription()

    protected fun addSubscription(subscription: Subscription) {
        compositeSubscription.add(subscription)
    }

    fun unSubscribe() {
        if(compositeSubscription.hasSubscriptions()){
            compositeSubscription.unsubscribe()
        }
    }
}