package com.sunc.car.lovecar.my

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunc.base.BaseBindingAdapter
import com.sunc.base.DataBoundViewHolder
import com.sunc.car.lovecar.databinding.ItemServiceBinding

/**
 * Created by Administrator on 2017/11/22.
 */
class ServiceAdapter(private val mList: List<ServiceItem>): BaseBindingAdapter<ItemServiceBinding>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBoundViewHolder<ItemServiceBinding> {
        return DataBoundViewHolder(ItemServiceBinding.inflate(LayoutInflater.from(parent!!.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemServiceBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.item = mList[position]
        holder.binding.executePendingBindings()
    }
}