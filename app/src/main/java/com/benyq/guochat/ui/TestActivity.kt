package com.benyq.guochat.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.benyq.guochat.R
import com.benyq.guochat.ui.common.NormalProgressDialogManager
import com.benyq.guochat.ui.common.stateful_loading.CommonStateAdapter
import com.benyq.guochat.ui.common.stateful_loading.LoadingHelper
import com.benyq.mvvm.ext.runOnUiThreadDelayed
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val loadingHelper = LoadingHelper(tvContent)
        loadingHelper.mAdapter = CommonStateAdapter()
        loadingHelper.showLoading()

        runOnUiThreadDelayed(2000L) {
            loadingHelper.showError()
        }
//        runOnUiThreadDelayed(4000L) {
//            loadingHelper.showContent()
//        }

        tvContent.setOnClickListener {
            loadingHelper.showError()
        }
        loadingHelper.onRetryAction = {
            loadingHelper.showLoading()
            runOnUiThreadDelayed(4000L) {
                loadingHelper.showContent()
            }
            NormalProgressDialogManager.showLoading(this, "hello")
        }
    }

}
