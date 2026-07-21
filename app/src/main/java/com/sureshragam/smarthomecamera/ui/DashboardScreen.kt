package com.sureshragam.smarthomecamera.ui

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DashboardScreen() {

    AndroidView(
        factory = { context ->

            WebView(context).apply {

                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()

                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadsImagesAutomatically = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true

                loadUrl("https://smarthome.sureshragam.in")
            }

        }
    )
}