package com.mechwv.placeeventmap.presentation.profile

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mechwv.placeeventmap.BuildConfig
import com.mechwv.placeeventmap.databinding.WebFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebFragment : Fragment() {
    private lateinit var mBinding: WebFragmentBinding
    private val viewModel: WebViewModel by viewModels()

    private var url: String = "https://oauth.yandex.ru/authorize?" +
            "response_type=token&" +
            "client_id=${BuildConfig.OAUTH_ID}&" +
            "redirect_uri=${BuildConfig.REDIRECT_URI}"


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = WebFragmentBinding.inflate(inflater, container, false)
        CookieManager.getInstance().removeAllCookies(null)
        mBinding.Web.clearCache(true)
        mBinding.Web.settings.javaScriptEnabled = true
        mBinding.Web.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                viewModel.getProfile(url).observe(viewLifecycleOwner, { result ->
                    if (result != null) {
                        val action = WebFragmentDirections.actionWebFragmentToProfileFragment()
                        findNavController().navigate(action)
                    }
                })
            }
        }
        mBinding.Web.loadUrl(url)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}