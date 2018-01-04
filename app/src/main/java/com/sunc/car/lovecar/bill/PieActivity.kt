package com.sunc.car.lovecar.bill

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.App
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.bmob.Bill
import com.sunc.car.lovecar.databinding.ActivityPieBinding
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.toast
import com.sunc.di.component.BillModule
import com.sunc.mvp.contract.BillContract
import com.sunc.mvp.presenter.BillPresenter
import com.sunc.utils.AndroidUtils
import com.sunc.utils.Strings
import kotlinx.android.synthetic.main.layout_title_bar.*
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.PieChartView
import javax.inject.Inject

class PieActivity : BaseBindingActivity<ActivityPieBinding>(), BillContract.View {
    companion object {
        val TOTAL_INCOME = "TOTAL_INCOME"
        val TOTAL_OUTCOME = "TOTAL_OUTCOME"
    }
    var mTotalIn = ""
    var mTotalOut = ""

    @Inject lateinit var mPresenter: BillPresenter

    override fun setData(results: List<Bill>?) {}

    override fun setOutStatisticData(total: String) { }

    override fun setInStatisticData(total: String) {}

    override fun setMonthStatisticData(total: List<MonthBill>) {}

    override fun setPieOutStatisticData(total: List<SliceValue>) {
        supportFragmentManager.beginTransaction().add(R.id.container_1, PlaceholderFragment.newInstance(total, getString(R.string.bill_out), mTotalOut)).commit()
    }

    override fun setPieInStatisticData(total: List<SliceValue>) {
        supportFragmentManager.beginTransaction().add(R.id.container_2, PlaceholderFragment.newInstance(total, getString(R.string.bill_in), mTotalIn)).commit()
    }

    override fun onError(msg: String) {
        toast(msg)
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityPieBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_pie)
    }

    override fun initView() {
        head_title.text = getString(R.string.bill_statistic)
        head_back.visibility = View.VISIBLE
        head_back.setOnClickListener{
            finish()
        }
        getMainComponent().plus(BillModule(this)).inject(this)
        mTotalIn = intent.getStringExtra(TOTAL_INCOME)
        mTotalOut = intent.getStringExtra(TOTAL_OUTCOME)
        val car = App.instance.getCar()
        if (car != null && AndroidUtils.isNetworkConnected(this)) {
            mPresenter.billPieStatistic(car)
        }
    }

    /**
     * A fragment containing a pie chart.
     */
    class PlaceholderFragment : Fragment() {

        private var chart: PieChartView? = null
        private var data: PieChartData? = null
        private var mData: List<SliceValue>? = null
        private var mTitle: String = ""
        private var mTotal: String = ""

        private var hasLabels = true
        private var hasLabelsOutside = false
        private var hasCenterCircle = true
        private var hasCenterText1 = true
        private var hasCenterText2 = true
        private var hasLabelForSelected = false

        companion object {
            fun newInstance(list: List<SliceValue>, title: String, total: String): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                fragment.mData = list
                fragment.mTitle = title
                fragment.mTotal = total
                return fragment
            }
        }

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val rootView = inflater!!.inflate(R.layout.fragment_pie_chart, container, false)
            chart = rootView.findViewById(R.id.chart)
            chart!!.onValueTouchListener = ValueTouchListener()
            showData()
            return rootView
        }

        private fun showData() {
            data = PieChartData(mData)
            data!!.setHasLabels(hasLabels)
            data!!.setHasLabelsOnlyForSelected(hasLabelForSelected)
            data!!.setHasLabelsOutside(hasLabelsOutside)
            data!!.setHasCenterCircle(hasCenterCircle)
            if (hasCenterText1) {
                data!!.centerText1 = mTotal
                data!!.centerText1FontSize = ChartUtils.px2sp(resources.displayMetrics.scaledDensity,
                        resources.getDimension(R.dimen.h30).toInt())
            }
            if (hasCenterText2) {
                data!!.centerText2 = mTitle
                data!!.centerText2FontSize = ChartUtils.px2sp(resources.displayMetrics.scaledDensity,
                        resources.getDimension(R.dimen.h18).toInt())
            }
            chart!!.pieChartData = data
        }

        private inner class ValueTouchListener : PieChartOnValueSelectListener {

            override fun onValueSelected(arcIndex: Int, value: SliceValue) {
                var chars = value.labelAsChars
                var label = Strings.char2Str(chars)
                activity.toast(label)
            }

            override fun onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }
    }
}
