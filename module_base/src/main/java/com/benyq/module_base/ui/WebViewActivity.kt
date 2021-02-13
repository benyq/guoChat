package com.benyq.module_base.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.benyq.module_base.databinding.ActivityWebViewBinding
import com.benyq.module_base.ext.fromM
import com.benyq.module_base.ui.base.BaseActivity

/**
 * @author benyq
 * @time 2020/4/28
 * @e-mail 1520063035@qq.com
 * @note  访问网页Activity
 */

class WebViewActivity : BaseActivity<ActivityWebViewBinding>() {

    companion object {
        private const val WEB_URL = "webUrl"
        private const val WEB_TITLE = "webTitle"
        fun gotoWeb(context: Context, webUrl: String, webTitle: String) {
            context.startActivity(Intent(context, WebViewActivity::class.java).apply {
                putExtra(WEB_URL, webUrl)
                putExtra(WEB_TITLE, webTitle)
            })
        }
    }

    private lateinit var mWebView: WebView
    private lateinit var mWebUrl: String
    private lateinit var mWebTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        mWebUrl = intent.getStringExtra(WEB_URL) ?: intent.data.toString()
        mWebTitle = intent.getStringExtra(WEB_TITLE) ?: ""
        super.onCreate(savedInstanceState)
    }

    override fun provideViewBinding() = ActivityWebViewBinding.inflate(layoutInflater)

    override fun initView() {
        super.initView()
        binding.headerView.setBackAction { finish() }
        mWebView = WebView(this)
        binding.llContainer.addView(mWebView)
        initWebView()

        mWebView.loadUrl(mWebUrl)
    }

    private fun initWebView() {
        val webSettings = mWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.allowUniversalAccessFromFileURLs = true
        if (fromM()) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
        }
        mWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.headerView.setToolbarTitle(mWebTitle)
            }
        }

        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    binding.progressBar.progress = 100
                    binding.progressBar.visibility = View.GONE
                } else {
                    if (binding.progressBar.visibility == View.GONE) binding.progressBar.visibility = View.VISIBLE
                    binding.progressBar.progress = newProgress
                    binding.headerView.setToolbarTitle("正在加载中")
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
            }
        }
    }


    override fun onDestroy() {

        // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
        // destory()
        val parent = mWebView.parent
        if (parent != null) {
            (parent as ViewGroup).removeView(mWebView)
        }
        mWebView.stopLoading()
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        mWebView.settings.javaScriptEnabled = false
        mWebView.clearHistory()
        mWebView.clearView()
        mWebView.removeAllViews()
        mWebView.destroy()
        super.onDestroy()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mWebView.canGoBack()) {
            mWebView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
