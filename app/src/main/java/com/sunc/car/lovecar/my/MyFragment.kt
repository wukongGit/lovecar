package com.sunc.car.lovecar.my

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.base.bj.paysdk.domain.TrPayResult
import com.base.bj.paysdk.utils.TrPay
import com.kyview.interfaces.AdViewBannerListener
import com.kyview.manager.AdViewBannerManager
import com.squareup.otto.Subscribe
import com.sunc.base.BaseBingingFragment
import com.sunc.car.lovecar.App
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.FragmentMyBinding
import com.sunc.car.lovecar.eventbus.NotifyType
import com.sunc.car.lovecar.login.LoginActivity
import com.sunc.car.lovecar.third.*
import com.sunc.utils.AndroidUtils
import com.sunc.view.ItemDecoration
import java.util.*

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
                payAlert(pos)
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

    private fun payAlert(pos: Int) {
        AlertDialog.Builder(activity,
                android.support.v7.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle(getString(R.string.pay_title))
                .setMessage(getString(R.string.pay_message))
                .setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    pay(mList[pos].name, 10, pos)
                }
                .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .setCancelable(false)
                .create()
                .show()
    }

    private fun pay(tradename: String, amount: Long, pos: Int) {
        val userid = "suncheng911@163.com"//商户系统用户ID(如：trpay@52yszd.com，商户系统内唯一)
        val outtradeno = pos.toString() + System.currentTimeMillis() + AndroidUtils.getSerialNumber()//商户系统订单号(商户系统内唯一)
        val backparams = "name=wukong&age=31"//商户系统回调参数
        val notifyurl = "http://www.wukongapp.icoc.bz"//商户系统回调地址
        /**
         * 发起支付调用
         */
        TrPay.getInstance(activity).callPay(tradename,outtradeno, amount, backparams,notifyurl,userid) { p0, p1, p2, p3, p4, p5, p6 ->
            /**
             * 支付完成回调
             * @param context      上下文
             * @param outtradeno   商户系统订单号
             * @param resultCode   支付状态(RESULT_CODE_SUCC：支付成功、RESULT_CODE_FAIL：支付失败)
             * @param resultString 支付结果
             * @param payType      支付类型（1：支付宝 2：微信）
             * @param amount       支付金额
             * @param tradename    商品名称
             */
            if (p2 == TrPayResult.RESULT_CODE_SUCC.id) {//1：支付成功回调
                TrPay.getInstance(context).closePayView()//关闭快捷支付页面
                Toast.makeText(activity, p3, Toast.LENGTH_LONG).show()
                //支付成功逻辑处理
                startService(pos)
            } else if (p2 == TrPayResult.RESULT_CODE_FAIL.id) {//2：支付失败回调
                Toast.makeText(activity, p3, Toast.LENGTH_LONG).show()
                //支付失败逻辑处理
            }
        }
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