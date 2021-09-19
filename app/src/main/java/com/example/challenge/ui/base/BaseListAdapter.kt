package com.example.challenge.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Function to create a ListAdapter with DiffUtil
 * @param C
 * @constructor
 */
abstract class BaseListAdapter<C>(
    itemSame : (C,C) -> Boolean,
    contentSame : (C,C) -> Boolean
) : ListAdapter<C, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<C>(){
    override fun areContentsTheSame(oldItem: C, newItem: C): Boolean = itemSame(oldItem, newItem)
    override fun areItemsTheSame(oldItem: C, newItem: C): Boolean = contentSame(oldItem, newItem)
}){

    /**
     * abstract function to be called when the recyclerview needs a new viewholder of a given type when it needs to represent an item
     * @param parent ViewGroup
     * @param inflater LayoutInflater
     * @param viewType Int
     * @return RecyclerView.ViewHolder
     */
    abstract fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType : Int,
    ): RecyclerView.ViewHolder

    /**
     * override the default viewholder from ListAdapter.onCreateViewHolder
     * @param parent ViewGroup
     * @param viewType Int
     * @return ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = onCreateViewHolder(
        parent = parent,
        inflater = LayoutInflater.from(parent.context),
        viewType = viewType
    )

}
