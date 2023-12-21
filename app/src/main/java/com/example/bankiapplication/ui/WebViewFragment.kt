package com.example.bankiapplication.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.example.bankiapplication.R
import com.example.bankiapplication.databinding.FragmentWebviewBinding
import com.example.bankiapplication.presentation.WebViewViewModel
import com.example.bankiapplication.ui.model.WebViewFragmentState
import com.example.bankiapplication.util.BindingFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WebViewFragment : BindingFragment<FragmentWebviewBinding>() {
    private val viewModel: WebViewViewModel by viewModel { parametersOf() }
    lateinit var webView: WebView
    var startUrl: String? = null
    private var currentUrl: String? = null
    private val listUrl = mutableListOf<String>()


    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWebviewBinding {
        return FragmentWebviewBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showStartPageAnimation()
        webView = binding.webView
        viewModel.checkPermission(requireActivity())
        viewModel.viewStateLiveData.observe(requireActivity()) { render(it) }
        viewModel.handleIntent(requireActivity().intent)

        viewModel.networkStatus(requireContext())
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
            goToHomePage()
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
            is WebViewFragmentState.Loading -> showLoading()
            is WebViewFragmentState.ShowView -> showView(state.url, state.urlList, state.startUrl)
            is WebViewFragmentState.NoConnection -> showNoConnection()
            is WebViewFragmentState.InternetAvailable -> showYesConnection()
            is WebViewFragmentState.ShowViewVpn -> showVpnView(state.url)
        }
    }

    private fun showWebView(webView: WebView) {
        this.webView = webView
        viewModel.showWebView(webView)
    }

    private fun webViewGoBack(webView: WebView) {
        if (webView.url != startUrl) {
            viewModel.webViewGoBack(webView)
        }
    }

    private fun webViewGoForward(webView: WebView) {
        viewModel.webViewGoForward(webView)
    }

    private fun webViewReload(webView: WebView) {
        viewModel.webViewReload(webView)
    }

    private fun finish() {
        requireActivity().finish()
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.toolbar.visibility = View.GONE
    }

    private fun showView(currentUrl: String, urlList: MutableList<String>, startUrl: String) {
        sendReportEvent(currentUrl)
        this.startUrl = startUrl
        listUrl.addAll(urlList)
        this.currentUrl = currentUrl
        if (currentUrl != startUrl) {
            binding.webView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
            binding.toolbar.visibility = View.VISIBLE

        } else {
            binding.webView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
            binding.toolbar.visibility = View.GONE

        }
    }

    private fun showNoConnection() {
        binding.webView.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.INVISIBLE
        binding.toolbar.visibility = View.INVISIBLE
        binding.errorInternet.visibility = View.VISIBLE
    }

    private fun goToHomePage() {
        viewModel.goToHomePage(webView)
    }

    private fun showYesConnection() {
        binding.errorInternet.visibility = View.GONE
        if (listUrl.size >= 1) {
            if (currentUrl != listUrl.get(0)) {
                binding.webView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
                binding.toolbar.visibility = View.VISIBLE
            } else {
                binding.webView.visibility = View.VISIBLE
                binding.toolbar.visibility = View.GONE

            }
        } else {
            viewModel.loadUrl(webView)
        }
    }

    private fun showVpnView(currentUrl: String) {
        sendReportEvent(currentUrl)
        binding.webView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
        binding.toolbar.visibility = View.VISIBLE
        showToast(requireContext(), resources.getString(R.string.vpn))
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun sendReportEvent(currentUrl: String) {
        viewModel.sendReport(requireContext(), currentUrl)
    }

    private fun showStartPageAnimation() {
        this.lifecycleScope.launch {
            delay(3000)
            Dispatchers.Main
            binding.startAnimation.visibility = View.GONE
        }
    }


}