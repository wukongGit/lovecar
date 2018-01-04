package com.sunc.car.lovecar.third

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunc.base.BaseBindingAdapter
import com.sunc.base.DataBoundViewHolder
import com.sunc.car.lovecar.databinding.LayoutKeyValueBinding
import com.sunc.car.lovecar.yun.ApiKeyValue

/**
 * Created by Administrator on 2017/12/11.
 */
class KeyValueAdapter(private val mList: List<ApiKeyValue>): BaseBindingAdapter<LayoutKeyValueBinding>() {

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBoundViewHolder<LayoutKeyValueBinding> {
        return DataBoundViewHolder(LayoutKeyValueBinding.inflate(LayoutInflater.from(parent!!.context), parent, false))
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<LayoutKeyValueBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.item = mList[position]
        holder.binding.executePendingBindings()
    }
}