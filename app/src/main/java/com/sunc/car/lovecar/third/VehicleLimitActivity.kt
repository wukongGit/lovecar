package com.sunc.car.lovecar.third

import android.app.AlertDialog
import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.ActivityVehicleLimitBinding
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.toast
import com.sunc.car.lovecar.yun.ApiCityModel
import com.sunc.di.component.VehicleLimitModule
import com.sunc.mvp.contract.VehicleLimitContract
import com.sunc.mvp.presenter.VehiclePresenter
import kotlinx.android.synthetic.main.layout_title_bar.*
import java.text.SimpleDateFormat
import java.util.ArrayList
import javax.inject.Inject

class VehicleLimitActivity : BaseBindingActivity<ActivityVehicleLimitBinding>(), VehicleLimitContract.View {
    @Inject lateinit var mPresenter: VehiclePresenter
    private lateinit var mCityDialog: SimpleCityDialogFragment
    private lateinit var mDateDialog: SimpleCalendarDialogFragment
    private var mInited = false
    private var mCity: ApiCityModel? = null
    private var mDate: String? = null

    override fun setResult(model: ApiCityModel?) {
        if(model != null) {
            mBinding.item = model
        }
    }

    override fun setData(model: List<ApiCityModel>?) {
        mInited = model != null
        if (mInited) {
            mCityDialog = SimpleCityDialogFragment()
            mCityDialog.setData(model!!)
            mCityDialog.setFilterCallback(object : SimpleCityDialogFragment.FilterCallback {
                override fun filter(city: ApiCityModel?) {
                    if (city != null) {
                        mCity = city
                        with(mBinding) {
                            btnCity.text = mCity!!.cityname
                        }
                        if (mDate != null) {
                            mPresenter.getCityLimit(mCity!!.city, mDate!!)
                        }
                    }
                }
            })
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityVehicleLimitBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_vehicle_limit)
    }

    override fun initView() {
        getMainComponent().plus(VehicleLimitModule(this)).inject(this)
        with(mBinding) {
            head_title.text = getString(R.string.service_car_limit)
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener { finish() }
            mDateDialog = SimpleCalendarDialogFragment()
            mDateDialog.setFilterCallback(object : SimpleCalendarDialogFragment.FilterCallback {
                override fun filter(date: String?) {
                    if (date != null) {
                        mDate = date
                        with(mBinding) {
                            btnDate.text = date
                        }
                        if (mCity != null) {
                            mPresenter.getCityLimit(mCity!!.city, mDate!!)
                        }
                    }
                }
            })
            btnDate.setOnClickListener {
                mDateDialog.show(supportFragmentManager, "test-simple-calendar")
            }
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

    class SimpleCalendarDialogFragment : AppCompatDialogFragment(), OnDateSelectedListener {
        private var mFilterCallback: FilterCallback? = null
        private var textView: TextView? = null

        fun setFilterCallback(callback: FilterCallback) {
            mFilterCallback = callback
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val inflater = activity.layoutInflater
            val view = inflater.inflate(R.layout.dialog_calendar, null)

            textView = view.findViewById(R.id.textView)
            val widget: MaterialCalendarView = view.findViewById(R.id.calendarView)
            widget.setOnDateChangedListener(this)

            return AlertDialog.Builder(activity)
                    .setTitle(getString(R.string.select_date))
                    .setView(view)
                    .setPositiveButton(android.R.string.ok, { _, _ ->
                        mFilterCallback?.filter(textView!!.text.toString())
                    })
                    .create()
        }

        override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
            textView!!.text = SimpleDateFormat.getDateInstance().format(date.date)
        }

        interface FilterCallback {
            fun filter(date: String?)
        }
    }

    class SimpleCityDialogFragment : AppCompatDialogFragment() {
        private var mFilterCallback: FilterCallback? = null
        private var mList = ArrayList<ApiCityModel>()
        private var mSelected: ApiCityModel? = null
        private var mSelectedView: View? = null
        private var mAdapter  = CitySelectAdapter(mList)

        fun setFilterCallback(callback: FilterCallback) {
            mFilterCallback = callback
        }

        fun setData(data: List<ApiCityModel>) {
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
            fun filter(city: ApiCityModel?)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unSubscribe()
    }
}
