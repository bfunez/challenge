package com.example.challenge.ui.blog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.challenge.R
import com.example.challenge.databinding.BlogFragmentBinding
import com.example.challenge.domain.model.Post
import com.example.challenge.ui.base.BaseFragment
import com.example.challenge.ui.blog.adapter.BlogListAdapter

/**
 * Blog fragment
 * @property viewModel BlogsViewModel
 * @property adapter BlogListAdapter
 * @property listObserver Observer<List<Post>>
 */
class BlogFragment : BaseFragment<BlogFragmentBinding>(
    R.layout.blog_fragment
) {
    private val viewModel: BlogsViewModel = BlogsViewModel()
    private lateinit var adapter: BlogListAdapter

    override fun onInitDataBinding() {
        viewBinding.viewModel = viewModel
        adapter = BlogListAdapter(viewModel)
    }

    private val listObserver = Observer<List<Post>> {
        adapter.submitList(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postList.observe(viewLifecycleOwner, listObserver)
        viewModel.state.observe(viewLifecycleOwner, ::onViewEvent)
        viewBinding.blogList.adapter = adapter
        viewModel.loadData()
    }

    private fun onViewEvent(viewEvent: BlogFragmentState) =
        when (viewEvent) {
            is BlogFragmentState.OpenDetailView -> {
                findNavController().navigate(
                    BlogFragmentDirections.actionBlogFragmentToDetailFragment(
                        viewEvent.url
                    )
                )
            }
        }

}