package com.sunc.car.lovecar.third

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunc.base.BaseBindingAdapter
import com.sunc.base.DataBoundViewHolder
import com.sunc.car.lovecar.databinding.ItemViolationBinding
import com.sunc.car.lovecar.yun.ApiViolationModel

/**
 * Created by Administrator on 2017/12/11.
 */
class ViolationListAdapter(private val mList: List<ApiViolationModel.ApiViolationDetail>): BaseBindingAdapter<ItemViolationBinding>() {

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBoundViewHolder<ItemViolationBinding> {
        return DataBoundViewHolder(ItemViolationBinding.inflate(LayoutInflater.from(parent!!.context), parent, false))
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemViolationBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.item = mList[position]
        holder.binding.executePendingBindings()
    }
}