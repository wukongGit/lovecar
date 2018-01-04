package com.sunc.base

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * Created by Administrator on 2017/11/14.
 */
class DataBoundViewHolder<out T : ViewDataBinding>(val binding:T) : RecyclerView.ViewHolder(binding.root)