package com.sunc.car.lovecar.third

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.ActivityVinSearchBinding
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.toast
import com.sunc.car.lovecar.yun.ApiKeyValue
import com.sunc.di.component.VinModule
import com.sunc.mvp.contract.VinContract
import com.sunc.mvp.presenter.VinPresenter
import kotlinx.android.synthetic.main.layout_title_bar.*
import java.util.ArrayList
import javax.inject.Inject

class VinSearchActivity : BaseBindingActivity<ActivityVinSearchBinding>(), VinContract.View {
    @Inject lateinit var mPresenter: VinPresenter
    private var mList = ArrayList<ApiKeyValue>()
    private lateinit var mAdapter: KeyValueAdapter

    override fun setResult(model: Map<String, String>) {
        mList.clear()
        for ((key, value) in model) {
            mList.add(ApiKeyValue(key, value))
        }
        mList.let {
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityVinSearchBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_vin_search)
    }

    override fun initView() {
        mAdapter = KeyValueAdapter(mList)
        getMainComponent().plus(VinModule(this)).inject(this)
        with(mBinding) {
            head_title.text = getString(R.string.service_car_vin)
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener { finish() }
            recyclerView.layoutManager = LinearLayoutManager(this@VinSearchActivity)
            recyclerView.adapter = mAdapter
            btnSearch.setOnClickListener {
                val vin = etVin.text.toString()
                if (vin.isEmpty()) {
                    toast(getString(R.string.error_vin_required))
                    return@setOnClickListener
                }
                if (vin.length != 17) {
                    toast(getString(R.string.error_vin_error))
                    return@setOnClickListener
                }
                mPresenter.getVinInfo(vin)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unSubscribe()
    }
}
