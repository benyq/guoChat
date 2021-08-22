package com.benyq.guochat.wanandroid.ui.page

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.benyq.guochat.wanandroid.R

/**
 *
 * @author benyq
 * @date 2021/8/10
 * @email 1520063035@qq.com
 */

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilityTest(selected: Boolean) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.Blue, shape = RoundedCornerShape(25.dp))
            .padding(6.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_mine1),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        AnimatedVisibility(selected) {
            Text("编辑", color = Color.White)
        }
    }
}

