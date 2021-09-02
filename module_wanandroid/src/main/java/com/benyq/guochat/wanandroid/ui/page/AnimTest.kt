package com.benyq.guochat.wanandroid.ui.page

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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


@Composable
fun RotateIcon() {

    val isEnabled = remember { mutableStateOf(true) }
    val isRotated = remember { mutableStateOf(false) }

    val angle: Float by animateFloatAsState(
        targetValue = if (isRotated.value) 90F else 0F,
        animationSpec = tween(
            durationMillis = 500, // duration
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            // disable the button
            isEnabled.value = true
        }
    )

    Icon(
        painter = painterResource(R.drawable.ic_more), contentDescription = null,
        modifier = Modifier
            .size(36.dp)
            .padding(6.dp).rotate(angle).clickable {
                isRotated.value = !isRotated.value
                isEnabled.value = false
            }
    )
}

