package com.sunc.car.lovecar.my

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import c.b.BP
import c.b.PListener
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.kyview.interfaces.AdViewBannerListener
import com.kyview.manager.AdViewBannerManager
import com.squareup.otto.Subscribe
import com.sunc.base.BaseBingingFragment
import com.sunc.car.lovecar.App
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.bmob.Bill
import com.sunc.car.lovecar.bmob.Config
import com.sunc.car.lovecar.databinding.FragmentMyBinding
import com.sunc.car.lovecar.eventbus.NotifyType
import com.sunc.car.lovecar.login.LoginActivity
import com.sunc.car.lovecar.third.*
import com.sunc.car.lovecar.toast
import com.sunc.utils.AndroidUtils
import com.sunc.view.ItemDecoration

/**
 * Created by Administrator on 2017/11/14.
 */
class MyFragment : BaseBingingFragment<FragmentMyBinding>(), AdViewBannerListener {
    //val TAG = "ADVIEW_MyFragment"
    override fun onAdFailed(p0: String?) {
        //Log.d(TAG, "onAdFailed==" + p0)
    }

    override fun onAdDisplay(p0: String?) {
        //Log.d(TAG, "onAdDisplay==" + p0)
    }

    override fun onAdClick(p0: String?) {
        //Log.d(TAG, "onAdClick==" + p0)
    }

    override fun onAdReady(p0: String?) {
        //Log.d(TAG, "onAdReady==" + p0)
    }

    override fun onAdClose(p0: String?) {
        with(mBinding) {
            llAd?.removeView(llAd.findViewWithTag<View>(p0))
        }
    }

    private var mList = ArrayList<ServiceItem>()
    private lateinit var mAdapter: ServiceAdapter

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentMyBinding {
        return FragmentMyBinding.inflate(inflater, container, false)
    }

    override fun initView(savedInstanceState: Bundle?) {
        with(mBinding) {
            initHeadInfo()
            mList.clear()
            mList.add(ServiceItem(getString(R.string.service_car_query), "ic_map_4s"))
            mList.add(ServiceItem(getString(R.string.service_car_vin), "ic_map_4s"))
            mList.add(ServiceItem(getString(R.string.service_car_limit), "ic_map_4s"))
            mList.add(ServiceItem(getString(R.string.service_car_fine), "ic_map_4s"))
            mList.add(ServiceItem(getString(R.string.service_car_score), "ic_map_4s"))
            mList.add(ServiceItem(getString(R.string.service_oil_price), "ic_map_4s"))
            mAdapter = ServiceAdapter(mList)
            mAdapter.setOnItemClickListener { pos ->
                payAlert(getString(R.string.aliyun_market), getString(R.string.aliyun_market_description), pos)
            }
            rcServices.layoutManager = GridLayoutManager(context, 3)
            rcServices.addItemDecoration(ItemDecoration(context))
            rcServices.adapter = mAdapter
        }
    }

    private fun initHeadInfo() {
        with(mBinding) {
            val user = App.instance.getUser()
            val car = App.instance.getCar()
            if (user == null) {
                ivAvatar.setImageResource(R.mipmap.bg_male)
                tvCurrentCar.text = getString(R.string.login_rightly)
                llHead.setOnClickListener {
                    activity.startActivity(Intent(activity, LoginActivity::class.java))
                }
            } else {
                tvCurrentCar.text = getString(R.string.current_car).plus("：").plus(car?.name)
                llHead.setOnClickListener {
                    activity.startActivity(Intent(activity, HomeActivity::class.java))
                }
            }
        }
    }

    private fun initAd(key: String) {
        with(mBinding) {
            if (llAd == null)
                return
            val view = AdViewBannerManager.getInstance(activity).getAdViewLayout(activity, key)
            if (null != view && null != view.parent) {
                val parent = view.parent as ViewGroup
                parent.removeAllViews()
            }
            AdViewBannerManager.getInstance(activity).requestAd(activity, key, this@MyFragment)
            view!!.tag = key
            llAd.addView(view)
            llAd.invalidate()
        }
    }

    override fun onResume() {
        super.onResume()
        initAd(App.instance.key1)
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
            with(mBinding) {
                llAd?.removeAllViews()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun payAlert(title: String, description: String, pos: Int) {
        val channel = AndroidUtils.getAppMetaData(activity, "UMENG_CHANNEL")
        val query = BmobQuery<Config>()
        query.addWhereEqualTo("appStore", channel)
        query.findObjects(object : FindListener<Config>() {
            override fun done(p0: MutableList<Config>?, p1: BmobException?) {
                var free = if (p1 != null || p0 == null || p0.size == 0) {
                    true
                } else {
                    p0[0].serviceFree
                }
                if (free) {
                    startService(pos)
                    return
                }
                val view = LayoutInflater.from(activity).inflate(R.layout.layout_pay_select, null)
                val dialog = AlertDialog.Builder(activity)
                        .setTitle(getString(R.string.pay_title))
                        .setMessage(getString(R.string.pay_message))
                        .setView(view)

                view.findViewById<View>(R.id.iv_alipay).setOnClickListener {
                    pay(title, description, 1.0, BP.PayType_Alipay, pos)
                }
                view.findViewById<View>(R.id.iv_wechat).setOnClickListener {
                    pay(title, description, 1.0, BP.PayType_Wechat, pos)
                }
                dialog.show()
            }
        })
    }

    private fun pay(title: String, description: String, money: Double, type: Int, pos: Int) {
        BP.pay(title, description, money, type, object : PListener {
                        override fun orderId(p0: String?) {
                            Log.d("pay==", "orderId:" + p0)
                            if (p0 != null) {
                            }
                        }

                        override fun fail(p0: Int, p1: String?) {
                            Log.d("pay==", "fail:$p0, p1:$p1")
                            activity.toast("支付通道暂未开通，尽请期待！")
                            //fail:10012, p1:应用支付截图未审核通过, 请在账号管理->实名认证中提交认证
                        }

                        override fun succeed() {
                            Log.d("pay==", "succeed:")
                            startService(pos)
                        }
                    })
    }

    private fun startService(pos: Int) {
        when(pos) {
            0 -> startActivity(Intent(activity, CarQueryActivity::class.java))
            1 -> startActivity(Intent(activity, VinSearchActivity::class.java))
            2 -> startActivity(Intent(activity, VehicleLimitActivity::class.java))
            3 -> startActivity(Intent(activity, CarViolationActivity::class.java))
            4 -> startActivity(Intent(activity, LicenseActivity::class.java))
            5 -> startActivity(Intent(activity, OilPriceActivity::class.java))
        }
    }

    @Subscribe
    override fun onNotify(type: NotifyType?) {
        super.onNotify(type)
        when (type?.type) {
            NotifyType.EVENT_CAR_CHANGE -> {
                initHeadInfo()
            }
        }
    }

}