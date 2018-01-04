package com.sunc.car.lovecar.third

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.ActivityVoilationDetailBinding
import com.sunc.car.lovecar.yun.ApiViolationModel

class ViolationDetailActivity : BaseBindingActivity<ActivityVoilationDetailBinding>() {
    companion object {
        val VIOLATION = "VIOLATION"
    }

    private lateinit var mAdapter: ViolationListAdapter
    private lateinit var  mApiViolationModel: ApiViolationModel

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityVoilationDetailBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_voilation_detail)
    }

    override fun initView() {
        mApiViolationModel = intent.getSerializableExtra(VIOLATION) as ApiViolationModel
        with(mBinding) {
            tvAmount.text = getString(R.string.violation_total_num).plus("：").plus(mApiViolationModel.amount)
            tvUntreated.text = getString(R.string.violation_untreated_num_tag).plus("：").plus(mApiViolationModel.untreated)
            tvTotalFine.text = getString(R.string.violation_untreated_fine).plus("：").plus(mApiViolationModel.totalFine)
            tvTotalPoints.text = getString(R.string.violation_untreated_score).plus("：").plus(mApiViolationModel.totalPoints)
            tvFineTop.text = getString(R.string.violation_untreated_fine).plus("：").plus(mApiViolationModel.totalFine)
            tvScoreTop.text = getString(R.string.violation_untreated_score).plus("：").plus(mApiViolationModel.totalPoints)
            tvUntreatedTop.text = mApiViolationModel.untreated.plus(getString(R.string.violation_untreated_num))
            appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                val scroll = java.lang.Math.abs(verticalOffset).toFloat()
                val alpha = 1 - scroll / appBarLayout.totalScrollRange
                tvAmount.alpha = alpha
                tvUntreated.alpha = alpha
                tvTotalFine.alpha = alpha
                tvTotalPoints.alpha = alpha
                llData.alpha = 1 - alpha
            }
            recyclerView.layoutManager = LinearLayoutManager(this@ViolationDetailActivity)
            mAdapter =  ViolationListAdapter(mApiViolationModel.violations)
            recyclerView.adapter = mAdapter
        }
    }
}
