package com.mechwv.placeeventmap.presentation.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebFragment : Fragment() {
    private lateinit var mBinding: WebFragmentBinding
    private val viewModel: WebViewModel by viewModels()
    private lateinit var sdk: YandexAuthSdk
    val REQUEST_LOGIN_SDK = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = WebFragmentBinding.inflate(inflater, container, false)
        val loginOptionsBuilder: YandexAuthLoginOptions.Builder = YandexAuthLoginOptions.Builder()
        val intent = sdk.createLoginIntent(loginOptionsBuilder.build())
        startActivityForResult(intent, 1)
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sdk = YandexAuthSdk(
            context, YandexAuthOptions.Builder(requireContext())
                .enableLogging() // Only in testing builds
                .build()
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_LOGIN_SDK) {
            try {
                val yandexAuthToken = sdk.extractToken(resultCode, data)
                if (yandexAuthToken != null) {
                    viewModel.getProfile(yandexAuthToken.value).observe(viewLifecycleOwner) { result ->
                        if (result != null) {
                            val action = WebFragmentDirections.actionWebFragmentToProfileFragment()
                            findNavController().navigate(action)
                        }
                    }

                }
            } catch (e: YandexAuthException) {
                // Process error
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}