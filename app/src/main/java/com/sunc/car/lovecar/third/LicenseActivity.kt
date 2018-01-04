package com.sunc.car.lovecar.third

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.databinding.ActivityLicenseBinding
import com.sunc.car.lovecar.getMainComponent
import com.sunc.car.lovecar.toast
import com.sunc.car.lovecar.yun.ApiDriverLicenseModel
import com.sunc.di.component.DriverLicenseModule
import com.sunc.mvp.contract.DriverLicenseContract
import com.sunc.mvp.presenter.DriverLicencePresenter
import kotlinx.android.synthetic.main.layout_title_bar.*
import javax.inject.Inject

class LicenseActivity : BaseBindingActivity<ActivityLicenseBinding>(), DriverLicenseContract.View {
    @Inject lateinit var mPresenter: DriverLicencePresenter

    override fun setResult(result: ApiDriverLicenseModel) {
        with(mBinding) {
            llResult.visibility = View.VISIBLE
            tvResult.text = result.score
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityLicenseBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_license)
    }

    override fun initView() {
        getMainComponent().plus(DriverLicenseModule(this)).inject(this)
        with(mBinding) {
            head_title.text = getString(R.string.service_car_score)
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener { finish() }
            btnSearch.setOnClickListener {
                val licenseId = etLicenseid.text.toString()
                val licenseNum = etLicensenumber.text.toString()
                if (licenseId.isEmpty()) {
                    toast(getString(R.string.license_id_required))
                    return@setOnClickListener
                }
                if (licenseNum.isEmpty()) {
                    toast(getString(R.string.license_num_required))
                    return@setOnClickListener
                }
                mPresenter.getDriverLicense(licenseId, licenseNum)
            }
        }
    }

}
