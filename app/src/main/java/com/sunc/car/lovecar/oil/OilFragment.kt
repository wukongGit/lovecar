package com.sunc.car.lovecar.oil

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.otto.Subscribe
import com.sunc.base.BaseBingingFragment
import com.sunc.car.lovecar.App
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.bill.RecordActivity
import com.sunc.car.lovecar.bmob.Oil
import com.sunc.car.lovecar.databinding.FragmentOilBinding
import com.sunc.car.lovecar.eventbus.NotifyType
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.login.LoginActivity
import com.sunc.car.lovecar.toast
import com.sunc.di.component.OilModule
import com.sunc.mvp.contract.OilContract
import com.sunc.mvp.presenter.OilPresenter
import com.sunc.utils.AndroidUtils
import com.sunc.utils.Strings
import kotlinx.android.synthetic.main.layout_title_bar.*
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.LineChartView
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Administrator on 2017/11/21.
 */
class OilFragment : BaseBingingFragment<FragmentOilBinding>(), OilContract.View {
    @Inject lateinit var mPresenter: OilPresenter
    var mHolderFragment: PlaceholderFragment? = null
    var mTop:Float = 100f

    override fun setNormalData(mountTotal: String, feeTotal: String, mountMax: String, feeMax: String, first: String) {
        with(mBinding) {
            val mountF = mountTotal.toFloat()
            val feeF = Math.abs(feeTotal.toFloat())
            tvMountSum.text = getString(R.string.oil_total_weight).plus(mountTotal)
            tvFeeSum.text = getString(R.string.oil_total_fee).plus(feeF.toString())
            mTop = Math.max(mountMax.toFloat(), Math.abs(feeMax.toFloat())) + 10
            val startDate = Strings.getYYMMDD(first)
            val tY = Calendar.getInstance().get(Calendar.YEAR)
            val tM = Calendar.getInstance().get(Calendar.MONTH) + 1
            val mmSum: Int = (tY - startDate[0].toInt()) * 12 + tM - startDate[1].toInt() + 1
            tvMountAverage.text = (mountF / mmSum).toString()
            tvFeeAverage.text = (feeF / mmSum).toString()
        }
    }

    override fun setDetailData(results: MutableList<Oil>?) {
        if (mHolderFragment == null) {
            return
        }
        if (results == null || results.isEmpty()) {
            return
        }
        val startDate = Strings.getYYMMDD(results[0].updatedAt)
        val tY = Calendar.getInstance().get(Calendar.YEAR)
        val tM = Calendar.getInstance().get(Calendar.MONTH) + 1
        val mmSum: Int = (tY - startDate[0].toInt()) * 12 + tM - startDate[1].toInt() + 1

        val axisValues: ArrayList<AxisValue> = arrayListOf()
        val mountList: ArrayList<PointValue> = arrayListOf()
        val feeList: ArrayList<PointValue> = arrayListOf()

        for(i in 0 until mmSum) {
            val cmm = startDate[1].toInt() + i
            var dyy = 0
            var dmm = cmm
            if (cmm > 12) {
                dyy = cmm / 12
                dmm = cmm % 12
            }
            val mStr = if (dmm < 10) {
                "0".plus(dmm.toString())
            } else {
                dmm.toString()
            }
            val label = if (dmm == 1) {
                (startDate[0].toInt() + dyy).toString() + mStr
            } else {
                mStr
            }
            axisValues.add(AxisValue((i + 1).toFloat()).setLabel(label))
        }
        if (mmSum < 12) {
            val last = 12 - mmSum
            for (i in 1 .. last) {
                val cm = (tM + i) % 12
                var label = if (cm == 1) {
                    val cy = tY + (tM + i) / 12
                    cy.toString()
                } else {
                    if (cm < 10) {"0".plus(cm.toString())} else cm.toString()
                }
                axisValues.add(AxisValue((i + 1).toFloat()).setLabel(label))
            }
        }
        for (oil in results) {
            val time = oil.updatedAt
            val date = Strings.getYYMMDD(time)
            val x = (date[0].toInt() - startDate[0].toInt()) * 12 + date[1].toInt() - startDate[1].toInt() + 1 + date[2].toFloat() / 30
            val mountPoint = PointValue(x, oil.mount)
            val feePoint = PointValue(x, Math.abs(oil.fee))
            mountList.add(mountPoint)
            feeList.add(feePoint)
        }

        val data: Array<ArrayList<PointValue>> = arrayOf(mountList, feeList)

        val num = if (results.size > 12) results.size else 12
        mHolderFragment!!.setData(data, axisValues, mTop, num.toFloat())
    }

    override fun onError(msg: String) {
        activity.toast(msg)
    }

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentOilBinding {
        return FragmentOilBinding.inflate(inflater, container, false)
    }

    override fun initView(savedInstanceState: Bundle?) {
        context.getMainComponent().plus(OilModule(this)).inject(this)
        mHolderFragment = PlaceholderFragment()
        childFragmentManager.beginTransaction().add(R.id.fl_container, mHolderFragment).commit()
        with(mBinding) {
            tvCurrentCar.text = getString(R.string.current_car).plus("：").plus(App.instance.getCar()?.name)
            tvAddRecord.setOnClickListener {
                val car = App.instance.getCar()
                if (car == null) {
                    startActivity(Intent(activity, LoginActivity::class.java))
                } else {
                    startActivity(Intent(activity, RecordActivity::class.java))
                }
            }
        }
        val car = App.instance.getCar()
        if (car != null && AndroidUtils.isNetworkConnected(activity)) {
            mPresenter.getData(car)
        }
    }

    @Subscribe
    override fun onNotify(type: NotifyType?) {
        super.onNotify(type)
        when (type?.type) {
            NotifyType.EVENT_CAR_CHANGE, NotifyType.EVENT_OIL_ADDED -> {
                val car = App.instance.getCar()
                if (car != null) {
                    with(mBinding) {
                        tvCurrentCar.text = getString(R.string.current_car).plus("：").plus(App.instance.getCar()?.name)
                    }
                    mPresenter.getData(car)
                } else {

                }
            }
        }
    }

    class PlaceholderFragment : Fragment() {
        private var tvFee: TextView? = null
        private var tvOil: TextView? = null
        private var ivCollapse: View? = null
        private var chart: LineChartView? = null
        private var data: LineChartData? = null

        private var hasLines = true
        private var hasPoints = true
        private var shape = ValueShape.CIRCLE
        private var isFilled = false
        private var hasLabels = false
        private var isCubic = false
        private var hasLabelForSelected = false
        private var pointsHaveDifferentColor: Boolean = false

        private var mData: Array<ArrayList<PointValue>>? = null
        private var mAxisValues: ArrayList<AxisValue>? = null
        private var mTop: Float = 100f
        private var mNum: Float = 12f

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            setHasOptionsMenu(true)
            val rootView = inflater!!.inflate(R.layout.fragment_line_chart, container, false)

            tvFee = rootView.findViewById<View>(R.id.tv_fee) as TextView
            tvFee!!.setTextColor(ChartUtils.COLORS[1])
            tvOil = rootView.findViewById<View>(R.id.tv_oil) as TextView
            tvOil!!.setTextColor(ChartUtils.COLORS[0])
            ivCollapse = rootView.findViewById(R.id.iv_collapse)
            ivCollapse!!.setOnClickListener {
                resetViewport()
            }
            chart = rootView.findViewById<View>(R.id.line_chart) as LineChartView
            chart!!.onValueTouchListener = ValueTouchListener()

            if (mData == null) {
                val axisValues: ArrayList<AxisValue> = arrayListOf()
                val mountList: ArrayList<PointValue> = arrayListOf()
                val feeList: ArrayList<PointValue> = arrayListOf()
                val months = arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
                for (i in 0 until  months.size) {
                    axisValues.add(AxisValue(i.toFloat()).setLabel(months[i]))
                    val point = PointValue(i.toFloat(), 0f)
                    mountList.add(point)
                    feeList.add(point)
                }
                mData = arrayOf(mountList, feeList)
                mAxisValues = axisValues
            }
            showData()

            // Disable viewport recalculations, see toggleCubic() method for more info.
            chart!!.isViewportCalculationEnabled = false
            chart!!.zoomType = ZoomType.HORIZONTAL

            return rootView
        }

        fun resetViewport() {
            // Reset viewport height range to (0,100)
            val m = Viewport(chart!!.maximumViewport)
            m.bottom = 0f
            m.top = mTop
            m.left = 1f
            m.right = mNum

            val c = Viewport(chart!!.maximumViewport)
            c.bottom = 0f
            c.top = mTop
            if (mNum > 12) {
                c.left = mNum - 11
                c.right = mNum
            } else {
                c.left = 1f
                c.right = 11f
            }

            chart!!.maximumViewport = m
            chart!!.currentViewport = c
        }

        private fun showData() {
            val lines = ArrayList<Line>()
            val lineSize = mData!!.size
            for (i in 0 until lineSize) {
                val line = Line(mData!![i])
                line.color = ChartUtils.COLORS[i]
                line.shape = shape
                line.isCubic = isCubic
                line.isFilled = isFilled
                line.setHasLabels(hasLabels)
                line.setHasLabelsOnlyForSelected(hasLabelForSelected)
                line.setHasLines(hasLines)
                line.setHasPoints(hasPoints)
                if (pointsHaveDifferentColor) {
                    line.pointColor = ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.size]
                }
                lines.add(line)
            }

            data = LineChartData(lines)
            data!!.axisXBottom = Axis(mAxisValues).setHasLines(true)
            data!!.axisYLeft = Axis().setHasLines(true).setMaxLabelChars(3)
            chart?.lineChartData = data

            resetViewport()
        }

        fun setData(lineData:  Array<ArrayList<PointValue>>, axisValues: ArrayList<AxisValue>, top: Float, num: Float) {
            mData = lineData
            mAxisValues = axisValues
            mTop = top
            mNum = num
            if (chart != null) {
                showData()
            }
        }

        private inner class ValueTouchListener : LineChartOnValueSelectListener {

            override fun onValueSelected(lineIndex: Int, pointIndex: Int, value: PointValue) {
                val month = value.x.toInt()
                var av = mAxisValues!![month - 1]
                var chars = av.labelAsChars
                var label = Strings.char2Str(chars)
                if (label.length > 2) {
                    label = "01"
                }
                val dayF = (value.x - month) * 30
                val day = if (dayF - dayF.toInt() - 0.5 > 0) dayF.toInt() + 1 else dayF.toInt()
                when(lineIndex) {
                    0 -> activity.toast("$label 月 $day 日，加油 ${value.y} 升")
                    1 -> activity.toast("$label 月 $day 日，油费 ${value.y} 元")
                }
            }

            override fun onValueDeselected() {

            }

        }
    }
}