package com.example.challenge.ui.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.challenge.R
import com.example.challenge.databinding.DetailFragmentBinding
import com.example.challenge.ui.base.BaseFragment

/**
 * Fragment to display a [WebView] where the Article link can be displayed
 * @property args DetailFragmentArgs
 */
class DetailFragment : BaseFragment<DetailFragmentBinding>(R.layout.detail_fragment) {
    private val args : DetailFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.webView.loadUrl(args.url)
    }
    override fun onInitDataBinding() {}
}