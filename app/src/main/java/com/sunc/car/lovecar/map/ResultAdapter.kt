package com.sunc.car.lovecar.map

import android.view.LayoutInflater
import android.view.ViewGroup
import com.amap.api.services.core.PoiItem
import com.sunc.base.BaseBindingAdapter
import com.sunc.base.DataBoundViewHolder
import com.sunc.car.lovecar.databinding.ItemResultBinding

/**
 * Created by Administrator on 2017/11/27.
 */
class ResultAdapter(private val mList: List<PoiItem>): BaseBindingAdapter<ItemResultBinding>() {
    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBoundViewHolder<ItemResultBinding> {
        return DataBoundViewHolder(ItemResultBinding.inflate(LayoutInflater.from(parent!!.context), parent, false))
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemResultBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.item = mList[position]
        holder.binding.executePendingBindings()
    }
}