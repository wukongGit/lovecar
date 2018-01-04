package com.sunc.base

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.otto.Subscribe
import com.sunc.car.lovecar.eventbus.EventBus
import com.sunc.car.lovecar.eventbus.NotifyType
import com.umeng.analytics.MobclickAgent

/**
 * Created by Administrator on 2017/11/14.
 */
abstract class BaseBingingFragment<B: ViewDataBinding> : Fragment(), NotifyType.INotify {
    lateinit var mBinding : B
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = createDataBinding(inflater,container,savedInstanceState)
        initView(savedInstanceState)
        return mBinding.root

    }

    abstract fun  createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.bus().register(this)
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(this.javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd(this.javaClass.simpleName)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.bus().unregister(this)
    }

    abstract fun initView(savedInstanceState: Bundle?)

    @Subscribe
    override fun onNotify(type: NotifyType?) {

    }
}