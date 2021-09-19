package com.example.challenge.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * Setup a base fragment to add easily add databinding to fragments
 * @param V : ViewDataBinding
 * @property layoutId Int
 * @property viewBinding V
 * @constructor
 */
abstract class BaseFragment<V : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : Fragment(){

    lateinit var viewBinding: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //setup databinding
        viewBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitDataBinding()
    }

    /**
     * Function to be called after databinding is setup
     */
    abstract fun onInitDataBinding()
}