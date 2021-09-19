package com.example.challenge.ui.blog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.challenge.databinding.BlogListItemBinding
import com.example.challenge.domain.model.Post
import com.example.challenge.ui.base.BaseViewHolder
import com.example.challenge.ui.blog.BlogsViewModel

/**
 * Holder to adapt posts to [BlogListAdapter]
 * @constructor
 */
class PostViewHolder(
    inflater: LayoutInflater, parent: ViewGroup
) : BaseViewHolder<BlogListItemBinding>(binding = BlogListItemBinding.inflate(inflater, parent, false)
){
    /**
     * Function to bind [PostViewHolder] on [BlogListAdapter.onBindViewHolder]
     * @param viewModel BlogsViewModel
     * @param post Post
     */
    fun bind(viewModel : BlogsViewModel , post : Post){
        binding.viewModel = viewModel
        binding.post = post
        binding.executePendingBindings()
    }
}