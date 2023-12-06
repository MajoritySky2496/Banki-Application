package com.example.bankiapplication.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import com.example.bankiapplication.data.WebViewImpl
import com.example.bankiapplication.databinding.FragmentWebviewBinding
import com.example.bankiapplication.presentation.WebViewViewModel
import com.example.bankiapplication.ui.model.WebViewFragmentState
import com.example.bankiapplication.util.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WebViewFragment:BindingFragment<FragmentWebviewBinding>() {
    private val viewModel: WebViewViewModel by viewModel { parametersOf() }
    lateinit var webView: WebView


    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWebviewBinding {
        return FragmentWebviewBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.checkPermission(requireActivity())
        viewModel.viewStateLiveData.observe(requireActivity()) { render(it) }
        webView = binding.webView
        showWebView(webView)
        binding.arrowBack.setOnClickListener {
            webViewGoBack(webView)
        }
        binding.arrowForward.setOnClickListener {
            webViewGoForward(webView)
        }
        binding.reload.setOnClickListener {
            webViewReload(webView)
        }
        binding.cross.setOnClickListener {
            webView.loadUrl(WebViewImpl.START_URL)
            webView.clearCache(true)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {
                viewModel.finish(webView)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun render(state: WebViewFragmentState) {
        when (state) {
            is WebViewFragmentState.Finish -> finish()
            is WebViewFragmentState.ShowToolbar -> showToolbar()
            is WebViewFragmentState.HideToolbar -> hideToolbar()
            is WebViewFragmentState.Loading -> showLoading()
            is WebViewFragmentState.ShowView -> showView(state.url)
        }
    }

    private fun showWebView(webView: WebView) {
        viewModel.showWebView(webView)
    }

    private fun webViewGoBack(webView: WebView) {
        if(webView.url!=WebViewImpl.START_URL) {
            viewModel.webViewGoBack(webView)
        }
    }
    private fun webViewGoForward(webView: WebView){
            viewModel.webViewGoForward(webView)
    }
    private fun webViewReload(webView: WebView){
        viewModel.webViewReload(webView)
    }

    private fun finish() {
        requireActivity().finish()
    }
    private fun showToolbar(){
        binding.toolbar.visibility = View.VISIBLE
    }
    private fun hideToolbar(){
        binding.toolbar.visibility = View.GONE
    }
    private fun showLoading(){
        binding.webView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.toolbar.visibility = View.GONE

    }
    private fun showView(url:String){
        if(url!="https://credp.site/auto-matic-zaem"){
            binding.webView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
            binding.toolbar.visibility = View.VISIBLE
        }else{
            binding.webView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
            binding.toolbar.visibility = View.GONE
        }

    }

}