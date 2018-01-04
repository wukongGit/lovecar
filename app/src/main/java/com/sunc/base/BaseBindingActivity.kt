package com.sunc.base

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.otto.Subscribe
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.eventbus.EventBus
import com.sunc.car.lovecar.eventbus.NotifyType
import com.sunc.utils.StatusBarUtils.setWindowStatusBarColor
import com.umeng.analytics.MobclickAgent

/**
 * Created by Administrator on 2017/11/13.
 */
abstract class BaseBindingActivity<B : ViewDataBinding> : AppCompatActivity(), NotifyType.INotify {
    lateinit var mBinding: B
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowStatusBarColor(this, R.color.main_color_normal)
        EventBus.bus().register(this)
        mBinding = createDataBinding(savedInstanceState)

        initView()
    }

    abstract fun initView()

    abstract fun  createDataBinding(savedInstanceState: Bundle?): B

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.bus().unregister(this)
    }

    @Subscribe
    override fun onNotify(type: NotifyType?) {
    }
}