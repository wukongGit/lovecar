package com.sunc.car.lovecar.third

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.ActivityViolationBinding
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.toast
import com.sunc.car.lovecar.yun.ApiViolationModel
import com.sunc.di.component.ViolationModule
import com.sunc.mvp.contract.ViolationContract
import com.sunc.mvp.presenter.ViolationPresenter
import kotlinx.android.synthetic.main.layout_title_bar.*
import javax.inject.Inject

/**
 * 违章查询
 * */
class CarViolationActivity : BaseBindingActivity<ActivityViolationBinding>(), ViolationContract.View {
    @Inject lateinit var mPresenter: ViolationPresenter
    var mCity: ApiViolationModel.ApiViolationCity? = null
    private lateinit var mCityDialog: SimpleCityDialogFragment
    private var mInited = false

    override fun setCity(list: List<ApiViolationModel.ApiViolationProvince>) {
        if (mInited) {
            var cities = ArrayList<ApiViolationModel.ApiViolationCity>()
            for (item in list) {
                cities.addAll(item.list)
            }
            mCityDialog = SimpleCityDialogFragment()
            mCityDialog.setData(cities)
            mCityDialog.setFilterCallback(object : SimpleCityDialogFragment.FilterCallback {
                override fun filter(city: ApiViolationModel.ApiViolationCity?) {
                    if (city != null) {
                        mCity = city
                        mBinding.city = mCity
                    }
                }
            })
        }
    }

    override fun setResult(result: ApiViolationModel) {
        var intent = Intent(this, ViolationDetailActivity::class.java)
        intent.putExtra(ViolationDetailActivity.VIOLATION, result)
        startActivity(intent)
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityViolationBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_violation)
    }

    override fun initView() {
        getMainComponent().plus(ViolationModule(this)).inject(this)
        with(mBinding) {
            head_title.text = getString(R.string.service_car_fine)
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener { finish() }
            btnCity.setOnClickListener {
                mPresenter.getCity()
            }
            btnQuery.setOnClickListener {
                val num = etNum.text.toString()
                val vin = etVin.text.toString()
                val engin = etEngine.text.toString()
                val city = mCity!!.city
                if (num.isEmpty()) {
                    toast(getString(R.string.car_num_required))
                    return@setOnClickListener
                }
                mPresenter.getViolation(num, vin, engin, city)
            }
        }
    }

    class SimpleCityDialogFragment : AppCompatDialogFragment() {
        private var mFilterCallback: FilterCallback? = null
        private var mList = ArrayList<ApiViolationModel.ApiViolationCity>()
        private var mSelected: ApiViolationModel.ApiViolationCity? = null
        private var mSelectedView: View? = null
        private var mAdapter  = ViolationCitySelectAdapter(mList)

        fun setFilterCallback(callback: FilterCallback) {
            mFilterCallback = callback
        }

        fun setData(data: List<ApiViolationModel.ApiViolationCity>) {
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
            fun filter(city: ApiViolationModel.ApiViolationCity?)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unSubscribe()
    }
}
