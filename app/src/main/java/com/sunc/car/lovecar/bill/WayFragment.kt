package com.sunc.car.lovecar.bill

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunc.base.BaseBingingFragment
import com.sunc.car.lovecar.databinding.ViewRecyclerBinding
import com.sunc.car.lovecar.my.ServiceItem

/**
 * Created by Administrator on 2017/11/29.
 */
class WayFragment: BaseBingingFragment<ViewRecyclerBinding>() {
    private var mList = ArrayList<ServiceItem>()
    private lateinit var mAdapter: WayAdapter
    private var mListener: OnSelectedListener?= null

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): ViewRecyclerBinding {
        return ViewRecyclerBinding.inflate(inflater, container, false)
    }

    override fun initView(savedInstanceState: Bundle?) {
        with(mBinding) {
            recyclerView.layoutManager = GridLayoutManager(context, 5)
            mAdapter = WayAdapter(mList)
            recyclerView.adapter = mAdapter
            mAdapter.setOnItemClickListener { pos ->
                mListener?.onSelected(mList[pos])
            }
        }
    }

    public fun refresh() {
        mAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(list: ArrayList<ServiceItem>, listener: OnSelectedListener): WayFragment {
            val fragment = WayFragment()
            fragment.mList = list
            fragment.mListener = listener
            return fragment
        }
    }

    interface OnSelectedListener {
        fun onSelected(item: ServiceItem)
    }
}