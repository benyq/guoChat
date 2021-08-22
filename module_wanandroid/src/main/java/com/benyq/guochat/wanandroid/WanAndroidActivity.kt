package com.benyq.guochat.wanandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.benyq.guochat.wanandroid.ui.page.HomePage
import com.benyq.guowanandroid.ui.theme.GuoWanAndroidTheme
import com.benyq.module_base.RouterPath
import com.google.accompanist.pager.ExperimentalPagerApi
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@ExperimentalAnimationApi
@AndroidEntryPoint
@Route(path = RouterPath.WAN_ANDROID)
class WanAndroidActivity : AppCompatActivity() {

    private lateinit var startLoginLauncher: ActivityResultLauncher<Intent>
    private var selected by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GuoWanAndroidTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        HomePage()
//                        AnimatedVisibilityTest(selected = selected)
                    }
                }
            }
        }
        createLoginLauncher()

        lifecycleScope.launch {
            while (true) {
                delay(2000)
                selected = !selected
            }

        }
    }

    private fun createLoginLauncher() {
        startLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
                Logger.d("activity result ${result?.data?.getStringExtra("value") ?: ""}")
            }
    }
}