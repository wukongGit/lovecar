package com.sunc.car.lovecar.my

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.sunc.base.BaseBindingActivity
import com.sunc.car.lovecar.R
import com.sunc.car.lovecar.bmob.Feedback
import com.sunc.car.lovecar.databinding.ActivityFeedbackBinding
import com.sunc.car.lovecar.toast
import com.sunc.utils.AndroidUtils
import kotlinx.android.synthetic.main.layout_title_bar.*

class FeedbackActivity : BaseBindingActivity<ActivityFeedbackBinding>() {
    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        @SuppressLint("SetTextI18n")
        override fun afterTextChanged(s: Editable) {
            val newText = s.toString()
            with(mBinding) {
                if (newText.isEmpty()) {
                    tvInputNum.text = "0/200"
                } else {
                    tvInputNum.text = newText.length.toString() + "/200"
                }
            }
        }
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityFeedbackBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_feedback)
    }

    override fun initView() {
        with(mBinding) {
            head_title.text = getString(R.string.feed_back)
            head_back.visibility = View.VISIBLE
            head_back.setOnClickListener{
                finish()
            }
            btnSubmit.setOnClickListener {
                submit(etInput.text.toString(), etContactMethod.text.toString(), getSystemParameter())
            }
            tvInputNum.addTextChangedListener(mTextWatcher)
        }
    }

    private fun submit(content: String, contact: String, info: String) {
        if (content.isEmpty()) {
            toast(getString(R.string.submit_feedback_required))
            return
        }
        val feedback = Feedback()
        feedback.content = content
        feedback.contact = contact
        feedback.phoneInfo = info
        feedback.save(object : SaveListener<String>() {
            override fun done(s: String, e: BmobException?) {
                if (e == null) {
                    toast(getString(R.string.submit_success))
                    finish()
                } else {
                    toast(getString(R.string.submit_fail))
                }
            }
        })
    }

    private fun getSystemParameter(): String {
        val builder = StringBuilder()
        builder.append("手机厂商：" + AndroidUtils.getDeviceBrand())
        builder.append("手机型号：" + AndroidUtils.getSystemModel())
        builder.append("手机当前系统语言：" + AndroidUtils.getSystemLanguage())
        builder.append("Android系统版本号：" + AndroidUtils.getSystemVersion())
        return builder.toString()
    }
}
