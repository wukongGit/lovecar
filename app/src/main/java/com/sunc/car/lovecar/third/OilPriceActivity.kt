package com.sunc.car.lovecar.third

import android.app.AlertDialog
import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.ActivityOilPriceBinding
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.toast
import com.sunc.car.lovecar.yun.ApiKeyValue
import com.sunc.di.component.OilPriceModule
import com.sunc.mvp.contract.OilPriceContract
import com.sunc.mvp.presenter.OilPricePresenter
import kotlinx.android.synthetic.main.layout_title_bar.*
import java.util.ArrayList
import javax.inject.Inject

class OilPriceActivity : BaseBindingActivity<ActivityOilPriceBinding>(), OilPriceContract.View {
    @Inject lateinit var mPresenter: OilPricePresenter
    private var mList = ArrayList<ApiKeyValue>()
    private lateinit var mAdapter: KeyValueAdapter
    private lateinit var mCityDialog: SimpleCityDialogFragment
    private var mInited = false

    override fun setResult(model: Map<String, String>) {
        mList.clear()
        for ((key, value) in model) {
            mList.add(ApiKeyValue(key, value))
        }
        mList.let {
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun setCity(list: List<String>) {
        mInited = true
        if (mInited) {
            mCityDialog = SimpleCityDialogFragment()
            mCityDialog.setData(list)
            mCityDialog.setFilterCallback(object : SimpleCityDialogFragment.FilterCallback {
                override fun filter(city: String?) {
                    if (city != null) {
                        with(mBinding) {
                            btnCity.text = city
                        }
                        mPresenter.getOilPrice(city)
                    }
                }
            })
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityOilPriceBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_oil_price)
    }

    override fun initView() {
        mAdapter = KeyValueAdapter(mList)
        getMainComponent().plus(OilPriceModule(this)).inject(this)
        with(mBinding) {
            head_title.text = getString(R.string.service_oil_price)
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener { finish() }
            recyclerView.layoutManager = LinearLayoutManager(this@OilPriceActivity)
            recyclerView.adapter = mAdapter
            btnCity.setOnClickListener {
                if (!mInited) {
                    toast(getString(R.string.initializing))
                    return@setOnClickListener
                }
                mCityDialog.show(supportFragmentManager, "test-simple-city")
            }
            mPresenter.getCity()
        }
    }

    class SimpleCityDialogFragment : AppCompatDialogFragment() {
        private var mFilterCallback: FilterCallback? = null
        private var mList = ArrayList<String>()
        private var mSelected: String? = null
        private var mSelectedView: View? = null
        private var mAdapter  = OilCitySelectAdapter(mList)

        fun setFilterCallback(callback: FilterCallback) {
            mFilterCallback = callback
        }

        fun setData(data: List<String>) {
            mList.clear()
            mList.addAll(data)
            mAdapter.notifyDataSetChanged()
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val inflater = activity.layoutInflater
            val view = inflater.inflate(R.layout.dialog_recycleview, null)
            val widget: RecyclerView = view.findViewById(R.id.recyclerView)
            widget.layoutManager = GridLayoutManager(activity, 3)
            mAdapter.setOnItemClickListener { pos ->
                if (mSelectedView != null) {
                    mSelectedView!!.isSelected = false
                }
                mSelectedView = widget.getChildAt(pos)
                mSelectedView!!.isSelected = true
                val click = mList[pos]
                mSelected = if (click == mSelected) {
                    null
                } else {
                    click
                }
            }
            widget.adapter = mAdapter

            return AlertDialog.Builder(activity)
                    .setTitle(getString(R.string.select_city))
                    .setView(view)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        mFilterCallback?.filter(mSelected)
                    }
                    .create()
        }

        interface FilterCallback {
            fun filter(city: String?)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unSubscribe()
    }
}
