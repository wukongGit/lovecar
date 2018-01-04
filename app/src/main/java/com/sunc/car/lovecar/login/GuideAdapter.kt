package com.sunc.car.lovecar.login

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sunc.base.BaseBindingAdapter
import com.sunc.base.DataBoundViewHolder
import com.sunc.car.lovecar.databinding.ItemGuideBinding

/**
 * Created by Administrator on 2017/11/27.
 */
class GuideAdapter(private val mList: List<GuideItem>): BaseBindingAdapter<ItemGuideBinding>() {
    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBoundViewHolder<ItemGuideBinding> {
        return DataBoundViewHolder(ItemGuideBinding.inflate(LayoutInflater.from(parent!!.context), parent, false))
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemGuideBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.item = mList[position]
        holder.binding.executePendingBindings()
    }
}