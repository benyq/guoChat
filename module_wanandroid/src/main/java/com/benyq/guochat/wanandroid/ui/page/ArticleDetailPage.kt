package com.benyq.guochat.wanandroid.ui.page

import android.view.LayoutInflater
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.benyq.guochat.wanandroid.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * @author benyq
 * @time 2021/8/7
 * @e-mail 1520063035@qq.com
 * @note
 */

@Composable
fun ArticlePage(url: String) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Yellow)

    Scaffold() {

        AndroidView(factory = {
            val rootView = LayoutInflater.from(it).inflate(R.layout.layout_webview, null)
            val webView = rootView.findViewById<WebView>(R.id.webView)

            val webSettings = webView.settings
            webSettings.javaScriptEnabled = true
            webSettings.allowFileAccess = true
            webSettings.allowFileAccessFromFileURLs = true
            webSettings.allowUniversalAccessFromFileURLs = true

            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }
            }

            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                }
            }
            webView.loadUrl(url)

            rootView
        })
    }
}