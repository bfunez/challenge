package com.example.challenge.ui.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * To add databinding to the viewholder
 * @param C: ViewDataBinding
 * @property binding C
 * @constructor
 */
abstract class BaseViewHolder<C: ViewDataBinding>(
    val binding : C
) : RecyclerView.ViewHolder(binding.root)