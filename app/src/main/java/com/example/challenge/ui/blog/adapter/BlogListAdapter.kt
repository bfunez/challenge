package com.example.challenge.ui.blog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challenge.domain.model.Post
import com.example.challenge.ui.base.BaseListAdapter
import com.example.challenge.ui.blog.BlogsViewModel

/**
 * Adapter to display Posts on BlogFragment [RecyclerView]
 * @property viewModel BlogsViewModel
 * @constructor
 */
class BlogListAdapter (val viewModel : BlogsViewModel): BaseListAdapter<Post>(
    itemSame = { old, new -> old.uuid == new.uuid},
    contentSame = { old, new -> old.uuid == new.uuid}
){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder  = PostViewHolder(inflater, parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PostViewHolder -> holder.bind(viewModel, getItem(position))
        }
    }

}