package com.sunc.car.lovecar.bill

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.App
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.bmob.Bill
import com.sunc.car.lovecar.bmob.Oil
import com.sunc.car.lovecar.databinding.ActivityRecordBinding
import com.sunc.car.lovecar.eventbus.EventBus
import com.sunc.car.lovecar.eventbus.NotifyType
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.my.ServiceItem
import com.sunc.car.lovecar.toast
import com.sunc.di.component.RecordModule
import com.sunc.mvp.contract.RecordContract
import com.sunc.mvp.presenter.RecordPresenter
import com.sunc.utils.AndroidUtils
import com.sunc.utils.DBKeys
import com.sunc.utils.DBUtils
import java.util.*
import javax.inject.Inject

class RecordActivity : BaseBindingActivity<ActivityRecordBinding>(), RecordContract.View {
    val REQUEST_CODE_ADD_OUT = 100
    val REQUEST_CODE_ADD_IN = 101
    @Inject lateinit var mPresenter: RecordPresenter

    override fun oilAdded() {
        EventBus.bus().post(NotifyType(NotifyType.EVENT_OIL_ADDED))
        finish()
    }
    override fun billAdded() {
        EventBus.bus().post(NotifyType(NotifyType.EVENT_BILL_ADDED))
        if (mCurrentItem.icon == "ic_oil") {
            with(mBinding) {
                val priceText = etPrice.text.toString()
                if (priceText.isBlank()) {
                    toast(getString(R.string.please_input_price))
                    return
                }
                val fee = if(viewPager.currentItem == 0) {0 - etFee.text.toString().toFloat()} else {etFee.text.toString().toFloat()}
                val note = etNote.text.toString()
                val price = etPrice.text.toString().toFloat()
                val mount = tvMount.text.toString().toFloat()
                mPresenter.addOil(Oil(App.instance.getCar()!!, mount, fee, price, "", note))
            }
        } else {
            finish()
        }
    }

    override fun onError(msg: String) {
        toast(msg)
    }

    private var mOutList = ArrayList<ServiceItem>()
    private var mInList = ArrayList<ServiceItem>()
    lateinit var mFragments: MutableList<WayFragment>
    private lateinit var mCurrentItem : ServiceItem
    private val mPriceWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            val newText = s.toString()
            with(mBinding) {
                val feeText = etFee.text.toString()
                if (feeText.isBlank() || newText.isBlank() || newText.toFloat() == 0f) {
                    return
                } else {
                    val fee = feeText.toFloat()
                    val price = newText.toFloat()
                    val mount = fee / price
                    tvMount.text = mount.toString()
                }
            }
        }
    }
    private val mFeeWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            val newText = s.toString()
            with(mBinding) {
                val priceText = etPrice.text.toString()
                if (priceText.isBlank() || newText.isBlank() || priceText.toFloat() == 0f) {
                    return
                } else {
                    val fee = newText.toFloat()
                    val price = priceText.toFloat()
                    val mount = fee / price
                    tvMount.text = mount.toString()
                }
            }
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityRecordBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_record)
    }

    override fun initView() {
        getMainComponent().plus(RecordModule(this)).inject(this)
        val outList = DBUtils.read< ArrayList<ServiceItem>>(DBKeys.BILL_WAY_OUT)
        if (outList == null) {
            mOutList.add(ServiceItem(getString(R.string.out_detail_oil), "ic_oil"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_charge), "ic_charge"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_road), "ic_road"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_parking), "ic_parking"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_wash), "ic_wash"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_spa), "ic_spa"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_format), "ic_format"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_battery), "ic_battery"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_electronic), "ic_electronic"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_fix), "ic_fix"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_hook), "ic_hook"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_key), "ic_key"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_fine), "ic_fine"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_normal), "ic_normal"))
            mOutList.add(ServiceItem(getString(R.string.out_detail_add), "ic_add_circle_outline"))
        } else {
            mOutList = outList
        }
        val inList = DBUtils.read< ArrayList<ServiceItem>>(DBKeys.BILL_WAY_IN)
        if (inList == null) {
            mInList.add(ServiceItem(getString(R.string.in_detail_taxi), "ic_taxi"))
            mInList.add(ServiceItem(getString(R.string.in_detail_goods), "ic_load"))
            mInList.add(ServiceItem(getString(R.string.out_detail_hook), "ic_hook"))
            mInList.add(ServiceItem(getString(R.string.in_detail_gift), "ic_gift"))
            mInList.add(ServiceItem(getString(R.string.out_detail_normal), "ic_normal"))
            mInList.add(ServiceItem(getString(R.string.out_detail_add), "ic_add_circle_outline"))
        } else {
            mInList = inList
        }
        mCurrentItem = ServiceItem(getString(R.string.out_detail_oil), "ic_oil")
        with(mBinding) {
            back.setOnClickListener {
                finish()
            }
            tvMenu.setOnClickListener {
                if (AndroidUtils.isFastDoubleClick()) {
                    return@setOnClickListener
                }
                if (!AndroidUtils.isNetworkConnected(this@RecordActivity)) {
                    toast(getString(R.string.network_error))
                    return@setOnClickListener
                }
                val feeText = etFee.text.toString()
                if (feeText.isBlank()) {
                    toast(getString(R.string.please_input_fee))
                    return@setOnClickListener
                }
                val type = mCurrentItem.name
                val icon = mCurrentItem.icon
                val fee = if(viewPager.currentItem == 0) {0 - feeText.toFloat()} else {feeText.toFloat()}
                val note = etNote.text.toString()
                val tY = Calendar.getInstance().get(Calendar.YEAR)
                val tM = Calendar.getInstance().get(Calendar.MONTH) + 1
                var tMS = tM.toString()
                if (tM < 10) {
                    tMS = "0".plus(tMS)
                }
                val month = tY.toString().plus(tMS)
                mPresenter.addBill(Bill(App.instance.getCar()!!, month, type, icon, fee, note))
            }
            etFee.addTextChangedListener(mFeeWatcher)
            etPrice.addTextChangedListener(mPriceWatcher)
            tab.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    viewPager.currentItem = position
                    if (position == 0) {
                        oilLine.visibility = View.VISIBLE
                        llOilPrice.visibility = View.VISIBLE
                    } else {
                        oilLine.visibility = View.GONE
                        llOilPrice.visibility = View.GONE
                    }
                    mCurrentItem = if (position == 0) mOutList[0] else mInList[0]
                    ivIcon.setImageResource(AndroidUtils.getIconByReflect(mCurrentItem.icon))
                    tvWay.text = mCurrentItem.name
                    AndroidUtils.showInputMethod(this@RecordActivity, etFee)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
            mFragments = ArrayList()
            mFragments.add(WayFragment.newInstance(mOutList, object : WayFragment.OnSelectedListener {
                override fun onSelected(item: ServiceItem) {
                    if (item.icon == "ic_add_circle_outline") {
                        startActivityForResult(Intent(this@RecordActivity, EditWayActivity::class.java), REQUEST_CODE_ADD_OUT)
                        return
                    }
                    mCurrentItem = item
                    if (item.icon == "ic_oil") {
                        oilLine.visibility = View.VISIBLE
                        llOilPrice.visibility = View.VISIBLE
                    } else {
                        oilLine.visibility = View.GONE
                        llOilPrice.visibility = View.GONE
                    }
                    ivIcon.setImageResource(AndroidUtils.getIconByReflect(item.icon))
                    tvWay.text = item.name
                    AndroidUtils.showInputMethod(this@RecordActivity, etFee)
                }
            }))
            mFragments.add(WayFragment.newInstance(mInList, object : WayFragment.OnSelectedListener {
                override fun onSelected(item: ServiceItem) {
                    if (item.icon == "ic_add_circle_outline") {
                        startActivityForResult(Intent(this@RecordActivity, EditWayActivity::class.java), REQUEST_CODE_ADD_IN)
                        return
                    }
                    mCurrentItem = item
                    ivIcon.setImageResource(AndroidUtils.getIconByReflect(item.icon))
                    tvWay.text = item.name
                    AndroidUtils.showInputMethod(this@RecordActivity, etFee)
                }
            }))
            var mAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
                private val titles = arrayOf(getString(R.string.bill_out), getString(R.string.bill_in))

                override fun getPageTitle(position: Int): CharSequence {
                    return titles[position]
                }

                override fun getItem(position: Int): Fragment {
                    return when (position) {
                        0 -> mFragments[0]
                        else -> mFragments[1]
                    }
                }

                override fun getCount(): Int {
                    return 2
                }
            }
            viewPager.adapter = mAdapter
            tab.setViewPager(viewPager)
            viewPager.offscreenPageLimit = 2
        }

        with(mBinding) {



        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val way = data!!.getStringExtra(EditWayActivity.RESULT_DATA)
            val icon = data!!.getStringExtra(EditWayActivity.RESULT_ICON)
            when(requestCode) {
                REQUEST_CODE_ADD_OUT -> {
                    val index = mOutList.size
                    mOutList.add(index - 1, ServiceItem(way, icon))
                    DBUtils.write(DBKeys.BILL_WAY_OUT, mOutList)
                    mFragments[0].refresh()
                }
                REQUEST_CODE_ADD_IN -> {
                    val index = mInList.size
                    mInList.add(index - 1, ServiceItem(way, icon))
                    DBUtils.write(DBKeys.BILL_WAY_IN, mInList)
                    mFragments[1].refresh()
                }
            }
        }
    }

}
