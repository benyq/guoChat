package com.benyq.guochat.wanandroid.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 *
 * @author benyq
 * @date 2021/8/20
 * @email 1520063035@qq.com
 *
 */

@Composable
fun HomePage() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Hello World")
        Text(text = "你好世界")
    }
}

@Composable
@Preview
fun ShowHomePage() {
    HomePage()
}